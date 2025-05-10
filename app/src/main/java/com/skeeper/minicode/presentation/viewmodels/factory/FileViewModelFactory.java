package com.skeeper.minicode.presentation.viewmodels.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.skeeper.minicode.presentation.viewmodels.FilesViewModel;

import java.io.File;
import java.util.List;

public class FileViewModelFactory implements ViewModelProvider.Factory {
    private final File rootFile;

    public FileViewModelFactory(File rootFile) {
        this.rootFile = rootFile;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FilesViewModel.class)) {
            return (T) new FilesViewModel(rootFile);
        }
        throw new IllegalArgumentException("not file vm params");
    }
}