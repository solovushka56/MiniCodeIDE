package com.skeeper.minicode.domain.contracts.repos;

import com.skeeper.minicode.domain.contracts.other.callbacks.FileCallback;
import com.skeeper.minicode.domain.contracts.other.callbacks.ReadFileCallback;
import com.skeeper.minicode.domain.contracts.other.callbacks.WriteFileCallback;

import java.io.File;

public interface IFileRepository {
    File getFile();
    void createFile(File file);
    void deleteFile(File file);
    void renameFile(File source, String newName);
    void moveFile(File source, File targetDir);
    String readFileText(File file);
    void writeFileText(File file, String text);
}
