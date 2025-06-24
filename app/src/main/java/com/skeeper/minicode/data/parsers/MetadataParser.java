package com.skeeper.minicode.data.parsers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.skeeper.minicode.domain.models.ProjectModel;
import com.skeeper.minicode.domain.serialization.ISerializer;

import javax.inject.Inject;

public class MetadataParser implements ISerializer<ProjectModel> {
    private final Gson gson;

    public MetadataParser(Gson gson) {
        this.gson = gson;
    }

    @Inject

    @Override
    public String serialize(ProjectModel model) {
        return gson.toJson(model, ProjectModel.class);
    }

    @Override
    public ProjectModel deserialize(String json) throws JsonSyntaxException {
        return gson.fromJson(json, ProjectModel.class);
    }
}
