package com.skeeper.minicode.data.repos;

import com.skeeper.minicode.domain.contracts.repos.IFileRepository;

import java.io.File;

public class GitFileRepository implements IFileRepository {
    @Override
    public String readFileText(File file) {
        return "";
    }

    @Override
    public void writeFileText(File file, String text) {

    }

    @Override
    public void createFile(File file) {

    }

    @Override
    public void deleteFile(File file) {

    }

    @Override
    public void renameFile(File file) {

    }

    @Override
    public void moveFile(File file, File newPath) {

    }
}
