package com.skeeper.minicode.core.singleton;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.skeeper.minicode.domain.contracts.other.IFileDirectoryProvider;
import com.skeeper.minicode.domain.models.ProjectModel;

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

    public final IFileDirectoryProvider fileDirProvider;


    @Inject
    public ProjectManager(IFileDirectoryProvider fileDirectoryProvider) {
        this.fileDirProvider = fileDirectoryProvider;
    }

    private static final String projectsStoreFolder = "projects";

    private static final String ideFilesDirectoryName = ".minicode";
    private static final String ideProjectConfigFilename = "project_config.json"; // in ideFilesDirectoryName


    public List<ProjectModel> loadAllProjectModels() {
        var innerFiles = getProjectsStoreFolder().listFiles();
        if (innerFiles == null) return new ArrayList<>();

        List<String> allProjectNames = Arrays
                .stream(innerFiles)
                .filter(file -> file.isDirectory())
                .map(file -> file.getName())
                .collect(Collectors.toList());


        List<ProjectModel> models = new ArrayList<>();
        for (String projectName : allProjectNames) {
            models.add(loadProjectModel(projectName));
        }
        return models;
    }


    @Nullable
    public ProjectModel loadProjectModel(String projectName) {
        File configDir = getProjectConfigDir(projectName);
        try {
            StringBuilder content = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new FileReader(configDir))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            }

            return new Gson().fromJson(content.toString().trim(), ProjectModel.class);
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }






    public boolean createProject(ProjectModel model, boolean overwrite) {
        File projectDir = getProjectDir(model.getProjectName());
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
        try {
            generateProjectIdeFiles(projectDir, model);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }


    /// add ide configs and settings of the project to special folder
    public void generateProjectIdeFiles(File projectDir, ProjectModel model) throws IOException {
        model.setProjectPath(projectDir.getAbsolutePath());
        File ideFilesPath = new File(projectDir, ideFilesDirectoryName);

        if (!ideFilesPath.exists()) ideFilesPath.mkdirs();

        Gson gson = new Gson();
        String content = gson.toJson(model);

        saveFile(ideFilesPath, ideProjectConfigFilename, content);
    }




    public boolean deleteProject(String projectName) {
        File projectDir = getProjectDir(projectName);
        return deleteRecursive(projectDir);
    }

    public boolean renameProject(String oldName, String newName) {
        File oldDir = getProjectDir(oldName);
        File newDir = getProjectDir(newName);

        if (oldDir.exists() && !newDir.exists()) {
            return oldDir.renameTo(newDir);
        }
        return false;
    }


    public void saveFile(String projectName,
                                String fileName, String content) throws IOException {
        File projectDir = getProjectDir(projectName);
        if (!projectDir.exists() && !projectDir.mkdirs()) {
            throw new IOException("failed to create proj dir");
        }
        File file = new File(projectDir, fileName);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }
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



    public boolean projectExists(String projectName) {
        return getProjectDir(projectName).exists();
    }


    private File getProjectConfigDir(String projectName) {
        File configFolder = getProjectConfigFolder(projectName);
        return new File(configFolder, ideProjectConfigFilename);
    }
    private File getProjectConfigFolder(String projectName) {
        File projectDir = getProjectDir(projectName);
        return new File(projectDir, ideFilesDirectoryName);
    }





    private boolean deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }
        return fileOrDirectory.delete();
    }
    public File getProjectsStoreFolder() {

        File folder = new File(fileDirProvider.getFilesDir(), projectsStoreFolder);
        if (!folder.exists()) {
            boolean success = folder.mkdirs();
        }
        return folder;
    }
    public File getProjectDir(String projectName) {
        return new File(getProjectsStoreFolder(), projectName);
    }


}
