package com.skeeper.minicode.data.repos.project;


import com.google.gson.Gson;
import com.skeeper.minicode.core.constants.ProjectConstants;
import com.skeeper.minicode.data.local.FileDirectoryProvider;
import com.skeeper.minicode.domain.contracts.operations.IProjectOperations;
import com.skeeper.minicode.domain.contracts.repos.project.IProjectRepository;
import com.skeeper.minicode.domain.models.ProjectModel;
import com.skeeper.minicode.domain.serialization.ISerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class ProjectRepository implements IProjectRepository {
    private final FileDirectoryProvider fileDirProvider;
    private final Gson gson;

    @Inject
    public ProjectRepository(FileDirectoryProvider fileDirProvider,
                             Gson gson) {
        this.gson = gson;
        this.fileDirProvider = fileDirProvider;
    }


    @Override
    public File getProjectsStoreFolder() {
        File folder = new File(fileDirProvider.getFilesDir(),
                ProjectConstants.PROJECTS_STORE_FOLDER);
        if (!folder.exists()) folder.mkdirs();
        return folder;
    }

    @Override
    public List<ProjectModel> loadAllProjects() {
        File[] projectDirs = listChildDirectories(getProjectsStoreFolder());
        if (projectDirs == null) return List.of();

        return Arrays.stream(projectDirs)
                .map(dir -> loadProject(dir.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public File getProjectDir(String projectName) {
        return null;
    }

    @Override
    public File getProjectConfigFile(String projectName) {
        return null;
    }

    @Override
    public ProjectModel loadProject(String projectName) {
        try {
            File configFile = getProjectConfigFile(projectName);
            String json = readFile(configFile);
            return gson.fromJson(json, ProjectModel.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load project: " + projectName, e);
        }
    }

    public boolean createProject(ProjectModel model, boolean overwrite) {
        File projectDir = getProjectDir(model.name());
        if (projectDir.exists()) {
            if (overwrite) {
                if (!deleteRecursive(projectDir)) {
                    return false;
                }
            }
            else {
                return false;
            }
        }
        if (!projectDir.mkdirs()) return false;
        saveMetadata(model);
        return true;
    }


    @Override
    public boolean deleteProject(String projectName) {
        File projectDir = getProjectDir(projectName);
        return deleteRecursive(projectDir);
    }

    @Override
    public boolean renameProject(String oldName, String newName) {
        File oldDir = getProjectDir(oldName);
        File newDir = getProjectDir(newName);
        return oldDir.exists() &&
                !newDir.exists() &&
                oldDir.renameTo(newDir);
    }

    @Override
    public boolean projectExists(String projectName) {
        return getProjectDir(projectName).exists();
    }



    @Override
    public boolean saveMetadata(ProjectModel model) {
        return false;
    }

    @Override
    public boolean deleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory()) {
            File[] children = fileOrDirectory.listFiles();
            if (children != null) {
                for (File child : children) {
                    deleteRecursive(child);
                }
            }
        }
        return fileOrDirectory.delete();
    }


    @Override
    public String readFile(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString().trim();
    }


    @Override
    public void saveFile(File projectDir,
                         String fileName, String content) throws IOException {
        if (!projectDir.exists() && !projectDir.mkdirs()) {
            throw new IOException("failed to create proj dir");
        }

        File file = new File(projectDir, fileName);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }

    @Override
    public File[] listChildDirectories(File dir) {
        return dir.listFiles(File::isDirectory);
    }
}
