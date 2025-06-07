package com.skeeper.minicode.domain.usecases.project;

import android.content.res.Resources;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.skeeper.minicode.data.mappers.LangSyntaxParser;
import com.skeeper.minicode.domain.contracts.other.providers.IResourcesProvider;
import com.skeeper.minicode.domain.models.LangModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.inject.Inject;

public class GetLangModelUseCase {

    private final IResourcesProvider resourcesProvider;

    @Inject
    public GetLangModelUseCase(IResourcesProvider resourcesProvider) {
        this.resourcesProvider = resourcesProvider;
    }

    public LangModel execute(int langRawRes) {
        try (InputStream inputStream = resourcesProvider.getResources().openRawResource(langRawRes)) {
            InputStreamReader reader = new InputStreamReader(inputStream);
            var gson = new Gson();
            return gson.fromJson(reader, LangModel.class);
        }
        catch (JsonIOException | JsonSyntaxException | IOException e) {
            Log.e("PARSING",
                    e + " in " +
                    GetLangModelUseCase.class.getName());
            return null;
        }

    }
}
