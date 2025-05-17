package com.skeeper.minicode.domain.usecases;

import com.skeeper.minicode.domain.contracts.repos.IFileRepository;

public class SaveKeySymbolsUseCase {
    IFileRepository fileRepository;

    public SaveKeySymbolsUseCase(IFileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public void execute() {

    }
}
