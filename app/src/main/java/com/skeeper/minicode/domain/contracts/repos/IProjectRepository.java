package com.skeeper.minicode.domain.contracts.repos;

import com.skeeper.minicode.domain.models.ProjectModel;

import java.util.List;

public interface IProjectRepository {
    ProjectModel getProject();
    void createProject();
    void saveProject();
    void removeProject();
}
