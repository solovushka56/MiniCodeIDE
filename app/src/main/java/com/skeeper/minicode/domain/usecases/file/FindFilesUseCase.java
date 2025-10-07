package com.skeeper.minicode.domain.usecases.file;

import com.skeeper.minicode.domain.contracts.repos.file.IFileStoreRepository;

import java.io.File;

public class FindFilesUseCase {
    private final IFileStoreRepository fileStoreRepository;

    public FindFilesUseCase(IFileStoreRepository fileStoreRepository) {
        this.fileStoreRepository = fileStoreRepository;
    }

    public void execute(File rootDirectory, String fileName, boolean ignoreCase) {
        fileStoreRepository.findFiles(rootDirectory, fileName, ignoreCase);
    }
}
