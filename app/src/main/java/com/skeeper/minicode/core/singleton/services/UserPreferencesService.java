package com.skeeper.minicode.core.singleton.services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.HashSet;
import java.util.Set;

public class UserPreferencesService {

    private static final String PREF_NAME = "user_settings";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private UserPreferencesService() {}


    @SuppressLint("CommitPrefEdits")
    public static void init(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getApplicationContext()
                    .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
    }

    private static void checkInitialization() {
        if (sharedPreferences == null || editor == null) {
            throw new IllegalStateException("not init");
        }
    }


    public static void setString(String key, String value) {
        checkInitialization();
        if (!TextUtils.isEmpty(key)) {
            editor.putString(key, value);
            editor.apply();
        }
    }
    public static String getString(String key, String defaultValue) {
        checkInitialization();
        return sharedPreferences.getString(key, defaultValue);
    }




    public static void setInt(String key, int value) {
        checkInitialization();
        editor.putInt(key, value);
        editor.apply();
    }
    public static int getInt(String key, int defaultValue) {
        checkInitialization();
        return sharedPreferences.getInt(key, defaultValue);
    }


    public static void setBoolean(String key, boolean value) {
        checkInitialization();
        editor.putBoolean(key, value);
        editor.apply();
    }
    public static boolean getBoolean(String key, boolean defaultValue) {
        checkInitialization();
        return sharedPreferences.getBoolean(key, defaultValue);
    }

// delete setting
    public static void remove(String key) {
        checkInitialization();
        editor.remove(key);
        editor.apply();
    }

//delete all settings
    public static void clearAll() {
        checkInitialization();
        editor.clear();
        editor.apply();
    }


    public static boolean contains(String key) {
        checkInitialization();
        return sharedPreferences.contains(key);
    }



    public static void setStringSet(String key, Set<String> value) {
        checkInitialization();
        if (!TextUtils.isEmpty(key)) {
            editor.putStringSet(key, value);
            editor.apply();
        }
    }

    public static Set<String> getStringSet(String key, Set<String> defaultValue) {
        checkInitialization();
        Set<String> storedSet = sharedPreferences.getStringSet(key, defaultValue);
        return storedSet != null ? new HashSet<>(storedSet) : defaultValue;
    }
}
