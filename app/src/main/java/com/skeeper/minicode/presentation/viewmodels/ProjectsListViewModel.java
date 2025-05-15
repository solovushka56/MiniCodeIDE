package com.skeeper.minicode.presentation.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.skeeper.minicode.domain.models.ProjectModel;

import java.util.List;

public class ProjectsListViewModel extends ViewModel {
    private LiveData<List<ProjectModel>> projectModels;


    public LiveData<List<ProjectModel>> getProjectModels() {
        return projectModels;
    }




}
