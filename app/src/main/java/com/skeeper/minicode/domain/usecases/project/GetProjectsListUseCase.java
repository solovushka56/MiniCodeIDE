package com.skeeper.minicode.domain.usecases.project;

import com.skeeper.minicode.domain.contracts.repos.project.IProjectRepository;

public class GetProjectsListUseCase {
    private final IProjectRepository projectRepository;

    public GetProjectsListUseCase(IProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public void execute() {
        projectRepository.loadAllProjects();
    }
}
