package com.skeeper.minicode.presentation.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.skeeper.minicode.core.singleton.ProjectManager;
import com.skeeper.minicode.data.repos.git.GitManageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class GitManageViewModel extends ViewModel {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final ProjectManager projectManager;
    private final GitManageRepository gitManageRepository;

    private final MutableLiveData<List<String>> projectBranches =
            new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> currentBranch =
            new MutableLiveData<>("");
    private final MutableLiveData<BranchSetResult> branchSetResult =
            new MutableLiveData<>(); // true means success

    @Inject
    public GitManageViewModel(ProjectManager projectManager,
                              GitManageRepository gitManageRepository) {
        this.projectManager = projectManager;
        this.gitManageRepository = gitManageRepository;
    }


    public void setRepoBranch(String projectName, String branch) {
        executor.execute(() -> {
            try {
                gitManageRepository.setRepoBranch(
                        projectManager.loadProjectModel(projectName),
                        branch
                );

                branchSetResult.postValue(new BranchSetResult(branch, null));
                currentBranch.postValue(branch);
            }
            catch (Exception e) {
                Log.e("BranchChanged", "Failed to chang branch: " + e.getMessage());
                branchSetResult.postValue(new BranchSetResult(null, e.getMessage()));
            }
        });
    }
    public void loadProjectBranches(String projectName) {
        executor.execute(() -> {
            try {
                var project = projectManager.loadProjectModel(projectName);
                var result = gitManageRepository.getRepoBranches(project);
                projectBranches.postValue(result);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    public void loadCurrentBranch(String projectName) {
        executor.execute(() -> {
            try {
                var project = projectManager.loadProjectModel(projectName);
                var branch = gitManageRepository.getCurrentRepoBranch(project);
                currentBranch.postValue(branch);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public MutableLiveData<List<String>> getProjectBranches() {
        return projectBranches;
    }
    public MutableLiveData<String> getCurrentBranch() {
        return currentBranch;
    }

    public MutableLiveData<BranchSetResult> getBranchSetResult() {
        return branchSetResult;
    }


    public class BranchSetResult {
        public String currentBranch = null;
        public String errorMessage = null;

        public BranchSetResult(String currentBranch, String errorMessage) {
            this.currentBranch = currentBranch;
            this.errorMessage = errorMessage;
        }
    }

}


