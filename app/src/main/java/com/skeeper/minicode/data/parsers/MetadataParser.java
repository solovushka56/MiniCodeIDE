package com.skeeper.minicode.data.parsers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.skeeper.minicode.domain.models.ProjectModel;
import com.skeeper.minicode.domain.serialization.ISerializer;

public class MetadataParser implements ISerializer<ProjectModel> {
    Gson gson = new Gson();

    @Override
    public String serialize(ProjectModel model) {
        return gson.toJson(model, ProjectModel.class);
    }

    @Override
    public ProjectModel deserialize(String json) throws JsonSyntaxException {
        return gson.fromJson(json, ProjectModel.class);
    }
}
