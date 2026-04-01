package com.skeeper.minicode.utils.helpers;

import android.content.Context;

import androidx.annotation.NonNull;

import org.eclipse.tm4e.core.registry.IThemeSource;

import io.github.rosemoe.sora.langs.textmate.TextMateColorScheme;
import io.github.rosemoe.sora.langs.textmate.registry.FileProviderRegistry;
import io.github.rosemoe.sora.langs.textmate.registry.GrammarRegistry;
import io.github.rosemoe.sora.langs.textmate.registry.ThemeRegistry;
import io.github.rosemoe.sora.langs.textmate.registry.model.ThemeModel;
import io.github.rosemoe.sora.langs.textmate.registry.provider.AssetsFileResolver;
import io.github.rosemoe.sora.widget.CodeEditor;

public final class TextMateManager {

    private static boolean initialized = false;
    private static String activeThemeName = "dark_plus";

    private TextMateManager() {
    }

    public static synchronized void init(@NonNull Context context) throws Exception {
        if (initialized) return;

        Context appContext = context.getApplicationContext();

        FileProviderRegistry.getInstance().addFileProvider(
                new AssetsFileResolver(appContext.getAssets())
        );

//        loadTheme("quietlight", false);
        loadTheme("dark_plus", true);

        ThemeRegistry.getInstance().setTheme(activeThemeName);
        GrammarRegistry.getInstance().loadGrammars("textmate/languages.json");

        initialized = true;
    }

    public static synchronized void setTheme(@NonNull String themeName) {
        activeThemeName = themeName;
        ThemeRegistry.getInstance().setTheme(themeName);
    }

    public static void applyColorScheme(@NonNull CodeEditor editor) {
        editor.setColorScheme(TextMateColorScheme.create(ThemeRegistry.getInstance()));
    }

    private static void loadTheme(@NonNull String themeName, boolean isDark) throws Exception {
        String path = "textmate/themes/" + themeName + ".json";

        ThemeModel model = new ThemeModel(
                IThemeSource.fromInputStream(
                        FileProviderRegistry.getInstance().tryGetInputStream(path),
                        path,
                        null
                ),
                themeName
        );
        model.setDark(isDark);
        ThemeRegistry.getInstance().loadTheme(model);
    }
}