package com.skeeper.minicode.core.singleton;

import com.skeeper.minicode.domain.contracts.repos.file.IFileRepository;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class FileManager {
    IFileRepository fileRepository;

    @Inject
    public FileManager(IFileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }


    public File getFile() {
        return null;
    }

    public void createFile() {

    }

    public void deleteFile() {

    }

    public void renameFile(String newName) {

    }

    public void moveFile(File targetDir) {

    }

    public String readFileText(File file) {
        return "";
    }


    public void writeFileText(String text) {

    }

}
