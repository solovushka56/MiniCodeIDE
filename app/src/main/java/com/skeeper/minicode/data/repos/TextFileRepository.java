package com.skeeper.minicode.data.repos;

import com.skeeper.minicode.core.singleton.ProjectManager;
import com.skeeper.minicode.domain.contracts.repos.IFileRepository;
import com.skeeper.minicode.utils.FileUtils;

public class TextFileRepository implements IFileRepository {
    private String fileExtension;
    private String fileText;


    @Override
    public String loadFileText() {
        if (fileText != null) return fileText;


        return "";
    }

    @Override
    public void saveFileText() {

    }

}
