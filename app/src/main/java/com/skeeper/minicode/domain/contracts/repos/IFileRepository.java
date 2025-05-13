package com.skeeper.minicode.domain.contracts.repos;

import com.skeeper.minicode.domain.contracts.other.callbacks.FileCallback;
import com.skeeper.minicode.domain.contracts.other.callbacks.ReadFileCallback;
import com.skeeper.minicode.domain.contracts.other.callbacks.WriteFileCallback;

import java.io.File;

public interface IFileRepository {
    File getFile();
    void createFile();
    void deleteFile();
    void renameFile(String newName);
    void moveFile(File targetDir);
    String readFileText();
    void writeFileText(String text);
}
