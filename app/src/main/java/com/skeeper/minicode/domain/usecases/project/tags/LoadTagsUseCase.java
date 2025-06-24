package com.skeeper.minicode.domain.usecases.project.tags;

import com.skeeper.minicode.domain.usecases.project.management.metadata.LoadMetadataUseCase;

import java.util.Arrays;
import java.util.List;

public class LoadTagsUseCase {

    private final LoadMetadataUseCase loadMetadataUseCase;

    public LoadTagsUseCase(LoadMetadataUseCase loadMetadataUseCase) {
        this.loadMetadataUseCase = loadMetadataUseCase;
    }

    public List<String> execute(String projectName) {
        var model = loadMetadataUseCase.execute(projectName);
        return Arrays.asList(model.tags());
    }
}
