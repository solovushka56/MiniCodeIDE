package com.skeeper.minicode.singleton;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.skeeper.minicode.models.ProjectModel;
import com.skeeper.minicode.services.FilesystemService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ProjectManagerSingleton {

    private static ProjectManagerSingleton instance = null;
    private ProjectManagerSingleton() {}
    public static ProjectManagerSingleton getInstance() {
        if (instance == null) instance = new ProjectManagerSingleton();
        return instance;
    }


    private static final String projectsStoreFolder = "projects";
    private static final String ideFilesDirectoryName = ".minicode";
    private static final String ideProjectConfigFilename = "project_config.json"; // in ideFilesDirectoryName

    public static File getAllProjectsFolder(Context context) {
        File folder = new File(context.getFilesDir(), projectsStoreFolder);
        if (!folder.exists()) {
            boolean success = folder.mkdirs();
        }
        return folder;
    }

    private static File getProjectDir(Context context, String projectName) {
        return new File(context.getFilesDir(), projectName);
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
        File ideFilesPath = new File(projectDir, ideFilesDirectoryName);
        if (!ideFilesPath.exists()) ideFilesPath.mkdirs();

        Gson gson = new Gson();
        String content = gson.toJson(model);

        saveFile(context, ideFilesPath, ideProjectConfigFilename, content);

//        try (FileOutputStream outputStream = context
//                .openFileOutput(ideProjectConfigFilename, Context.MODE_PRIVATE)) {
//            outputStream.write(content.getBytes());
//        }
//        catch (IOException e) { e.printStackTrace(); }


    }
//    public static boolean overwriteIdeFiles(File projectDir) {
//        File ideFilesPath = new File(projectDir, ideFilesDirectoryName);
//        boolean success;
//        if (!ideFilesPath.exists()) return false;
//
//        return true;
//    }
//

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


    private static boolean deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }
        return fileOrDirectory.delete();
    }






}
