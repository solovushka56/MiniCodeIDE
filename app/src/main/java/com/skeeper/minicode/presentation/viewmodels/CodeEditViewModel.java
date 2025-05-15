package com.skeeper.minicode.presentation.viewmodels;

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


    public void initVM(FileItem file,
                       FileOpenMode fileMode, LangModel langModel)
    {
        editingFile.setValue(file);
        switch (fileMode) {
            //add other
            case NEW:
                fileRepository = new LocalFileRepository(file.getDirectory());
                //fileRepository = new LocalFileRepository(File.createTempFile("ff", "txt"));
            case LOCAL:
                fileRepository = new LocalFileRepository(file.getDirectory());
        }
        var extensionType = ExtensionUtils.getFileExtensionType(file.getName());

    }


    public MutableLiveData<FileItem> getEditingFile() {
        return editingFile;
    }
}
