package com.skeeper.minicode.data.repos;

import com.skeeper.minicode.domain.contracts.repos.IProjectRepository;
import com.skeeper.minicode.domain.models.ProjectModel;
import com.skeeper.minicode.utils.FileUtils;

import java.io.File;


public class ProjectRepository implements IProjectRepository {
    public static final String IDE_METADATA_DIR_NAME = ".minicode";
    private ProjectModel project = null;

    public ProjectRepository(ProjectModel project) {
        this.project = project;
    }


    public void tryGenerateMetadata() {
        if (isMetadataExists()) return;

    }

    private boolean isMetadataExists() {
        File ideMetadataDir = new File(project.getProjectPath(), IDE_METADATA_DIR_NAME);
        return ideMetadataDir.exists();
    }

    @Override
    public ProjectModel getProject() {
        return project;
    }

    @Override
    public boolean createProject() {
        return false;
    }

    @Override
    public boolean saveProject() {
        return false;
    }

    @Override
    public boolean removeProject() {
        return false;
    }
}
