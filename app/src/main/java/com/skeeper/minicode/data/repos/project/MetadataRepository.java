package com.skeeper.minicode.data.repos.project;

import static com.skeeper.minicode.core.constants.ProjectConstants.IDE_PROJECT_CONFIG_FILENAME;
import static com.skeeper.minicode.core.constants.ProjectConstants.METADATA_DIR_NAME;

import com.google.gson.Gson;
import com.skeeper.minicode.data.mappers.ProjectMapper;
import com.skeeper.minicode.data.parsers.MetadataParser;
import com.skeeper.minicode.data.repos.filerepos.LocalFileRepository;
import com.skeeper.minicode.domain.contracts.other.providers.IFileDirectoryProvider;
import com.skeeper.minicode.domain.models.ProjectModel;

import org.eclipse.jgit.internal.storage.file.FileRepository;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

public class MetadataRepository { // todo

//    private final IFileDirectoryProvider fileDirProvider;
//    private final Gson gson;
//
//    public MetadataRepository(IFileDirectoryProvider fileDirProvider, Gson gson) {
//        this.fileDirProvider = fileDirProvider;
//        this.gson = gson;
//    }
//
//    public ProjectModel loadProjectMetadata(String projectName) {
//
//    }
//    public void saveProjectMetadata(String projectName) {
//
//    }
//    public void generateProjectMetadata(File projectDir, ProjectModel model) throws IOException {
//        var dataModel = new ProjectMapper().mapFromDomain(model);
//        dataModel.setProjectPath(projectDir.getAbsolutePath());
//        File ideFilesPath = new File(projectDir, METADATA_DIR_NAME);
//
//        if (!ideFilesPath.exists()) ideFilesPath.mkdirs();
//        String content = gson.toJson(model);
//
//        saveFile(ideFilesPath, IDE_PROJECT_CONFIG_FILENAME, content);
//    }
//
//    private boolean isMetadataValid() {
//
//    }
//
//    private File getMetadataFolder() {
//        return
//    }

}
