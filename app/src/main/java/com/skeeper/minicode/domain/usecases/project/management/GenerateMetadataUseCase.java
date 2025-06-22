package com.skeeper.minicode.domain.usecases.project.management;

import com.skeeper.minicode.domain.contracts.operations.IProjectOperations;
import com.skeeper.minicode.domain.models.ProjectModel;

import java.io.IOException;

import javax.inject.Inject;

public class GenerateMetadataUseCase {
    private final IProjectOperations operations;

    @Inject
    public GenerateMetadataUseCase(IProjectOperations operations) {
        this.operations = operations;
    }

    public void execute(ProjectModel model) throws IOException {
        operations.generateMetadata(model);
    }
}
