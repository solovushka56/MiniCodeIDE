package com.skeeper.minicode.domain.contracts.repos;

import com.skeeper.minicode.domain.models.ProjectModel;

import java.util.List;

public interface IProjectRepository {
    List<ProjectModel> getAllProjects();
    ProjectModel getProject();
    boolean createProject();
    boolean saveProject();
    boolean removeProject();
}
