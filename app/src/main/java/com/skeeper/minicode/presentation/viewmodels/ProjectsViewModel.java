package com.skeeper.minicode.presentation.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.skeeper.minicode.core.singleton.ProjectManager;
import com.skeeper.minicode.domain.models.ProjectModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ProjectsViewModel extends ViewModel {

    private final ExecutorService executor = Executors.newFixedThreadPool(2);

    private final MutableLiveData<List<ProjectModel>>
            models = new MutableLiveData<>(new ArrayList<>());

    @Inject ProjectManager projectManager;

    @Inject
    public ProjectsViewModel() {}

    public void loadModelsAsync() {
        executor.execute(() -> {
            models.postValue(projectManager.loadAllProjectModels());
        });
    }

    public MutableLiveData<List<ProjectModel>> getModels() {
        return models;
    }
}
