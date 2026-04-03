package com.skeeper.minicode.utils.helpers;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Locale;

public final class EditorLangResolver {

    public static final String SCOPE_PLAIN_TEXT = null;
    public static final String SCOPE_JAVA = "source.java";
    public static final String SCOPE_PYTHON = "source.python";
    public static final String SCOPE_XML = "text.xml";
    public static final String SCOPE_CPP = "source.cpp";
    public static final String SCOPE_CS = "source.cs";
    public static final String SCOPE_JSON = "source.json";


    @Nullable
    public static String resolveScopeByFileName(@Nullable String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return SCOPE_PLAIN_TEXT;
        }
        String name = fileName.toLowerCase(Locale.ROOT);
        if (name.endsWith(".java")) return SCOPE_JAVA;
        if (name.endsWith(".py")) return SCOPE_PYTHON;
        if (name.endsWith(".xml")) return SCOPE_XML;
        if (name.endsWith(".cs")) return SCOPE_CS;
        if (name.endsWith(".cpp")) return SCOPE_CPP;
        if (name.endsWith(".json")) return SCOPE_JSON;
        return SCOPE_PLAIN_TEXT;
    }

    public static boolean isSupported(@Nullable String scopeName) {
        return scopeName != null;
    }

    @NonNull
    public static String normalizeThemeName(@Nullable String themeName) {
        if (themeName == null || themeName.trim().isEmpty()) {
            return "quietlight";
        }
        return themeName;
    }
}