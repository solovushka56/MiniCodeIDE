package com.skeeper.minicode.domain.contracts.repos.file;

import com.skeeper.minicode.domain.exceptions.file.DomainIOException;

public interface IFileRepository {
    boolean exists(String path);
    void createFile(String path) throws DomainIOException;
    String readFileText(String path) throws DomainIOException;
    void writeFileText(String path, String content) throws DomainIOException;
    void deleteFile(String path) throws DomainIOException;
    void moveFile(String sourcePath, String targetPath) throws DomainIOException;
}