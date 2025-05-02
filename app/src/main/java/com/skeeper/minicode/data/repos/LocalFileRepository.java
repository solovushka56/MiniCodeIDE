package com.skeeper.minicode.data.repos;

import com.skeeper.minicode.domain.contracts.other.callbacks.FileCallback;
import com.skeeper.minicode.domain.contracts.other.callbacks.ReadFileCallback;
import com.skeeper.minicode.domain.contracts.other.callbacks.WriteFileCallback;
import com.skeeper.minicode.domain.contracts.repos.IFileRepository;
import com.skeeper.minicode.utils.FileUtils;

import java.io.File;

import javax.security.auth.callback.Callback;

public class LocalFileRepository implements IFileRepository {
    private final File file;

    public LocalFileRepository(File file) {
        this.file = file;
    }

    public void createFile(FileCallback callback) {
        FileUtils.createFile(this.file, callback);
    }


    public void deleteFile(FileCallback callback) {
        FileUtils.deleteFile(this.file, callback);
    }
    public void renameFile(String newName, FileCallback callback) {
        FileUtils.renameFile(this.file, newName, callback);
    }
    public void moveFile(File targetDir, FileCallback callback) {
        FileUtils.moveFile(this.file, targetDir, callback);
    }
    public void readFileText(ReadFileCallback callback) {
        FileUtils.readFileText(this.file, callback);
    }
    public void writeFileText(String text, WriteFileCallback callback) {
        FileUtils.writeFileText(this.file, text, callback);
    }


    @Override
    public void createFile(File file, FileCallback callback) {
        FileUtils.createFile(file, callback);
    }

    @Override
    public void deleteFile(File file, FileCallback callback) {
        FileUtils.deleteFile(file, callback);
    }

    @Override
    public void renameFile(File source, String newName, FileCallback callback) {
        FileUtils.renameFile(source, newName, callback);
    }

    @Override
    public void moveFile(File source, File targetDir, FileCallback callback) {
        FileUtils.moveFile(source, targetDir, callback);
    }

    @Override
    public void readFileText(File file, ReadFileCallback callback) {
        FileUtils.readFileText(file, callback);
    }

    @Override
    public void writeFileText(File file, String text, WriteFileCallback callback) {
        FileUtils.writeFileText(file, text, callback);
    }
}