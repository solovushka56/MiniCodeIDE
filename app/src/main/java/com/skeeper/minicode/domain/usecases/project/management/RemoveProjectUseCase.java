package com.skeeper.minicode.domain.usecases.project.management;

import com.skeeper.minicode.core.singleton.ProjectManager;
import com.skeeper.minicode.domain.exceptions.project.ProjectOperationException;

import javax.inject.Inject;

public class RemoveProjectUseCase {

    @Inject ProjectManager projectManager;

    public void execute(String projectName) {
        projectManager.deleteProject(projectName);
    }
}
