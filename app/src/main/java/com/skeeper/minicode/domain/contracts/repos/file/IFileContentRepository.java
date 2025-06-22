package com.skeeper.minicode.domain.contracts.repos.file;

import com.skeeper.minicode.domain.exceptions.file.DomainIOException;
import com.skeeper.minicode.domain.exceptions.file.FileReadException;

public interface IFileContentRepository {
    String readFileText(String path) throws FileReadException;
    void writeFileText(String path, String content) throws DomainIOException;
}
