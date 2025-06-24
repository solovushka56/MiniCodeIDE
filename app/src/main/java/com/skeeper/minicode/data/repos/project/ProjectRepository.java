package com.skeeper.minicode.data.repos.project;


import com.google.gson.Gson;
import com.skeeper.minicode.domain.contracts.operations.IProjectOperations;
import com.skeeper.minicode.domain.contracts.repos.project.IProjectRepository;
import com.skeeper.minicode.domain.models.ProjectModel;
import com.skeeper.minicode.domain.serialization.ISerializer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class ProjectRepository implements IProjectRepository {

    private final ISerializer<ProjectModel> metadataParser;
    private final IProjectOperations operations;
    private final Gson gson;

    @Inject
    public ProjectRepository(IProjectOperations operations, ISerializer<ProjectModel> metadataParser, Gson gson) {
        this.operations = operations;
        this.gson = gson;
        this.metadataParser = metadataParser;

    }


    @Override
    public IProjectOperations getOperations() {
        return operations;
    }

    @Override
    public List<ProjectModel> loadAllProjects() {
        File[] projectDirs = operations.listChildDirectories(operations.getProjectsStoreFolder());
        if (projectDirs == null) return List.of();

        return Arrays.stream(projectDirs)
                .map(dir -> loadProject(dir.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public ProjectModel loadProject(String projectName) {
        try {
            File configFile = operations.getProjectConfigFile(projectName);
            String json = operations.readFile(configFile);
            return gson.fromJson(json, ProjectModel.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load project: " + projectName, e);
        }
    }

    public boolean createProject(ProjectModel model, boolean overwrite) {
        File projectDir = operations.getProjectDir(model.name());
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
            operations.generateMetadata(model);
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
        return operations.exists(oldDir) &&
                !operations.exists(newDir) &&
                operations.rename(oldDir, newDir);
    }

    @Override
    public boolean projectExists(String projectName) {
        return operations.exists(operations.getProjectDir(projectName));
    }

}
