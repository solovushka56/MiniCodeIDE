package com.skeeper.minicode.core.singleton;

import com.skeeper.minicode.domain.contracts.repos.file.IFileContentRepository;
import com.skeeper.minicode.domain.contracts.repos.file.IFileStoreRepository;
import com.skeeper.minicode.domain.exceptions.file.DomainIOException;
import com.skeeper.minicode.domain.exceptions.file.FileReadException;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class FileManager {
    IFileContentRepository fileContentRepository;
    IFileStoreRepository fileStoreRepository;

    @Inject
    public FileManager(IFileContentRepository fileContentRepository,
                       IFileStoreRepository fileStoreRepository) {
        this.fileContentRepository = fileContentRepository;
        this.fileStoreRepository = fileStoreRepository;
    }


    public void createFile(String path) throws DomainIOException {
        fileStoreRepository.createFile(path);
    }

    public void deleteFile(String path) throws DomainIOException {
        fileStoreRepository.deleteFile(path);
    }

    public void renameFile(String filePath, String newName) throws DomainIOException {
        fileStoreRepository.renameFile(filePath, newName);
    }

    public void moveFile(String sourcePath, String targetDir) throws DomainIOException {
        fileStoreRepository.moveFile(sourcePath, targetDir);
    }

    public String readFileText(String filePath) throws FileReadException {
        return fileContentRepository.readFileText(filePath);
    }


    public void writeFileText(String path, String content) throws DomainIOException {
        fileContentRepository.writeFileText(path, content);
    }

}
