package com.skeeper.minicode.data.repos.project;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.skeeper.minicode.R;
import com.skeeper.minicode.domain.contracts.other.providers.IResourcesProvider;
import com.skeeper.minicode.domain.contracts.repos.project.ICodeLangRepository;
import com.skeeper.minicode.domain.enums.ExtensionType;
import com.skeeper.minicode.domain.models.LangModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.inject.Inject;

public class CodeLangRepository implements ICodeLangRepository {

    private final IResourcesProvider resourcesProvider;

    @Inject
    public CodeLangRepository(IResourcesProvider resourcesProvider) {
        this.resourcesProvider = resourcesProvider;
    }

    @Override
    public LangModel getLangModel(ExtensionType extensionType) {
        var langRawRes = getLangRaw(extensionType);

        try (InputStream inputStream = resourcesProvider.getResources().openRawResource(langRawRes)) {
            InputStreamReader reader = new InputStreamReader(inputStream);
            var gson = new Gson();
            return gson.fromJson(reader, LangModel.class);
        }
        catch (JsonIOException | JsonSyntaxException | IOException e) {
            Log.e("PARSING", "Failed in CodeLangRepository");
            return null;
        }
    }


    public int getLangRaw(ExtensionType type) {
        return switch (type) {
            case JAVA -> R.raw.java_lang_syntax;
            case PYTHON -> R.raw.py_lang_syntax;
            case XML -> R.raw.xml_lang_syntax;
            case HTML ->  R.raw.html_lang_syntax;
            default -> -1;
        };
    }

}
