package com.skeeper.minicode.utils.helpers;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Locale;

public final class EditorLangResolver {

    public static final String SCOPE_PLAIN_TEXT = null;
    public static final String SCOPE_JAVA = "source.java";
    public static final String SCOPE_PYTHON = "source.python";
    public static final String SCOPE_XML = "text.xml";
    public static final String SCOPE_HTML = "text.html.basic";



    @Nullable
    public static String resolveScopeByFileName(@Nullable String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return SCOPE_PLAIN_TEXT;
        }

        String lower = fileName.toLowerCase(Locale.ROOT);

        if (lower.endsWith(".java")) return SCOPE_JAVA;
        if (lower.endsWith(".py")) return SCOPE_PYTHON;
        if (lower.endsWith(".xml")) return SCOPE_XML;
        if (lower.endsWith(".html")) return SCOPE_HTML;
        if (lower.endsWith(".htm")) return SCOPE_HTML;

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