package com.skeeper.minicode.domain.usecases.project.tags;

import com.skeeper.minicode.data.mappers.ProjectMapper;
import com.skeeper.minicode.data.repos.project.ProjectRepository;
import com.skeeper.minicode.domain.contracts.repos.project.IProjectRepository;
import com.skeeper.minicode.domain.exceptions.file.DomainIOException;
import com.skeeper.minicode.domain.models.ProjectModel;
import com.skeeper.minicode.domain.serialization.ISerializer;
import com.skeeper.minicode.domain.usecases.project.management.metadata.GenerateMetadataUseCase;
import com.skeeper.minicode.domain.usecases.project.management.metadata.LoadMetadataUseCase;

import java.util.Arrays;

public class SaveTagsUseCase {

    public final LoadMetadataUseCase loadMetadataUseCase;
    public final GenerateMetadataUseCase generateMetadataUseCase;
    public final IProjectRepository projectRepository;
    public final ISerializer<ProjectModel> serializer;

    public SaveTagsUseCase(LoadMetadataUseCase loadMetadataUseCase,
                           GenerateMetadataUseCase generateMetadataUseCase,
                           IProjectRepository projectRepository,
                           ISerializer<ProjectModel> serializer) {
        this.loadMetadataUseCase = loadMetadataUseCase;
        this.generateMetadataUseCase = generateMetadataUseCase;
        this.projectRepository = projectRepository;
        this.serializer = serializer;
    }

    public void execute(String[] newTags, String projectName) throws DomainIOException {
        var model = projectRepository.loadProject(projectName);
        var newModel = new ProjectModel(model.name(),
                model.description(),
                model.path(),
                newTags,
                model.mainRectColorHex(), model.innerRectColorHex());
        generateMetadataUseCase.execute(model);
    }

}
