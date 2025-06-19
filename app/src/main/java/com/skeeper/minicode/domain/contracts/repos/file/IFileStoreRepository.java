package com.skeeper.minicode.domain.contracts.repos.file;

import com.skeeper.minicode.domain.exceptions.DomainIOException;

public interface IFileStoreRepository {
    void createFile(String path) throws DomainIOException;
    void deleteFile(String path) throws DomainIOException;
    void moveFile(String sourcePath, String targetPath) throws DomainIOException;

}
