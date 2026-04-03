package com.skeeper.minicode.presentation.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.skeeper.minicode.core.singleton.ProjectManager;
import com.skeeper.minicode.data.repos.git.GitManageRepository;
import com.skeeper.minicode.domain.models.ProjectModel;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class GitPullViewModel extends ViewModel {
    ExecutorService executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<String> pullResult = new MutableLiveData<>();
    public String username = null;
    public String token = null;

    private final GitManageRepository gitManageRepository;
    private final ProjectManager projectManager;

    @Inject
    public GitPullViewModel(GitManageRepository gitManageRepository, ProjectManager projectManager) {
        this.gitManageRepository = gitManageRepository;
        this.projectManager = projectManager;
    }


    public void makeGitPull(String projectPath) {
        if (projectPath == null || username ==  null || token == null) return;
        executor.execute(() -> {
            try {
                gitManageRepository.pullChanges(projectPath, username, token);
                pullResult.postValue("Pull Success");
            }
            catch (Exception e) {
                pullResult.postValue(e.getMessage());
            }
        });
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public MutableLiveData<String> getPullResult() {
        return pullResult;
    }
}
