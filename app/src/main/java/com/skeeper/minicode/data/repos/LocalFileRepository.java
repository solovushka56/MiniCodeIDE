package com.skeeper.minicode.data.repos;

import com.skeeper.minicode.domain.contracts.repos.IFileRepository;

import javax.security.auth.callback.Callback;

public class LocalFileRepository {
    private String fileExtension;
    private String fileText;


    public String loadFileText() {

        if (fileText != null) return fileText;


        return "";
    }

    public void saveFileText() {

    }

}
