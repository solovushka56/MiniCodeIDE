package com.skeeper.minicode.data.repos.file;

import com.skeeper.minicode.domain.contracts.repos.file.IFileRepository;
import com.skeeper.minicode.domain.exceptions.file.DomainIOException;
import com.skeeper.minicode.domain.exceptions.file.FileCreateException;
import com.skeeper.minicode.utils.FileUtils;

import java.io.File;

public class GlobalFileRepository implements IFileRepository {
    @Override
    public boolean exists(String path) {
        return new File(path).exists();
    }

    @Override
    public void createFile(String path) throws DomainIOException {
        if(!FileUtils.createFile(new File(path)))
            throw new FileCreateException("File not created");
    }

    @Override
    public String readFileText(String path) throws DomainIOException {
        var str = FileUtils.readFileText(new File(path));
        if(!FileUtils.createFile(new File(path)))
            throw new FileCreateException("File not created");
        return str;
    }

    @Override
    public void writeFileText(String path, String content) throws DomainIOException {

    }

    @Override
    public void deleteFile(String path) throws DomainIOException {

    }

    @Override
    public void moveFile(String sourcePath, String targetPath) throws DomainIOException {

    }
}
