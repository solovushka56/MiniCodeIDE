package com.skeeper.minicode.data.repos.filerepos;

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
    public void createFile() {
        FileUtils.createFile(file);
    }

    @Override
    public void deleteFile() {
        FileUtils.deleteFile(file);
    }

    @Override
    public void renameFile(String newName) {
        FileUtils.renameFile(file, newName);
    }

    @Override
    public void moveFile(File targetDir) {
        FileUtils.moveFile(file, targetDir);
    }

    @Override
    public String readFileText() {
        return FileUtils.readFileText(file);
    }

    @Override
    public void writeFileText(String text) {
        FileUtils.writeFileText(file, text);
    }
}