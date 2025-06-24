package com.skeeper.minicode.domain.usecases.project.management.metadata;

import com.skeeper.minicode.domain.contracts.repos.IProjectRepository;
import com.skeeper.minicode.domain.contracts.repos.file.IFileContentRepository;
import com.skeeper.minicode.domain.contracts.repos.file.IFileStoreRepository;
import com.skeeper.minicode.domain.exceptions.file.DomainIOException;
import com.skeeper.minicode.domain.models.ProjectModel;
import com.skeeper.minicode.domain.serialization.ISerializer;
import com.skeeper.minicode.utils.args.ProjectCreateArgs;
import com.skeeper.minicode.utils.helpers.ProjectRectColorBinding;

import java.io.IOException;

import javax.inject.Inject;

public class GenerateMetadataUseCase {
    private final IProjectRepository projectRepository;
    private final IFileStoreRepository fileStoreRepository;
    private final IFileContentRepository fileContentRepository;
    private final ISerializer<ProjectModel> serializer;

    @Inject
    public GenerateMetadataUseCase(IProjectRepository projectRepository,
                                   IFileStoreRepository fileStoreRepository,
                                   IFileContentRepository fileContentRepository,
                                   ISerializer<ProjectModel> serializer) {
        this.projectRepository = projectRepository;
        this.fileStoreRepository = fileStoreRepository;
        this.fileContentRepository = fileContentRepository;
        this.serializer = serializer;
    }

    public void execute(ProjectCreateArgs args) throws DomainIOException {
        var operations = projectRepository.getOperations();
        var config_file = operations.getProjectConfigFile(args.name());
        fileStoreRepository.saveFile(config_file.getPath());

        var projPath = operations.getProjectDir(args.name());
        var colorRect = new ProjectRectColorBinding();
        var model = new ProjectModel(args.name(),
                args.description(),
                projPath.getPath(),
                args.tags(),
                colorRect.getMainRectColor(),
                colorRect.getInnerRectColor());

        String serialized = serializer.serialize(model);
        fileContentRepository.writeFileText(
                config_file.getPath(),
                serialized);
    }


    public void execute(ProjectModel model) throws DomainIOException {
        var operations = projectRepository.getOperations();
        var config_file = operations.getProjectConfigFile(model.name());
        fileStoreRepository.saveFile(config_file.getPath());

        String serialized = serializer.serialize(model);
        fileContentRepository.writeFileText(
                config_file.getPath(),
                serialized);
    }
}
