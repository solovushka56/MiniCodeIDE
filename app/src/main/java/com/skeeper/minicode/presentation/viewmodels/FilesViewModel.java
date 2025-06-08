package com.skeeper.minicode.presentation.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.skeeper.minicode.data.mappers.FileTreeMapper;
import com.skeeper.minicode.domain.models.FileItem;
import com.skeeper.minicode.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/// for file tree ierarch management
public class FilesViewModel extends ViewModel {

    private File rootDirectory;

    private final MutableLiveData<List<FileItem>> files = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final FileTreeMapper fileTreeMapper = new FileTreeMapper();

    public FilesViewModel(File rootDirectory) {
        this.rootDirectory = rootDirectory;
        updateFilesAsync();
    }


    public MutableLiveData<List<FileItem>> getFiles() {
        return files;
    }
    public File getRootDirectory() {
        return rootDirectory;
    }
    public void setRootDirectory(File rootDirectory) {
        this.rootDirectory = rootDirectory;
    }




    public void createFile(File newFile) {
        if(!FileUtils.createFile(newFile)) return;
        updateFilesAsync();
    }

    public void createFolder(File directory, String name) {
        if(!FileUtils.createFolder(directory, name)) return;
        updateFilesAsync();
    }

    public void deleteFile(File file) {

        FileUtils.deleteFile(file);
    }

    public void renameFile(File file, String newName) {

        FileUtils.renameFile(file, newName);
    }

    public void moveFile(File file, File targetDir) {
        FileUtils.moveFile(file, targetDir);
    }


    public void writeFileText(File file, String text) {
        FileUtils.writeFileText(file, text);
    }

    private void updateFilesAsync() {
        executor.execute(() -> {
            var updatedList = fileTreeMapper.buildFileTree(rootDirectory, 0);
            files.postValue(updatedList);
        });
    }
}
