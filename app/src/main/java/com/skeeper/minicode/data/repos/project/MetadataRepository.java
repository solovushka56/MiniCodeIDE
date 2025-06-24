package com.skeeper.minicode.data.repos.project;

import static com.skeeper.minicode.core.constants.ProjectConstants.IDE_PROJECT_CONFIG_FILENAME;
import static com.skeeper.minicode.core.constants.ProjectConstants.METADATA_DIR_NAME;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.skeeper.minicode.core.constants.ProjectConstants;
import com.skeeper.minicode.data.mappers.ProjectMapper;
import com.skeeper.minicode.data.parsers.MetadataParser;
import com.skeeper.minicode.data.repos.filerepos.LocalFileRepository;
import com.skeeper.minicode.domain.contracts.other.providers.IFileDirectoryProvider;
import com.skeeper.minicode.domain.contracts.repos.IMetadataRepository;
import com.skeeper.minicode.domain.models.ProjectModel;
import com.skeeper.minicode.utils.args.ProjectCreateArgs;
import com.skeeper.minicode.utils.helpers.ProjectRectColorBinding;
import com.skeeper.minicode.utils.helpers.ProjectRectColorBindings;

import org.eclipse.jgit.internal.storage.file.FileRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.inject.Inject;

public class MetadataRepository { //implements IMetadataRepository {
//    private final Gson gson;
//    private final IFileDirectoryProvider fileDirectoryProvider;
//    MetadataParser parser = new MetadataParser();
//
//    public MetadataRepository(Gson gson, IFileDirectoryProvider fileDirectoryProvider) {
//        this.gson = gson;
//        this.fileDirectoryProvider = fileDirectoryProvider;
//    }
//
//    @Override
//    public ProjectModel loadMetadata(String projectName) throws IOException, JsonSyntaxException {
//        var text = readFile(getProjectConfigFile(projectName));
//        return parser.deserialize(text);
//    }
//
//    @Override
//    public void saveMetadata(ProjectCreateArgs args) {
//        var binding = new ProjectRectColorBinding();
//
//        ProjectModel model = new ProjectModel(
//                args.name(),
//                args.description(),
//                "" ,
//                args.tags(),
//                binding.getInnerRectColor(),
//                binding.getMainRectColor());
//
//        File projectDir = new File(model.path());
//        File ideFilesPath = new File(projectDir, METADATA_DIR_NAME);
//        if (!ideFilesPath.exists()) ideFilesPath.mkdirs();
//        String content = gson.toJson(model, ProjectModel.class);
//        saveFile(ideFilesPath, IDE_PROJECT_CONFIG_FILENAME, content);
//    }
//
//
//    private String readFile(File file) throws IOException {
//        StringBuilder content = new StringBuilder();
//        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                content.append(line).append("\n");
//            }
//        }
//        return content.toString().trim();
//    }
//
//    public File getProjectConfigFile(String projectName) {
//        File metadataDir = new File(getProjectDir(projectName),
//                ProjectConstants.METADATA_DIR_NAME);
//        return new File(metadataDir, ProjectConstants.IDE_PROJECT_CONFIG_FILENAME);
//    }
//
//    public File getProjectDir(String projectName) {
//        return new File(getProjectsStoreFolder(), projectName);
//    }
//    public File getProjectsStoreFolder() {
//        File folder = new File(fileDirectoryProvider.getFilesDir(),
//                ProjectConstants.PROJECTS_STORE_FOLDER);
//        if (!folder.exists()) folder.mkdirs();
//        return folder;
//    }
}
