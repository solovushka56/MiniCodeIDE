package com.skeeper.minicode.data.parsers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skeeper.minicode.domain.serialization.ISerializer;
import java.lang.reflect.Type;

public class TParser<T> implements ISerializer<T> {

    private final Gson gson;
    private final Type type;

    public TParser(Class<T> clazz) {
        this.gson = new Gson();
        this.type = clazz;
    }

    public TParser(TypeToken<T> typeToken) {
        this.gson = new Gson();
        this.type = typeToken.getType();
    }

    @Override
    public String serialize(T model) {
        return gson.toJson(model, type);
    }

    @Override
    public T deserialize(String json) {
        return gson.fromJson(json, type);
    }
}