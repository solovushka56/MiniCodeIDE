package com.skeeper.minicode.data.repos;

import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.skeeper.minicode.data.repos.filerepos.LocalFileRepository;
import com.skeeper.minicode.domain.models.SnippetModel;
import com.skeeper.minicode.utils.helpers.SerializablePair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;


public class SnippetRepository extends LocalFileRepository {

    private final Gson gson = new Gson();
    public static final String FILENAME = "snippetsData.json";

    @Inject
    public SnippetRepository(File storageDir) {
        super(new File(storageDir, FILENAME).getPath());
    }

    public void addSnippet(String key, String value) {
        List<SnippetModel> list = loadList();
        boolean keyExists = list.stream()
                .anyMatch(item -> item.getSymbolKey().equals(key));
        if (!keyExists) {
            list.add(new SnippetModel(key, value));
            saveList(list);
        }
    }

    public void removeSnippet(String snippetKey) {
        List<SnippetModel> loadedList = loadList();
        boolean removed = loadedList.removeIf(item ->
                Objects.equals(item.getSymbolKey(), snippetKey));
        if (removed) {
            saveList(loadedList);
        }
    }

    public void saveList(List<SnippetModel> models) {
        List<SerializablePair> serializableList = convertToSerializable(toPairList(models));
        String json = gson.toJson(serializableList);
        try (FileOutputStream fos = new FileOutputStream(getFilePath());
             OutputStreamWriter writer = new OutputStreamWriter(fos)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<SnippetModel> loadList() {
        File file = new File(getFilePath());
        if (!file.exists()) return new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader reader = new InputStreamReader(fis)) {
            Type type = new TypeToken<List<SerializablePair>>(){}.getType();
            List<SerializablePair> loaded = gson.fromJson(reader, type);
            return toModelList(convertFromSerializable(loaded));
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
            return new ArrayList<>();
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