package com.skeeper.minicode.domain.contracts.repos;

import com.skeeper.minicode.domain.contracts.other.callbacks.FileCallback;
import com.skeeper.minicode.domain.contracts.other.callbacks.ReadFileCallback;
import com.skeeper.minicode.domain.contracts.other.callbacks.WriteFileCallback;

import java.io.File;

public interface IFileRepository {
    void createFile(File file, FileCallback callback);
    void deleteFile(File file, FileCallback callback);
    void renameFile(File source, String newName, FileCallback callback);
    void moveFile(File source, File targetDir, FileCallback callback);

    void readFileText(File file, ReadFileCallback callback);
    void writeFileText(File file, String text, WriteFileCallback callback);
}
