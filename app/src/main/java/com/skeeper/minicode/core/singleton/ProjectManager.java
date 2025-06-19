package com.skeeper.minicode.core.singleton;


import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.skeeper.minicode.core.constants.ProjectConstants;
import com.skeeper.minicode.data.repos.ProjectRepository;
import com.skeeper.minicode.domain.contracts.other.providers.IFileDirectoryProvider;
import com.skeeper.minicode.data.models.ProjectModelParcelable;
import com.skeeper.minicode.domain.contracts.repos.IProjectRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ProjectManager {
    private final IProjectRepository repository;
    private final IFileDirectoryProvider fileDirectoryProvider;


    @Inject
    public ProjectManager(IProjectRepository repository,
                          IFileDirectoryProvider fileDirectoryProvider) {
        this.repository = repository;
        this.fileDirectoryProvider = fileDirectoryProvider;
    }

    public List<ProjectModelParcelable> loadAllProjectModels() {
        return repository.loadAllProjects();
    }

    public ProjectModelParcelable loadProjectModel(String projectName) {
        return repository.loadProject(projectName);
    }

    public boolean createProject(ProjectModelParcelable model, boolean overwrite) {
        return repository.createProject(model, overwrite);
    }

    public boolean deleteProject(String projectName) {
        return repository.deleteProject(projectName);
    }

    public boolean renameProject(String oldName, String newName) {
        return repository.renameProject(oldName, newName);
    }

    public boolean projectExists(String projectName) {
        return repository.projectExists(projectName);
    }

    public File getProjectsStoreFolder() {
        File folder = new File(fileDirectoryProvider.getFilesDir(),
                ProjectConstants.PROJECTS_STORE_FOLDER);
        if (!folder.exists()) {
            boolean success = folder.mkdirs();
        }
        return folder;
    }

    public File getProjectDir(String projectName) {
        return new File(getProjectsStoreFolder(), projectName);
    }

    public void saveFile(File projDir, String fileName, String content) throws IOException {
        repository.getOperations().saveFile(projDir, fileName, content);
    }

    public void generateMetadata(File projDir, ProjectModelParcelable model) throws IOException {
        repository.getOperations().generateMetadata(projDir, model);
    }

}