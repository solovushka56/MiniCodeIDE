package com.skeeper.minicode.data.mappers;

import com.google.gson.Gson;
import com.skeeper.minicode.data.repos.filerepos.LocalFileRepository;
import com.skeeper.minicode.domain.models.ProjectModel;

import java.io.File;

public class ProjectJsonMapper {
    LocalFileRepository jsonFileRepository;

    public ProjectJsonMapper(LocalFileRepository jsonFileRepository) {
        this.jsonFileRepository = jsonFileRepository;
    }

    public ProjectModel getProject() {
        Gson gson = new Gson();
        return gson.fromJson(
                jsonFileRepository.readFileText(),
                ProjectModel.class);
    }

    public void saveProject(ProjectModel projectModel, File saveDirectory) {
        Gson gson = new Gson();
        jsonFileRepository.writeFileText(
                gson.toJson(projectModel, ProjectModel.class));
    }

}
