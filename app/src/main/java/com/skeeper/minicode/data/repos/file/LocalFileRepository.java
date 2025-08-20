package com.skeeper.minicode.data.repos.file;

import com.skeeper.minicode.domain.contracts.repos.file.IFileRepository;
import com.skeeper.minicode.domain.exceptions.file.DomainIOException;
import com.skeeper.minicode.utils.FileUtils;

import java.io.File;

public class LocalFileRepository implements IFileRepository {
    protected final File file;
    protected final String path;

    public LocalFileRepository(String path) {
        this.path = path;
        this.file = new File(path);
    }

    public String getFilePath() {
        return file.getPath();
    }

    public void createFile() {
        FileUtils.createFile(file);
    }

    public void deleteFile() {
        FileUtils.deleteFile(file);
    }

    public void renameFile(String newName) {
        FileUtils.renameFile(file, newName);
    }

    public void moveFile(String targetPath) {
        FileUtils.moveFile(file, new File(targetPath));
    }

    public String readFileText() {
        return FileUtils.readFileText(file);
    }

    public void writeFileText(String text) {
        FileUtils.writeFileText(file, text);
    }


    @Override
    public boolean exists(String path) {
        return new File(path).exists();
    }

    @Override
    public void createFile(String path) throws DomainIOException {
        createFile(file.getPath());
    }

    @Override
    public String readFileText(String path) throws DomainIOException {
        return "";
    }

    @Override
    public void writeFileText(String path, String content) throws DomainIOException {

    }

    @Override
    public void deleteFile(String path) throws DomainIOException {

    }

    @Override
    public void moveFile(String sourcePath, String targetPath) throws DomainIOException {

    }
}