package com.skeeper.minicode.singleton;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.skeeper.minicode.models.ProjectModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectManager {

    private static ProjectManager instance = null;
    private ProjectManager() {}
    public static ProjectManager getInstance() {
        if (instance == null) instance = new ProjectManager();
        return instance;
    }


    private static final String projectsStoreFolder = "projects";

    private static final String ideFilesDirectoryName = ".minicode";
    private static final String ideProjectConfigFilename = "project_config.json"; // in ideFilesDirectoryName




    public static List<ProjectModel> loadAllProjectModels(Context context) {
        var innerFiles = getProjectsStoreFolder(context).listFiles();
        if (innerFiles == null) return new ArrayList<>();

        List<String> allProjectNames = Arrays
                .stream(innerFiles)
                .filter(file -> file.isDirectory())
                .map(file -> file.getName())
                .collect(Collectors.toList());


        List<ProjectModel> models = new ArrayList<>();
        for (String projectName : allProjectNames) {
            models.add(loadProjectModel(context, projectName));
        }
        return models;
    }


    @Nullable
    public static ProjectModel loadProjectModel(Context context, String projectName) {
        File configDir = getProjectConfigDir(context, projectName);
        try {
//            FileInputStream stream = context.openFileInput(configDir.getName());
//            InputStreamReader inputReader = new InputStreamReader(stream);
//            BufferedReader bufferedReader = new BufferedReader(inputReader);
//            StringBuilder stringBuilder = new StringBuilder();
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                stringBuilder.append(line);
//            }

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






    public static boolean createProject(Context context, ProjectModel model, boolean overwrite) {
        File projectDir = getProjectDir(context, model.getProjectName());
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
            generateProjectIdeFiles(context, projectDir, model);
        }
        catch (IOException e) {
            Toast.makeText(context, "failed to generate ide files", Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }

        return true;
    }


    /// add ide configs and settings of the project to special folder
    private static void generateProjectIdeFiles(Context context, File projectDir, ProjectModel model) throws IOException {
        model.setProjectPath(projectDir.getAbsolutePath());
        File ideFilesPath = new File(projectDir, ideFilesDirectoryName);

        if (!ideFilesPath.exists()) ideFilesPath.mkdirs();

        Gson gson = new Gson();
        String content = gson.toJson(model);

        saveFile(context, ideFilesPath, ideProjectConfigFilename, content);
    }




    public static boolean deleteProject(Context context, String projectName) {
        File projectDir = getProjectDir(context, projectName);
        return deleteRecursive(projectDir);
    }

    public static boolean renameProject(Context context, String oldName, String newName) {
        File oldDir = getProjectDir(context, oldName);
        File newDir = getProjectDir(context, newName);

        if (oldDir.exists() && !newDir.exists()) {
            return oldDir.renameTo(newDir);
        }
        return false;
    }




    public static void saveFile(Context context, String projectName,
                                String fileName, String content) throws IOException {
        File projectDir = getProjectDir(context, projectName);
        if (!projectDir.exists() && !projectDir.mkdirs()) {
            throw new IOException("failed to create proj dir");
        }
        File file = new File(projectDir, fileName);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }
    public static void saveFile(Context context, File projectDir,
                                String fileName, String content) throws IOException {
        if (!projectDir.exists() && !projectDir.mkdirs()) {
            throw new IOException("failed to create proj dir");
        }

        File file = new File(projectDir, fileName);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }



    public static String readFile(Context context, String projectName,
                                  String fileName) throws IOException {
        File file = new File(getProjectDir(context, projectName), fileName);
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString().trim();
    }


    public static boolean projectExists(Context context, String projectName) {
        return getProjectDir(context, projectName).exists();
    }


    private static File getProjectConfigDir(Context context, String projectName) {
        File configFolder = getProjectConfigFolder(context, projectName);
        return new File(configFolder, ideProjectConfigFilename);
    }
    private static File getProjectConfigFolder(Context context, String projectName) {
        File projectDir = getProjectDir(context, projectName);
        return new File(projectDir, ideFilesDirectoryName);
    }





    private static boolean deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }
        return fileOrDirectory.delete();
    }
    private static File getProjectsStoreFolder(Context context) {
        File folder = new File(context.getFilesDir(), projectsStoreFolder);
        if (!folder.exists()) {
            boolean success = folder.mkdirs();
        }
        return folder;
    }
    public static File getProjectDir(Context context, String projectName) {
        return new File(getProjectsStoreFolder(context), projectName);
    }


}
