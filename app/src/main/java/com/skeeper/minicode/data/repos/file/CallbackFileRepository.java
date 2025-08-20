package com.skeeper.minicode.data.repos.file;

import com.skeeper.minicode.domain.contracts.other.callbacks.FileCallback;
import com.skeeper.minicode.domain.contracts.other.callbacks.ReadFileCallback;
import com.skeeper.minicode.domain.contracts.other.callbacks.WriteFileCallback;
import com.skeeper.minicode.domain.contracts.repos.ICallbackFileRepository;
import com.skeeper.minicode.utils.CallbackFileUtils;

import java.io.File;

public class CallbackFileRepository implements ICallbackFileRepository {
    private final File file;

    public CallbackFileRepository(File file) {
        this.file = file;
    }

    public void createFile(FileCallback callback) {
        CallbackFileUtils.createFile(this.file, callback);
    }


    public void deleteFile(FileCallback callback) {
        CallbackFileUtils.deleteFile(this.file, callback);
    }
    public void renameFile(String newName, FileCallback callback) {
        CallbackFileUtils.renameFile(this.file, newName, callback);
    }
    public void moveFile(File targetDir, FileCallback callback) {
        CallbackFileUtils.moveFile(this.file, targetDir, callback);
    }
    public void readFileText(ReadFileCallback callback) {
        CallbackFileUtils.readFileText(this.file, callback);
    }
    public void writeFileText(String text, WriteFileCallback callback) {
        CallbackFileUtils.writeFileText(this.file, text, callback);
    }


    @Override
    public File getFile() {
        return file;
    }

    @Override
    public void createFile(File file, FileCallback callback) {
        CallbackFileUtils.createFile(file, callback);
    }

    @Override
    public void deleteFile(File file, FileCallback callback) {
        CallbackFileUtils.deleteFile(file, callback);
    }

    @Override
    public void renameFile(File source, String newName, FileCallback callback) {
        CallbackFileUtils.renameFile(source, newName, callback);
    }

    @Override
    public void moveFile(File source, File targetDir, FileCallback callback) {
        CallbackFileUtils.moveFile(source, targetDir, callback);
    }

    @Override
    public void readFileText(File file, ReadFileCallback callback) {
        CallbackFileUtils.readFileText(file, callback);
    }

    @Override
    public void writeFileText(File file, String text, WriteFileCallback callback) {
        CallbackFileUtils.writeFileText(file, text, callback);
    }
}