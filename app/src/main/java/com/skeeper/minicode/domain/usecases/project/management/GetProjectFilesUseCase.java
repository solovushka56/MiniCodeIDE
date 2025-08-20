package com.skeeper.minicode.domain.usecases.project.management;

import com.skeeper.minicode.core.singleton.ProjectManager;
import com.skeeper.minicode.data.repos.file.DirectoryRepository;
import com.skeeper.minicode.domain.contracts.repos.file.IDirectoryRepository;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

public class GetProjectFilesUseCase {
    @Inject ProjectManager projectManager;
    private final IDirectoryRepository directoryRepository;

    public GetProjectFilesUseCase(IDirectoryRepository directoryRepository) {
        this.directoryRepository = directoryRepository;
    }

    public List<File> execute(String projectName) {
        return directoryRepository
                .getAllFiles(projectManager.getProjectDir(projectName).getPath());
    }
}
