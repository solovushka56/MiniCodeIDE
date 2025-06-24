package com.skeeper.minicode.domain.usecases.project.management;

import com.skeeper.minicode.domain.contracts.operations.IProjectOperations;
import com.skeeper.minicode.domain.contracts.repos.IProjectRepository;
import com.skeeper.minicode.domain.contracts.repos.file.IFileContentRepository;
import com.skeeper.minicode.domain.contracts.repos.file.IFileStoreRepository;
import com.skeeper.minicode.domain.exceptions.file.DomainIOException;
import com.skeeper.minicode.domain.exceptions.project.ProjectOperationException;
import com.skeeper.minicode.domain.models.ProjectModel;
import com.skeeper.minicode.domain.usecases.project.management.metadata.GenerateMetadataUseCase;
import com.skeeper.minicode.domain.usecases.project.management.metadata.LoadMetadataUseCase;

import java.io.File;

import javax.inject.Inject;

public class RenameProjectUseCase {

    private final IProjectRepository projectRepository;
    private final IFileStoreRepository fileStoreRepository;
    private final LoadMetadataUseCase loadMetadataUseCase;
    private final GenerateMetadataUseCase generateMetadataUseCase;

    @Inject
    public RenameProjectUseCase(IProjectRepository projectRepository,
                                IFileStoreRepository fileStoreRepository,
                                LoadMetadataUseCase loadMetadataUseCase,
                                GenerateMetadataUseCase generateMetadataUseCase) {
        this.projectRepository = projectRepository;
        this.fileStoreRepository = fileStoreRepository;
        this.loadMetadataUseCase = loadMetadataUseCase;
        this.generateMetadataUseCase = generateMetadataUseCase;
    }

    public void execute(String oldName, String newName)
            throws ProjectOperationException {

        if (!projectRepository.projectExists(oldName)) {
            throw new ProjectOperationException(
                    "Error: Project with editing name not exists!");
        }
        if (projectRepository.projectExists(newName)) {
            throw new ProjectOperationException(
                    "Error: Project with entered name already exists!");
        }
        var operations = projectRepository.getOperations();

        var model = loadMetadataUseCase.execute(oldName);
        var projPath = operations.getProjectDir(model.name());
        var projParent = projPath.getParentFile(); // todo to file repos
        var newPath = new File(projParent, newName).toString();

        var newModel = new ProjectModel(newName,
                model.description(),
                newPath,
                model.tags(),
                model.mainRectColorHex(),
                model.innerRectColorHex());
        try {
            generateMetadataUseCase.execute(newModel);
        }
        catch (DomainIOException e) {
            throw new ProjectOperationException(e.getMessage());
        }

        var file = projectRepository.getOperations().getProjectDir(oldName).getPath();
        try {
            fileStoreRepository.renameFile(file, newName);
        }
        catch (DomainIOException e) {
            resetChanges(oldName, newName);
            throw new ProjectOperationException(
                    "Error: Cannot rename project directory!");
        }

    }

    private void resetChanges(String oldName, String newName) {

    }
}
