package com.skeeper.minicode.domain.contracts.repos;

import com.skeeper.minicode.domain.models.ProjectModel;
import com.skeeper.minicode.utils.args.ProjectCreateArgs;

import java.io.IOException;

public interface IMetadataRepository {
    ProjectModel loadMetadata(String projectName) throws IOException;
    void saveMetadata(ProjectCreateArgs args);
}
