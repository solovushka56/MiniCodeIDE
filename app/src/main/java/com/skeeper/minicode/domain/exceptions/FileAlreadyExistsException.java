package com.skeeper.minicode.domain.exceptions;

public class FileAlreadyExistsException extends DomainIOException {
    public FileAlreadyExistsException(String message) {
        super(message);
    }
}
