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
import java.util.List;
import java.util.Objects;


public class SnippetsManager {

    private static SnippetsManager instance = null;
    private SnippetsManager() {}

    public static SnippetsManager getInstance() {
        if (instance == null) instance = new SnippetsManager();
        return instance;
    }

    private static final Gson gson = new Gson();
    public static final String FILENAME= "keySymbolsData.json";


    public static void addSnippet(Context context, String key, String value) {
        List<SnippetModel> list = loadList(context);
        boolean keyExists = list.stream()
                .anyMatch(item -> item.getSymbolKey().equals(key));
        if (!keyExists) {
            list.add(new SnippetModel(key, value));
            saveList(context, list);
        }
    }


    public static void removeSnippet(Context context, String snippetKey) {
        var loadedList = loadList(context);
        for (var item : loadedList) {
            if (Objects.equals(item.getSymbolKey(), snippetKey)) {
                loadedList.remove(item);
                saveList(context, loadedList);

            }
        }
    }

    public static void saveList(Context context, List<SnippetModel> models) {
        List<SerializablePair> serializableList = convertToSerializable(toPairList(models));
        String json = gson.toJson(serializableList);
        try (FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
             OutputStreamWriter writer = new OutputStreamWriter(fos)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static List<SnippetModel> loadList(Context context) {
        try (FileInputStream fis = context.openFileInput(FILENAME);
             InputStreamReader reader = new InputStreamReader(fis)) {
            Type type = new TypeToken<List<SerializablePair>>(){}.getType();
            List<SerializablePair> loaded = gson.fromJson(reader, type);
            return toModelList(convertFromSerializable(loaded));
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }




    public static void saveList(Context context, String fileName, List<SnippetModel> models) {
        List<SerializablePair> serializableList = convertToSerializable(toPairList(models));
        String json = gson.toJson(serializableList);
        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
             OutputStreamWriter writer = new OutputStreamWriter(fos)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<SnippetModel> loadList(Context context, String fileName) {
        try (FileInputStream fis = context.openFileInput(fileName);
             InputStreamReader reader = new InputStreamReader(fis)) {
            Type type = new TypeToken<List<SerializablePair>>(){}.getType();
            List<SerializablePair> loaded = gson.fromJson(reader, type);
            return toModelList(convertFromSerializable(loaded));
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }



    private static List<SerializablePair> convertToSerializable(List<Pair<String, String>> list) {
        List<SerializablePair> result = new ArrayList<>();
        for (Pair<String, String> pair : list) {
            result.add(new SerializablePair(pair.first, pair.second));
        }
        return result;
    }
    private static List<Pair<String, String>> convertFromSerializable(List<SerializablePair> list) {
        List<Pair<String, String>> result = new ArrayList<>();
        for (SerializablePair sp : list) {
            result.add(new Pair<>(sp.key, sp.value));
        }
        return result;
    }

    private static List<SnippetModel> toModelList(List<Pair<String, String>> list) {
        List<SnippetModel> models = new ArrayList<>();
        for (var pair : list) {
            models.add(new SnippetModel(pair.first, pair.second));
        }
        return models;
    }

    private static List<Pair<String, String>> toPairList(List<SnippetModel> modelList) {
        List<Pair<String, String>> pairList = new ArrayList<>();
        for (var model : modelList) {
            pairList.add(new Pair<>(model.getSymbolKey(), model.getSymbolValue()));
        }
        return pairList;
    }



}
