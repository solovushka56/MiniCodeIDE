package com.skeeper.minicode.presentation.viewmodels;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.skeeper.minicode.data.repos.file.LocalFileRepository;
import com.skeeper.minicode.domain.enums.EditorLang;
import com.skeeper.minicode.domain.enums.FileOpenMode;
import com.skeeper.minicode.domain.models.FileItem;


public class CodeEditorViewModel extends ViewModel {

    private final MutableLiveData<FileItem> editingFile = new MutableLiveData<>();
    private final MutableLiveData<String> preloadedFileText = new MutableLiveData<>();

    private final MutableLiveData<EditorLang> lang = new MutableLiveData<>();

    private LocalFileRepository fileRepository; // git or local

    public CodeEditorViewModel(@Nullable FileItem fileItem, FileOpenMode fileOpenMode) {
        if (fileItem != null) {
            editingFile.setValue(fileItem);
            fileRepository = new LocalFileRepository(fileItem.getDirectory().getPath());
        }

    }


    public void saveFile(String fileText) {
        if (editingFile.getValue() == null) return;
        fileRepository.writeFileText(fileText);
    }

    public EditorLang resolveLang(String fileName) {
        if (fileName == null) return EditorLang.OTHER;
        String lower = fileName.toLowerCase();
        if (lower.endsWith(".java")) return EditorLang.JAVA;
        if (lower.endsWith(".py")) return EditorLang.PYTHON;
        if (lower.endsWith(".xml")) return EditorLang.XML;
        if (lower.endsWith(".html") || lower.endsWith(".htm")) return EditorLang.HTML;
        return EditorLang.OTHER;
    }


    public MutableLiveData<FileItem> getEditingFile() {
        return editingFile;
    }
}
