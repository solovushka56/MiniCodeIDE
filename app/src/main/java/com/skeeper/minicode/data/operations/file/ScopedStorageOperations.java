package com.skeeper.minicode.data.operations.file;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.DocumentsContract;

import com.skeeper.minicode.domain.contracts.operations.IFileOperations;
import com.skeeper.minicode.domain.exceptions.DomainIOException;

// TODO: implement
public class ScopedStorageOperations implements IFileOperations {
    @Override
    public void move(String sourcePath, String targetPath) throws DomainIOException {

    }

    @Override
    public void createFile(String path) throws DomainIOException {

    }

    @Override
    public void delete(String path) throws DomainIOException {

    }

    @Override
    public String readContents(String path) throws DomainIOException {
        return "";
    }
//    private final ContentResolver resolver;
//
//    public void move(String source, String target) {
//        Uri sourceUri = Uri.parse(source);
//        DocumentsContract.moveDocument(resolver, sourceUri,
//                DocumentsContract.getParentDocumentUri(resolver, sourceUri),
//                Uri.parse(target));
//    }
}