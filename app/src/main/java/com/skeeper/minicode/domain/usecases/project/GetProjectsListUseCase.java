package com.skeeper.minicode.domain.usecases.project;

import android.content.Context;

import com.skeeper.minicode.data.local.FileDirectoryProvider;
import com.skeeper.minicode.domain.models.ProjectModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
