package com.skeeper.minicode.presentation.viewmodels.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.skeeper.minicode.domain.enums.FileOpenMode;
import com.skeeper.minicode.domain.models.FileItem;
import com.skeeper.minicode.presentation.viewmodels.CodeEditViewModel;

public class CodeEditViewModelFactory implements ViewModelProvider.Factory {
    private final FileItem fileItem;
    private final FileOpenMode fileOpenMode;

    public CodeEditViewModelFactory(FileItem fileItem, FileOpenMode fileOpenMode) {
        this.fileItem = fileItem;
        this.fileOpenMode = fileOpenMode;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CodeEditViewModel.class)) {
            return (T) new CodeEditViewModel(fileItem, fileOpenMode);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
