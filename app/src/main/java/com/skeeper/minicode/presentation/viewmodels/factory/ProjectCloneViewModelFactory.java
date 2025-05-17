package com.skeeper.minicode.presentation.viewmodels.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.skeeper.minicode.presentation.viewmodels.FilesViewModel;
import com.skeeper.minicode.presentation.viewmodels.ProjectCloneViewModel;

import java.io.File;
// ne nuzno
public class ProjectCloneViewModelFactory implements ViewModelProvider.Factory {
//    private final File repoSavePath; // uri or ssh

//    public ProjectCloneViewModelFactory(File repoSavePath) {
//        this.repoSavePath = repoSavePath;
//    }
//
//    @NonNull
//    @Override
//    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
//        if (modelClass.isAssignableFrom(ProjectCloneViewModel.class)) {
//            return (T) new ProjectCloneViewModel(repoSavePath);
//        }
//        throw new IllegalArgumentException("not file vm params");
//    }

}
