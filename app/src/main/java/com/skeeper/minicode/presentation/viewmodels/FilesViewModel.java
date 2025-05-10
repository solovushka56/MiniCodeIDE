package com.skeeper.minicode.presentation.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.skeeper.minicode.core.singleton.ProjectManager;
import com.skeeper.minicode.data.mappers.FileTreeMapper;
import com.skeeper.minicode.domain.contracts.repos.IFileRepository;
import com.skeeper.minicode.domain.models.FileItem;
import com.skeeper.minicode.domain.models.ProjectModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FilesViewModel extends ViewModel {

    private File rootDirectory;



    private final FileTreeMapper fileTreeMapper = new FileTreeMapper();
    private final MutableLiveData<List<FileItem>> fileRepositoryList = new MutableLiveData<>();
    private final MutableLiveData<FileItem> selectedFile = new MutableLiveData<>();

    public FilesViewModel(File rootDirectory) {
        this.rootDirectory = rootDirectory;
        initVM(rootDirectory);
    }

    public void initVM(File rootDirectory) {
        this.rootDirectory = rootDirectory;
        fileRepositoryList.setValue(fileTreeMapper.buildFileTree(rootDirectory, 0));
    }



    public File getRootDirectory() {
        return rootDirectory;
    }
    public void setRootDirectory(File rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public MutableLiveData<FileItem> getSelectedFile() {
        return selectedFile;
    }
    public MutableLiveData<List<FileItem>> getFileRepositoryList() {
        return fileRepositoryList;
    }
}
