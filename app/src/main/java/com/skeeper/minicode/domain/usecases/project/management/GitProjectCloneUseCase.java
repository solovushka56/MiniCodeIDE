package com.skeeper.minicode.domain.usecases.project.management;

import com.skeeper.minicode.domain.contracts.operations.IProjectOperations;
import com.skeeper.minicode.domain.usecases.project.management.metadata.GenerateMetadataUseCase;

import javax.inject.Inject;

public class GitProjectCloneUseCase {

    private GenerateMetadataUseCase generateMetadataUseCase;
    private final IProjectOperations operations;


    @Inject
    public GitProjectCloneUseCase(IProjectOperations operations) {
        this.operations = operations;
    }

    public void execute(Runnable onCloneRunnable) {
        /// todo: create folder -> clone -> gen metadata
    }

}
