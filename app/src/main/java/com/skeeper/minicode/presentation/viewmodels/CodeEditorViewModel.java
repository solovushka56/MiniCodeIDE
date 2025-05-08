package com.skeeper.minicode.presentation.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.skeeper.minicode.domain.contracts.repos.IFileRepository;
import com.skeeper.minicode.domain.models.FileItem;
import com.skeeper.minicode.domain.usecases.GetFileTextUseCase;
import com.skeeper.minicode.domain.usecases.LangRegexUseCase;

import java.io.File;

public class CodeEditorViewModel extends ViewModel {

    private MutableLiveData<FileItem> editingFile;
    private MutableLiveData<String> preloadedFileText;


    private LangRegexUseCase langRegexUseCase;
    private GetFileTextUseCase getFileUseCase;
    private IFileRepository fileRepository; // git or local

    public void onFileInit(FileItem fileItem) {
        fileRepository.readFileText(fileItem.getDirectory(), (text, success) -> {
            if (success) {
                preloadedFileText.setValue(text);
            }
            else {
                Log.e("FILE", "cant preload file in vm");
                preloadedFileText.setValue("");
            }
        });
    }

    public MutableLiveData<FileItem> getEditingFile() {
        return editingFile;
    }

    public MutableLiveData<String> getPreloadedFileText() {
        return preloadedFileText;
    }


}
