package com.skeeper.minicode.presentation.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.skeeper.minicode.core.singleton.ProjectManager;
import com.skeeper.minicode.data.mappers.ProjectMapper;
import com.skeeper.minicode.data.models.ProjectModelParcelable;
import com.skeeper.minicode.data.sources.preferences.UserPreferencesProvider;
import com.skeeper.minicode.domain.models.ProjectModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class StartScreenViewModel extends ViewModel {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<ProjectModelParcelable> recentProject =
            new MutableLiveData<>(null);
    private final UserPreferencesProvider userPreferencesProvider;
    private final ProjectManager projectManager;
    private final ProjectMapper projectMapper;
    @Inject
    public StartScreenViewModel(
            UserPreferencesProvider userPreferencesProvider,
            ProjectManager projectManager,
            ProjectMapper projectMapper
    ) {
        this.userPreferencesProvider = userPreferencesProvider;
        this.projectManager = projectManager;
        this.projectMapper = projectMapper;
    }


    public void loadRecentProject() {
        executor.execute(() -> {
            var name = userPreferencesProvider.getRecentProjectName();
            if (name.isEmpty() || !projectManager.projectExists(name)) {
                recentProject.postValue(null);
                return;
            }
            var project = projectMapper.mapFromDomain(
                    projectManager.loadProjectModel(name));
            recentProject.postValue(project);
        });
    }

    public MutableLiveData<ProjectModelParcelable> getRecentProject() {
        return recentProject;
    }
}
