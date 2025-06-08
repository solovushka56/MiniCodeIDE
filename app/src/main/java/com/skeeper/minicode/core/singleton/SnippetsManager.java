package com.skeeper.minicode.core.singleton;

import android.content.Context;
import android.util.Pair;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.skeeper.minicode.utils.helpers.SerializablePair;
import com.skeeper.minicode.domain.models.SnippetModel;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class SnippetsManager {

    private final Context context;
    private final Gson gson = new Gson();
    private static final String FILENAME = "keySymbolsData.json";


    @Inject
    public SnippetsManager(@ApplicationContext Context context) {
        this.context = context;
    }


    public void addSnippet(String key, String value) throws IOException {
        List<SnippetModel> list = loadList();
        boolean keyExists = list.stream()
                .anyMatch(item -> item.getSymbolKey().equals(key));
        if (!keyExists) {
            list.add(new SnippetModel(key, value));
            saveList(list);
        }
    }


    public void removeSnippet(String snippetKey) throws IOException {
        List<SnippetModel> loadedList = loadList();
        Iterator<SnippetModel> iterator = loadedList.iterator();
        while (iterator.hasNext()) {
            if (Objects.equals(iterator.next().getSymbolKey(), snippetKey)) {
                iterator.remove();
            }
        }
        saveList(loadedList);
    }

    public void saveList(List<SnippetModel> models) throws IOException {
        List<SerializablePair> serializableList = convertToSerializable(toPairList(models));
        String json = gson.toJson(serializableList);
        try (FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
             OutputStreamWriter writer = new OutputStreamWriter(fos)) {
            writer.write(json);
        }
    }

    public List<SnippetModel> loadList() throws IOException {
        try (FileInputStream fis = context.openFileInput(FILENAME);
             InputStreamReader reader = new InputStreamReader(fis)) {
            Type type = new TypeToken<List<SerializablePair>>(){}.getType();
            List<SerializablePair> loaded = gson.fromJson(reader, type);
            return toModelList(convertFromSerializable(loaded));
        }
    }




    public void saveList(String fileName, List<SnippetModel> models) throws IOException {
        List<SerializablePair> serializableList = convertToSerializable(toPairList(models));
        String json = gson.toJson(serializableList);
        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
             OutputStreamWriter writer = new OutputStreamWriter(fos)) {
            writer.write(json);
        }
    }

    public List<SnippetModel> loadList(String fileName) throws IOException {
        try (FileInputStream fis = context.openFileInput(fileName);
             InputStreamReader reader = new InputStreamReader(fis)) {
            Type type = new TypeToken<List<SerializablePair>>(){}.getType();
            List<SerializablePair> loaded = gson.fromJson(reader, type);
            return toModelList(convertFromSerializable(loaded));
        }
    }



    private List<SerializablePair> convertToSerializable(List<Pair<String, String>> list) {
        List<SerializablePair> result = new ArrayList<>();
        for (Pair<String, String> pair : list) {
            result.add(new SerializablePair(pair.first, pair.second));
        }
        return result;
    }
    private List<Pair<String, String>> convertFromSerializable(List<SerializablePair> list) {
        List<Pair<String, String>> result = new ArrayList<>();
        for (SerializablePair sp : list) {
            result.add(new Pair<>(sp.getKey(), sp.getValue()));
        }
        return result;
    }

    private List<SnippetModel> toModelList(List<Pair<String, String>> list) {
        List<SnippetModel> models = new ArrayList<>();
        for (var pair : list) {
            models.add(new SnippetModel(pair.first, pair.second));
        }
        return models;
    }

    private List<Pair<String, String>> toPairList(List<SnippetModel> modelList) {
        List<Pair<String, String>> pairList = new ArrayList<>();
        for (var model : modelList) {
            pairList.add(new Pair<>(model.getSymbolKey(), model.getSymbolValue()));
        }
        return pairList;
    }



}
