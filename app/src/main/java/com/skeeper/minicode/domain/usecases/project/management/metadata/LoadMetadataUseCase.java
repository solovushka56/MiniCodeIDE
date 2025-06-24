package com.skeeper.minicode.domain.usecases.project.management.metadata;

import com.skeeper.minicode.domain.contracts.repos.project.IProjectRepository;
import com.skeeper.minicode.domain.models.ProjectModel;

public class LoadMetadataUseCase {
    IProjectRepository projectRepository;

    public LoadMetadataUseCase(IProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public ProjectModel execute(String projectName) {
        return projectRepository.loadProject(projectName);
    }

}
