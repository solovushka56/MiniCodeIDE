package com.skeeper.minicode.data.parsers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.skeeper.minicode.domain.models.SnippetModel;
import com.skeeper.minicode.domain.serialization.ISerializer;


public class SnippetJsonParser implements ISerializer<SnippetModel> {
    Gson gson = new Gson();

    @Override
    public String serialize(SnippetModel model) {
        return gson.toJson(model, SnippetModel.class);
    }

    @Override
    public SnippetModel deserialize(String json) throws JsonSyntaxException {
        return gson.fromJson(json, SnippetModel.class);
    }
}
