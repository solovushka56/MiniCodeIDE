package com.skeeper.minicode.domain.contracts.repos.project;
import com.skeeper.minicode.domain.models.ProjectModel;
import java.io.File;
import java.io.IOException;
import java.util.List;

public interface IProjectRepository {
    File getProjectsStoreFolder();
    List<ProjectModel> loadAllProjects();
    File getProjectDir(String projectName);

    File getProjectConfigFile(String projectName);
    ProjectModel loadProject(String projectName);
    boolean createProject(ProjectModel model, boolean overwrite);
    boolean deleteProject(String projectName);
    boolean renameProject(String oldName, String newName);
    boolean projectExists(String projectName);
    boolean saveMetadata(ProjectModel model);
    boolean deleteRecursive(File fileOrDirectory);
    String readFile(File file) throws IOException;
    public void saveFile(File projectDir,
                         String fileName, String content) throws IOException;

    File[] listChildDirectories(File dir);

}