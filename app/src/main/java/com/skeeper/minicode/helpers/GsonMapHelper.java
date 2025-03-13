package com.skeeper.minicode.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class GsonMapHelper {
    private static final String PREFS_NAME = "GsonMapPrefs";
    private static final Gson gson = new Gson();


    public static void saveMap(Context context, String prefKey, Map<String, String> map) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = gson.toJson(map);
        prefs.edit().putString(prefKey, json).apply();
    }

    public static Map<String, String> loadMap(Context context, String prefKey) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(prefKey, "");

        if (json.isEmpty()) {
            return new HashMap<>();
        }
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        return gson.fromJson(json, type);
    }
}
