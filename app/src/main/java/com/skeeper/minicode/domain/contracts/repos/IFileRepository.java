package com.skeeper.minicode.domain.contracts.repos;

import java.io.File;

public interface IFileRepository {
    String readFileText(File file);
    void writeFileText(File file, String text);

    void createFile(File file);
    void deleteFile(File file);
    void renameFile(File file);
    void moveFile(File file, File newPath);
}
