package com.skeeper.minicode.presentation.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.skeeper.minicode.data.mappers.FileTreeMapper;
import com.skeeper.minicode.domain.contracts.repos.IFileRepository;
import com.skeeper.minicode.domain.models.FileItem;
import com.skeeper.minicode.utils.FileUtils;

import java.io.File;
import java.util.List;

public class FilesViewModel extends ViewModel implements IFileRepository {

    private File rootDirectory;

    private final MutableLiveData<List<FileItem>> fileRepositoryList = new MutableLiveData<>();
    private final MutableLiveData<FileItem> selectedFile = new MutableLiveData<>();


    public FilesViewModel(File rootDirectory) {
        this.rootDirectory = rootDirectory;
        FileTreeMapper fileTreeMapper = new FileTreeMapper();
        fileRepositoryList.setValue(fileTreeMapper.buildFileTree(rootDirectory, 0));
    }






    public MutableLiveData<FileItem> getSelectedFile() {
        return selectedFile;
    }
    public MutableLiveData<List<FileItem>> getFileRepositoryList() {
        return fileRepositoryList;
    }
    public File getRootDirectory() {
        return rootDirectory;
    }
    public void setRootDirectory(File rootDirectory) {
        this.rootDirectory = rootDirectory;
    }


    @Override
    public File getFile() {
        return selectedFile.getValue().getDirectory();
    }

    @Override
    public void createFile() {
        FileUtils.createFile(selectedFile.getValue().getDirectory());
    }

    @Override
    public void deleteFile() {
        FileUtils.deleteFile(selectedFile.getValue().getDirectory());
    }

    @Override
    public void renameFile(String newName) {
        FileUtils.renameFile(selectedFile.getValue().getDirectory(), newName);
    }

    @Override
    public void moveFile(File targetDir) {
        FileUtils.moveFile(selectedFile.getValue().getDirectory(), targetDir);
    }

    @Override
    public String readFileText() {
        return FileUtils.readFileText(selectedFile.getValue().getDirectory());
    }

    @Override
    public void writeFileText(String text) {
        FileUtils.writeFileText(selectedFile.getValue().getDirectory(), text);
    }
}
