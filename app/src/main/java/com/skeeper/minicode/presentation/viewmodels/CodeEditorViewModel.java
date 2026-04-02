package com.skeeper.minicode.presentation.viewmodels;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.skeeper.minicode.data.repos.file.LocalFileRepository;
import com.skeeper.minicode.domain.enums.EditorLang;
import com.skeeper.minicode.domain.enums.FileOpenMode;
import com.skeeper.minicode.domain.models.FileItem;
import com.skeeper.minicode.utils.FileUtils;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CodeEditorViewModel extends ViewModel {
    private ExecutorService executor = Executors.newSingleThreadExecutor();


    private MutableLiveData<String> fileText = new MutableLiveData<>();

    @Inject
    public CodeEditorViewModel() {
    }

    public void loadFileText(FileItem fileItem) {
        executor.execute(() -> {
            var result = FileUtils.readFileText(fileItem.getDirectory());
            fileText.postValue(result);
        });
    }
    public MutableLiveData<String> getFileText() {
        return fileText;
    }

}
