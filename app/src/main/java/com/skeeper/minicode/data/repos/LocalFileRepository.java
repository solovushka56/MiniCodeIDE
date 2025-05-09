package com.skeeper.minicode.data.repos;

import com.skeeper.minicode.domain.contracts.repos.IFileRepository;
import com.skeeper.minicode.utils.FileUtils;

import java.io.File;

public class LocalFileRepository implements IFileRepository {
    private final File file;

    public LocalFileRepository(File file) {
        this.file = file;
    }

    @Override
    public File getFile() {
       return file;
    }

    @Override
    public void createFile(File file) {
        FileUtils.createFile(file);
    }

    @Override
    public void deleteFile(File file) {
        FileUtils.deleteFile(file);
    }

    @Override
    public void renameFile(File source, String newName) {
        FileUtils.renameFile(source, newName);
    }

    @Override
    public void moveFile(File source, File targetDir) {
        FileUtils.moveFile(source, targetDir);
    }

    @Override
    public String readFileText(File file) {
        return FileUtils.readFileText(file);
    }

    @Override
    public void writeFileText(File file, String text) {
        FileUtils.writeFileText(file, text);

    }
}
