package com.skeeper.minicode.presentation.viewmodels;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.skeeper.minicode.data.repos.file.LocalFileRepository;
import com.skeeper.minicode.domain.enums.FileOpenMode;
import com.skeeper.minicode.domain.models.FileItem;


public class CodeEditViewModel extends ViewModel {

    private final MutableLiveData<FileItem> editingFile = new MutableLiveData<>();
    private final MutableLiveData<String> preloadedFileText = new MutableLiveData<>();

    private LocalFileRepository fileRepository; // git or local

    public CodeEditViewModel(@Nullable FileItem fileItem, FileOpenMode fileOpenMode) {
        if (fileItem != null) {
            editingFile.setValue(fileItem);
            fileRepository = new LocalFileRepository(fileItem.getDirectory().getPath());
        }

    }


    public void saveFile(String fileText) {
        if (editingFile.getValue() == null) return;
        fileRepository.writeFileText(fileText);
    }


    public MutableLiveData<FileItem> getEditingFile() {
        return editingFile;
    }
}
