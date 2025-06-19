package com.skeeper.minicode.domain.contracts.operations;

import com.skeeper.minicode.domain.exceptions.DomainIOException;

public interface IFileOperations {
    void move(String sourcePath, String targetPath) throws DomainIOException;
    void createFile(String path) throws DomainIOException;
    void delete(String path) throws DomainIOException;
    String readContents(String path) throws DomainIOException;
}