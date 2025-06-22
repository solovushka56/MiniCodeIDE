package com.skeeper.minicode.domain.usecases.project.management;

import com.skeeper.minicode.domain.contracts.operations.IProjectOperations;

import javax.inject.Inject;

public class GitProjectCloneUseCase {

    private final GenerateMetadataUseCase generateMetadataUseCase;
    private final IProjectOperations operations;


    @Inject
    public GitProjectCloneUseCase(IProjectOperations operations) {
        this.operations = operations;
        this.generateMetadataUseCase = new GenerateMetadataUseCase(operations);
    }

    public void execute(Runnable onCloneRunnable) {
        /// todo: create folder -> clone -> gen metadata
    }

}
