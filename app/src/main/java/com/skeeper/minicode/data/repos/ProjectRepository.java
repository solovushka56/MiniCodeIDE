package com.skeeper.minicode.data.repos;


import com.google.gson.Gson;
import com.skeeper.minicode.data.models.ProjectModelParcelable;
import com.skeeper.minicode.domain.contracts.operations.IProjectOperations;
import com.skeeper.minicode.domain.contracts.repos.IProjectRepository;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class ProjectRepository implements IProjectRepository {

    private final IProjectOperations operations;
    private final Gson gson;


    @Inject
    public ProjectRepository(IProjectOperations operations, Gson gson) {
        this.operations = operations;
        this.gson = gson;
    }


    @Override
    public IProjectOperations getOperations() {
        return operations;
    }

    @Override
    public List<ProjectModelParcelable> loadAllProjects() {
        File[] projectDirs = operations.listChildDirectories(operations.getProjectsStoreFolder());
        if (projectDirs == null) return List.of();

        return Arrays.stream(projectDirs)
                .map(dir -> loadProject(dir.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public ProjectModelParcelable loadProject(String projectName) {
        try {
            File configFile = operations.getProjectConfigFile(projectName);
            String json = operations.readFile(configFile);
            return gson.fromJson(json, ProjectModelParcelable.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load project: " + projectName, e);
        }
    }

    public boolean createProject(ProjectModelParcelable model, boolean overwrite) {
        File projectDir = operations.getProjectDir(model.getProjectName());
        if (projectDir.exists()) {
            if (overwrite) {
                if (!operations.deleteRecursive(projectDir)) {
                    return false;
                }
            }
            else {
                return false;
            }
        }
        if (!projectDir.mkdirs()) return false;
        try {
            operations.generateMetadata(projectDir, model);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }


    @Override
    public boolean deleteProject(String projectName) {
        File projectDir = operations.getProjectDir(projectName);
        return operations.deleteRecursive(projectDir);
    }

    @Override
    public boolean renameProject(String oldName, String newName) {
        File oldDir = operations.getProjectDir(oldName);
        File newDir = operations.getProjectDir(newName);
        return operations.exists(oldDir) && !operations.exists(newDir) && operations.rename(oldDir, newDir);
    }

    @Override
    public boolean projectExists(String projectName) {
        return operations.exists(operations.getProjectDir(projectName));
    }

}
