package com.skeeper.minicode.utils.helpers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.github.rosemoe.sora.langs.textmate.TextMateLanguage;
import io.github.rosemoe.sora.widget.CodeEditor;

public final class EditorTextMateApplier {

    private EditorTextMateApplier() {
    }

    public static void applyLanguageByFileName(
            @NonNull CodeEditor editor,
            @Nullable String fileName
    ) {
        String scopeName = EditorLangResolver.resolveScopeByFileName(fileName);

        if (!EditorLangResolver.isSupported(scopeName)) {
            editor.setEditorLanguage(null);
            return;
        }

        TextMateLanguage language = TextMateLanguage.create(
                scopeName,
                true//autocomplete
        );
        editor.setEditorLanguage(language);
    }
}