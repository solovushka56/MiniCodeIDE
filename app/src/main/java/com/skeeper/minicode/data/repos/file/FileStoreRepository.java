package com.skeeper.minicode.data.repos.file;

import com.skeeper.minicode.domain.contracts.repos.file.IFileStoreRepository;
import com.skeeper.minicode.domain.exceptions.file.DomainIOException;
import com.skeeper.minicode.domain.exceptions.file.FileCopyException;
import com.skeeper.minicode.domain.exceptions.file.FileCreateException;
import com.skeeper.minicode.domain.exceptions.file.FileDeleteException;
import com.skeeper.minicode.domain.exceptions.file.FileAlreadyExistsException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileStoreRepository implements IFileStoreRepository {

    public void createFolder(File directory, String name) throws IOException {
        File folder = new File(directory, name);
        if (folder.exists()) {
            throw new IOException("Directory already exists: "
                    + folder.getAbsolutePath());
        }
        if (!folder.mkdir()) {
            throw new IOException("Failed to create directory: "
                    + folder.getAbsolutePath());
        }
    }

    @Override
    public void saveFile(String path) throws DomainIOException {
        File file = new File(path);
        if (file.exists()) {
            return;
        }

        File parent = file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new FileCreateException("Failed to create parent directories");
        }

        try {
            if (!file.createNewFile()) {
                throw new FileCreateException("Failed to create file: "
                        + file.getAbsolutePath());
            }
        } catch (IOException e) {
            throw new FileCreateException("Failed to create file: "
                    + file.getAbsolutePath());
        }
    }

    public void deleteFile(String path) throws DomainIOException {
        File file = new File(path);
        if (!file.exists()) {
            throw new FileDeleteException("File not found: " +
                    file.getAbsolutePath());
        }
        deleteRecursive(file);
    }


    @Override
    public void renameFile(String filePath, String newName) throws DomainIOException {
        File source = new File(filePath);
        File target = new File(source.getParent(), newName);
        if (target.exists()) {
            throw new FileAlreadyExistsException("Target file already exists: "
                            + target.getAbsolutePath());
        }

        if (!source.renameTo(target)) {
            performMoveOperation(source, target);
        }
    }


    @Override
    public void moveFile(String sourcePath, String targetPath) throws DomainIOException {
        File source = new File(sourcePath);
        File targetDir = new File(targetPath);

        File target = new File(targetDir, source.getName());
        if (target.exists()) {
            throw new FileAlreadyExistsException("Target file already exists: "
                    + target.getAbsolutePath());
        }

        if (!source.renameTo(target)) {
            performMoveOperation(source, target);
        }
    }

    private void deleteRecursive(File fileOrDir) throws DomainIOException {
        if (fileOrDir.isDirectory()) {
            File[] children = fileOrDir.listFiles();
            if (children != null) {
                for (File child : children) {
                    deleteRecursive(child);
                }
            }
        }
        if (!fileOrDir.delete()) {
            throw new FileDeleteException("Failed to delete: "
                    + fileOrDir.getAbsolutePath());
        }
    }

    private void copyFile(File source, File dest) throws FileCopyException {
        try (InputStream in = new FileInputStream(source);
             OutputStream out = new FileOutputStream(dest)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        } catch (IOException e) {
            throw new FileCopyException(e.getMessage());
        }
    }

    private void performMoveOperation(File source, File target) throws DomainIOException {
        if (source.isDirectory()) {
            if (!target.mkdirs()) {
                throw new FileCreateException(
                        "Failed to create target directory");
            }
            File[] files = source.listFiles();
            if (files != null) {
                for (File file : files) {
                    performMoveOperation(file, new File(target, file.getName()));
                }
            }
        } else {
            copyFile(source, target);
        }
        deleteRecursive(source);
    }

    private static void createFileSync(File file) throws IOException {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IOException("Failed to create parent directories");
        }
        if (!file.createNewFile()) {
            throw new IOException("Failed to create file");
        }
    }
}
