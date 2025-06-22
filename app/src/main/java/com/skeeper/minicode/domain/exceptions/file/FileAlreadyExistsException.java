package com.skeeper.minicode.domain.exceptions.file;

public class FileAlreadyExistsException extends DomainIOException {
    public FileAlreadyExistsException(String message) {
        super(message);
    }
}
