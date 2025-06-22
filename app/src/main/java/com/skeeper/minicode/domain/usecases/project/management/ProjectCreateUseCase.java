package com.skeeper.minicode.domain.usecases.project.management;

import com.skeeper.minicode.data.repos.filerepos.FileStoreRepository;
import com.skeeper.minicode.data.repos.filerepos.LocalFileRepository;
import com.skeeper.minicode.domain.contracts.repos.IProjectRepository;
import com.skeeper.minicode.domain.enums.TemplateType;
import com.skeeper.minicode.domain.models.ProjectModel;

import org.eclipse.jgit.internal.storage.file.FileRepository;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

public class ProjectCreateUseCase {

    private final IProjectRepository projectRepository;
    private final GenerateMetadataUseCase generateMetadataUseCase;
    private final CreateTemplateUseCase createTemplateUseCase;


    public ProjectCreateUseCase(IProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
        this.generateMetadataUseCase = new GenerateMetadataUseCase(
                projectRepository.getOperations());
        createTemplateUseCase = new CreateTemplateUseCase(projectRepository);
    }

    public void execute(ProjectModel projectModel) {
        if(projectRepository.projectExists(projectModel.name()))
            return;
        projectRepository.createProject(projectModel, false);
    }


    public void execute(ProjectModel projectModel, TemplateType templateType) throws IOException {
        if(projectRepository.projectExists(projectModel.name()))
            return;
        projectRepository.createProject(projectModel, false);
        generateMetadataUseCase.execute(projectModel);
    }
}
