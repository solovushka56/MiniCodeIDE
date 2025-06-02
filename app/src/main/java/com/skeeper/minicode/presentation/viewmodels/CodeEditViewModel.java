package com.skeeper.minicode.presentation.viewmodels;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.skeeper.minicode.data.repos.filerepos.LocalFileRepository;
import com.skeeper.minicode.domain.contracts.repos.IFileRepository;
import com.skeeper.minicode.domain.enums.FileOpenMode;
import com.skeeper.minicode.domain.models.FileItem;
import com.skeeper.minicode.domain.models.HighlightColorModel;
import com.skeeper.minicode.domain.models.LangModel;
import com.skeeper.minicode.domain.usecases.GetFileTextUseCase;
import com.skeeper.minicode.domain.usecases.LangRegexUseCase;
import com.skeeper.minicode.utils.ExtensionUtils;

// for fragments mb

public class CodeEditViewModel extends ViewModel {

    private final MutableLiveData<FileItem> editingFile = new MutableLiveData<>();
    private final MutableLiveData<String> preloadedFileText = new MutableLiveData<>();

    private IFileRepository fileRepository; // git or local

    private LangRegexUseCase langRegexUseCase;
    private GetFileTextUseCase getFileUseCase;
    private HighlightColorModel highlightModel;

    public CodeEditViewModel(@Nullable FileItem fileItem, FileOpenMode fileOpenMode) {
        if (fileItem != null) {
            fileRepository = new LocalFileRepository(fileItem.getDirectory());
            editingFile.setValue(fileItem);
            switch (fileOpenMode) {
                case NEW:
                    fileRepository = new LocalFileRepository(fileItem.getDirectory());
                case LOCAL:
                    fileRepository = new LocalFileRepository(fileItem.getDirectory());
            }
            var extensionType = ExtensionUtils.getFileExtensionType(fileItem.getName());
        }
    }

    public void saveFile(String fileText) {
        if (editingFile == null) return;
        fileRepository.writeFileText(fileText);
    }


    public MutableLiveData<FileItem> getEditingFile() {
        return editingFile;
    }
}
