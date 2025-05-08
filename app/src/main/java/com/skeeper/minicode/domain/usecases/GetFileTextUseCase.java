package com.skeeper.minicode.domain.usecases;

import com.skeeper.minicode.domain.contracts.repos.IFileRepository;

public class GetFileTextUseCase {
    IFileRepository fileRepository;

    public GetFileTextUseCase(IFileRepository fileRepository) {
        this.fileRepository = fileRepository;
//        fileRepository.read
    }
    public String execute() {

        fileRepository.readFileText(fileRepository.getFile(), (text, success) -> {
            String resultText;
            if (success) {
                resultText = text;

            }
            else {
                resultText = "";
            }
        });
        return "";

    }
}
