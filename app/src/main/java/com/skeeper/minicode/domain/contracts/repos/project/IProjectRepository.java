package com.skeeper.minicode.domain.contracts.repos.project;

import com.skeeper.minicode.domain.contracts.operations.IProjectOperations;
import com.skeeper.minicode.domain.models.ProjectModel;

import java.util.List;

public interface IProjectRepository {
    IProjectOperations getOperations();
    List<ProjectModel> loadAllProjects();
    ProjectModel loadProject(String projectName);
    boolean createProject(ProjectModel model, boolean overwrite);
    boolean deleteProject(String projectName);
    boolean renameProject(String oldName, String newName);
    boolean projectExists(String projectName);

}