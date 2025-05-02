package com.skeeper.minicode.utils.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SharedPreferencesHelper {

    private static final String PREFS_NAME = "MinicodePrefsFile";

    public static void saveMap(Context context, String key, Map<String, String> map) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> set = map.keySet();
        for (String mapKey : set) {
            editor.putString(mapKey, map.get(mapKey));
        }
        editor.apply();
    }


    public static Map<String, String> loadMap(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Map<String, String> map = new HashMap<>();
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getValue() instanceof String) {
                map.put(entry.getKey(), (String) entry.getValue());
            }
        }
        return map;
    }


}