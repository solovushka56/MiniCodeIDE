package com.skeeper.minicode.presentation.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.skeeper.minicode.core.singleton.SnippetsManager;
import com.skeeper.minicode.data.repos.SnippetRepository;
import com.skeeper.minicode.domain.contracts.other.providers.IFileDirectoryProvider;
import com.skeeper.minicode.domain.models.SnippetModel;
import com.skeeper.minicode.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SnippetViewModel extends ViewModel {
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    private final IFileDirectoryProvider fileDirectoryProvider;
    private SnippetRepository snippetsFileRepository;
    private MutableLiveData<List<SnippetModel>> snippets = new MutableLiveData<>();

    @Inject
    SnippetsManager snippetsManager;


    @Inject
    public SnippetViewModel(IFileDirectoryProvider fileDirectoryProvider) {
        this.fileDirectoryProvider = fileDirectoryProvider;
    }

    public void saveSnippetsFilePresetIfNotExists() {
        File keySymbolConfigFile = new File(fileDirectoryProvider.getFilesDir(), "keySymbolsData.json");
        if (!keySymbolConfigFile.exists()) {
            new Thread(() -> {
                try {
                    snippetsManager.saveList(new ArrayList<>(Arrays.asList(
                            new SnippetModel("tab", "    "),
                            new SnippetModel("{}", "{}"),
                            new SnippetModel("[]", "[]"),
                            new SnippetModel("()", "()"),
                            new SnippetModel(";", ";"),
                            new SnippetModel("pb", "public"),
                            new SnippetModel("pr", "private")
                    )));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();

        }
    }





    public LiveData<List<SnippetModel>> loadSnippetsAsync() {
        executor.execute(() -> {
            try {
                snippets.postValue(snippetsManager.loadList());
            } catch (IOException e) {
                Log.e("SNIPPET", "load failed");
            }
        });

        return snippets;
    }

    private File getSnippetDir() {
        return new File(fileDirectoryProvider.getFilesDir(), "snippetData");
    }

    public MutableLiveData<List<SnippetModel>> getSnippets() {
        return snippets;
    }
}
