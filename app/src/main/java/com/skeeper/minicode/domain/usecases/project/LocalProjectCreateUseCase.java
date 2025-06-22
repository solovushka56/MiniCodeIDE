package com.skeeper.minicode.domain.usecases.project;

import com.skeeper.minicode.domain.contracts.repos.IProjectRepository;
import com.skeeper.minicode.domain.models.ProjectModel;

public class LocalProjectCreateUseCase {
    private final IProjectRepository projectRepository;

    public LocalProjectCreateUseCase(IProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public void execute(ProjectModel model) {
        projectRepository.createProject(model, false);
    }

}
