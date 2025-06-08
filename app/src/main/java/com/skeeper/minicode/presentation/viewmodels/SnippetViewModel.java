package com.skeeper.minicode.presentation.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.skeeper.minicode.R;
import com.skeeper.minicode.data.repos.SnippetRepository;
import com.skeeper.minicode.data.repos.filerepos.AsyncLocalFileRepository;
import com.skeeper.minicode.domain.contracts.other.providers.IFileDirectoryProvider;
import com.skeeper.minicode.domain.contracts.other.providers.IResourcesProvider;
import com.skeeper.minicode.domain.models.LangModel;
import com.skeeper.minicode.domain.models.SnippetModel;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SnippetViewModel extends ViewModel {

    private IResourcesProvider resourcesProvider;
    private IFileDirectoryProvider fileDirectoryProvider;

    private SnippetRepository snippetsFileRepository;
    private LiveData<List<SnippetModel>> snippets;


    @Inject
    public SnippetViewModel(IResourcesProvider resourcesProvider,
                            IFileDirectoryProvider fileDirectoryProvider) {

        if (!getSnippetDir().exists()) {
            try (InputStream inputStream = resourcesProvider
                    .getResources()
                    .openRawResource(R.raw.snippets_preset)) {

                InputStreamReader reader = new InputStreamReader(inputStream);
                var gson = new Gson();
                snippetsFileRepository.createFile();
//                snippetsFileRepository.writeFileText();
                //todo implement
            }
            catch (Exception e) {
                Log.e("PARSING", "json dont parse");

            }
        }
    }


    private File getSnippetDir() {
        return new File(fileDirectoryProvider.getFilesDir(), "snippetData");
    }

    public LiveData<List<SnippetModel>> getSnippets() {
        return snippets;
    }
}
