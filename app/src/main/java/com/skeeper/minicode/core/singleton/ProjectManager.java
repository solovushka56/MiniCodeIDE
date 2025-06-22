package com.skeeper.minicode.core.singleton;


import com.skeeper.minicode.core.constants.ProjectConstants;
import com.skeeper.minicode.domain.contracts.other.providers.IFileDirectoryProvider;
import com.skeeper.minicode.domain.contracts.repos.IProjectRepository;
import com.skeeper.minicode.domain.models.ProjectModel;

import java.io.File;
import java.io.IOException;
import java.util.List;

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

    public List<ProjectModel> loadAllProjectModels() {
        return repository.loadAllProjects();
    }

    public ProjectModel loadProjectModel(String projectName) {
        return repository.loadProject(projectName);
    }

    public boolean createProject(ProjectModel model, boolean overwrite) {
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

    public void generateMetadata(ProjectModel model) throws IOException {
        repository.getOperations().generateMetadata(model);
    }

}