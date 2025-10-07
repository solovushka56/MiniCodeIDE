package com.skeeper.minicode.domain.contracts.repos.file;

import com.skeeper.minicode.domain.exceptions.file.DomainIOException;

import java.io.File;
import java.util.List;

public interface IFileStoreRepository {
    void saveFile(String path) throws DomainIOException;
    void deleteFile(String path) throws DomainIOException;
    void moveFile(String sourcePath, String targetPath) throws DomainIOException;
    void renameFile(String filePath, String newName) throws DomainIOException;
    List<File> findFiles(File rootDirectory, String fileName, boolean ignoreCase);
}
