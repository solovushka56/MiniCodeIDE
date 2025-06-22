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
    private final Map<String, Boolean> expandedStateMap = new HashMap<>();

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
        if(!FileUtils.deleteFile(file)) return;
        updateFilesAsync();
    }

    public void renameFile(File file, String newName) {
        if (!FileUtils.renameFile(file, newName)) return;
        updateFilesAsync();
    }

    public void moveFile(File file, File targetDir) {
        if(!FileUtils.moveFile(file, targetDir)) return;
        updateFilesAsync();
    }

    public void saveFile(File file, String text) {
        executor.execute(() -> {
            FileUtils.writeFileText(file, text);
        });
    }


    private void updateFilesAsync() {
        executor.execute(() -> {
            List<FileItem> updatedList = fileTreeMapper.buildFileTree(rootDirectory, 0);
            restoreExpandedState(updatedList);
            files.postValue(updatedList);
        });
    }


    public void updateFolderExpandedState(File folder, boolean isExpanded) {
        expandedStateMap.put(folder.getAbsolutePath(), isExpanded);
    }

    private void restoreExpandedState(List<FileItem> items) {
        for (FileItem item : items) {
            if (item.isDirectory()) {
                String path = item.getDirectory().getAbsolutePath();
                if (expandedStateMap.containsKey(path)) {
                    item.setExpanded(expandedStateMap.get(path));
                }
                restoreExpandedState(item.getChildren());
            }
        }
    }


    private FileItem findItemByFile(List<FileItem> list, File file) {
        for (FileItem item : list) {
            if (item.getDirectory().equals(file)) {
                return item;
            }
        }
        return null;
    }

}
