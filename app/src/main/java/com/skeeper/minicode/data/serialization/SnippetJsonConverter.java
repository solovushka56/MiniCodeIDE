package com.skeeper.minicode.data.serialization;

import android.util.Log;

import com.google.gson.Gson;
import com.skeeper.minicode.domain.models.SnippetModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// todo add exception handling
public class SnippetJsonConverter implements ISerializer<SnippetModel> {

    private static final Logger log = LoggerFactory.getLogger(SnippetJsonConverter.class);
    Gson gson = new Gson();


    @Override
    public String serialize(SnippetModel model) {
        return gson.toJson(model, SnippetModel.class);
    }

    @Override
    public SnippetModel deserialize(String json) {
        SnippetModel result = gson.fromJson(json, SnippetModel.class);
        if (result == null) {
//            throw new Exception("Serialization exception");
        }
        return result;
    }
}
