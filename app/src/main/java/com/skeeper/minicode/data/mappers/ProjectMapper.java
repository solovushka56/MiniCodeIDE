package com.skeeper.minicode.data.mappers;

import com.skeeper.minicode.data.models.ProjectModelParcelable;
import com.skeeper.minicode.domain.models.ProjectModel;

public final class ProjectMapper {

    public ProjectModel mapToDomain(ProjectModelParcelable dataModel) {
        return new ProjectModel(
                dataModel.getProjectName(),
                dataModel.getProjectDescription(),
                dataModel.getProjectPath(),
                dataModel.getTags(),
                dataModel.getMainRectColorHex(),
                dataModel.getInnerRectColorHex(),
                dataModel.getMainFilePath()
        );
    }

    public ProjectModelParcelable mapFromDomain(ProjectModel domainEntity) {
        return new ProjectModelParcelable(
                domainEntity.name(),
                domainEntity.description(),
                domainEntity.path(),
                domainEntity.tags(),
                domainEntity.mainRectColorHex(),
                domainEntity.innerRectColorHex(),
                domainEntity.mainFilePath()

        );
    }
}