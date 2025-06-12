package com.skeeper.minicode.domain.usecases.project;

import com.skeeper.minicode.data.local.FileDirectoryProvider;

import java.io.File;

public class GetProjectsListUseCase {
    FileDirectoryProvider fileDirProvider;

    public GetProjectsListUseCase(FileDirectoryProvider fileDirProvider) {
        this.fileDirProvider = fileDirProvider;
    }

    public void execute() {

    }

    private File getProjectsDir() {
        return new File(fileDirProvider.getFilesDir(), "projects");
    }
}
