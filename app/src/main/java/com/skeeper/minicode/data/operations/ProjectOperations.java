package com.skeeper.minicode.data.operations;

import static com.skeeper.minicode.core.constants.ProjectConstants.IDE_PROJECT_CONFIG_FILENAME;
import static com.skeeper.minicode.core.constants.ProjectConstants.METADATA_DIR_NAME;

import com.google.gson.Gson;
import com.skeeper.minicode.core.constants.ProjectConstants;
import com.skeeper.minicode.data.parsers.MetadataParser;
import com.skeeper.minicode.domain.contracts.operations.IProjectOperations;
import com.skeeper.minicode.domain.contracts.other.providers.IFileDirectoryProvider;
import com.skeeper.minicode.domain.models.ProjectModel;
import com.skeeper.minicode.utils.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.inject.Inject;

public class ProjectOperations implements IProjectOperations {

    private final IFileDirectoryProvider fileDirProvider;
    private final Gson gson;

    @Inject
    public ProjectOperations(IFileDirectoryProvider fileDirProvider, Gson gson) {
        this.fileDirProvider = fileDirProvider;
        this.gson = gson;
    }

    @Override
    public File getProjectsStoreFolder() {
        File folder = new File(fileDirProvider.getFilesDir(),
                ProjectConstants.PROJECTS_STORE_FOLDER);
        if (!folder.exists()) folder.mkdirs();
        return folder;
    }

    @Override
    public File getProjectDir(String projectName) {
        return new File(getProjectsStoreFolder(), projectName);
    }

    @Override
    public File getProjectConfigFile(String projectName) {
        File metadataDir = new File(getProjectDir(projectName),
                ProjectConstants.METADATA_DIR_NAME);
        return new File(metadataDir, ProjectConstants.IDE_PROJECT_CONFIG_FILENAME);
    }

    @Override
    public boolean createDirectory(File dir) {
        return dir.mkdirs();
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
    public boolean rename(File source, File target) {
        return source.renameTo(target);
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

    public boolean renameProject(String oldName, String newName) { // todo model.mainFilePath()
        File oldDir = getProjectDir(oldName);
        File newDir = getProjectDir(newName);

        if (oldDir.exists() && !newDir.exists()) {
            return oldDir.renameTo(newDir);
        }

        File metadata = getMetadataDir(oldName);
        File projectConfig = getProjectConfig(oldName);
        if (!metadata.exists() || !projectConfig.exists()) return false;

        var parser = new MetadataParser(gson);
        try {
            ProjectModel model = parser.deserialize(readFile(metadata));
            var renamedModel = new ProjectModel(newName,
                    model.description(),
                    model.path(),
                    model.tags(),
                    model.mainRectColorHex(),
                    model.innerRectColorHex(),
                    model.mainFilePath());
            String json = parser.serialize(model);
            FileUtils.writeFileText(getProjectConfig(oldName), json);
        }
        catch (IOException e) {
            return false;
        }

        return true;
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
    public boolean exists(File file) {
        return file.exists();
    }

    @Override
    public File[] listChildDirectories(File dir) {
        return dir.listFiles(File::isDirectory);
    }

    @Override
    public void generateMetadata(ProjectModel model) throws IOException {
        File projectDir = new File(model.path());
        File ideFilesPath = new File(projectDir, METADATA_DIR_NAME);
        if (!ideFilesPath.exists()) ideFilesPath.mkdirs();
        String content = gson.toJson(model, ProjectModel.class);
        saveFile(ideFilesPath, IDE_PROJECT_CONFIG_FILENAME, content);
    }

    public File getMetadataDir(String projectName) {
        return new File(getProjectDir(projectName), METADATA_DIR_NAME);
    }


    public File getProjectConfig(String projectName) {
        return new File(getMetadataDir(projectName),IDE_PROJECT_CONFIG_FILENAME);
    }
}
