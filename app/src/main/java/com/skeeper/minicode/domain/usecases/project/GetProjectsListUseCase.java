package com.skeeper.minicode.domain.usecases.project;

import com.skeeper.minicode.data.local.FileDirectoryProvider;
import com.skeeper.minicode.domain.contracts.other.providers.IFileDirectoryProvider;

import java.io.File;

public class GetProjectsListUseCase {
    IFileDirectoryProvider fileDirProvider;

    public GetProjectsListUseCase(IFileDirectoryProvider fileDirProvider) {
        this.fileDirProvider = fileDirProvider;
    }

    public void execute() {

    }

    private File getProjectsDir() {
        return new File(fileDirProvider.getFilesDir(), "projects");
    }
}
