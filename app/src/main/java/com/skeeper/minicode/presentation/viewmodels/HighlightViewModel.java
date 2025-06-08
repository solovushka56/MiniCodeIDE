package com.skeeper.minicode.presentation.viewmodels;


/// for syntax highlight and work with editing file syntax
import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.skeeper.minicode.domain.contracts.other.providers.IResourcesProvider;
import com.skeeper.minicode.domain.enums.ExtensionType;
import com.skeeper.minicode.domain.usecases.project.syntax.GetLangRegexMapUseCase;
import com.skeeper.minicode.domain.usecases.file.GetExtensionUseCase;
import com.skeeper.minicode.domain.usecases.project.syntax.GetLangModelUseCase;
import com.skeeper.minicode.domain.usecases.project.syntax.GetLangRawUseCase;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HighlightViewModel extends ViewModel {

    private final IResourcesProvider resourcesProvider;
    private File editingFile;

    private Map<ExtensionType, Map<Pattern, Integer>>
            langMap = new HashMap<>(); /// to cache

    private final MutableLiveData<Map<Pattern, Integer>>
            currentRegexMapData = new MutableLiveData<>();

    @Inject
    public HighlightViewModel(IResourcesProvider resourcesProvider) {
        this.resourcesProvider = resourcesProvider;
    }


    public void initHighlightTo(File editingFile) {
        this.editingFile = editingFile;

        ExtensionType langType = new GetExtensionUseCase().execute(editingFile);

        if (langType == ExtensionType.OTHER) return;

        int langRaw = new GetLangRawUseCase().execute(langType);
        var langModel = new GetLangModelUseCase(resourcesProvider).execute(langRaw);

        // todo handle case where we have cached maps
        var map = new GetLangRegexMapUseCase(langModel).execute();
        currentRegexMapData.setValue(map);
    }



    public MutableLiveData<Map<Pattern, Integer>> getCurrentRegexMapData() {
        return currentRegexMapData;
    }
}
