package com.skeeper.minicode.domain.contracts.repos;

import com.skeeper.minicode.data.models.ProjectModelParcelable;
import com.skeeper.minicode.domain.contracts.operations.IProjectOperations;
import com.skeeper.minicode.domain.models.ProjectModel;

import java.io.File;
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