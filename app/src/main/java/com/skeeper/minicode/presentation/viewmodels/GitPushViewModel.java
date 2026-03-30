package com.skeeper.minicode.presentation.viewmodels;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.skeeper.minicode.core.singleton.ProjectManager;
import com.skeeper.minicode.data.repos.git.GitManageRepository;
import com.skeeper.minicode.domain.contracts.other.providers.IFileDirectoryProvider;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class GitPushViewModel extends ViewModel {

    private final IFileDirectoryProvider fileDirectoryProvider;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final MutableLiveData<String> pushResult = new MutableLiveData<>();

    private final GitManageRepository gitManageRepository;
    private final ProjectManager projectManager;

    public String username = null;
    public String token = null;

    @Inject
    public GitPushViewModel(
            ProjectManager projectManager,
            IFileDirectoryProvider fileDirectoryProvider,
            GitManageRepository gitManageRepository
    ) {
        this.projectManager = projectManager;
        this.fileDirectoryProvider = fileDirectoryProvider;
        this.gitManageRepository = gitManageRepository;
    }


    public void createCommitAndPush(File repoDir, String commitMessage) {
        if (username ==  null || token == null || commitMessage.isEmpty()) return;

        executor.execute(() -> {
            var task = new GitPushTask(this, gitManageRepository,
                    repoDir, username, token, commitMessage);
            task.execute();
        });
    }



    public MutableLiveData<String> getPushResult() {
        return pushResult;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private static class GitPushTask extends AsyncTask<Void, Void, String> {
        private final WeakReference<GitPushViewModel> viewModelRef;
        private final String username;
        private final String token;
        private final File projectDir;
        private final String commitMessage;
        private final GitManageRepository gitManageRepository;
        GitPushTask(GitPushViewModel viewModel,
                    GitManageRepository gitManageRepository,
                    File projectDir,
                    String username,
                    String token,
                    String commitMessage
        ) {
            this.viewModelRef = new WeakReference<>(viewModel);
            this.username = username;
            this.token = token;
            this.projectDir = projectDir;
            this.commitMessage = commitMessage;
            this.gitManageRepository = gitManageRepository;
        }

        @Override
        protected String doInBackground(Void... voids) {
            return gitManageRepository.pushChanges(projectDir,
                    username, token, commitMessage);
        }

        @Override
        protected void onPostExecute(String result) {
            GitPushViewModel viewModel = viewModelRef.get();
            if (viewModel != null) viewModel.pushResult.postValue(result);
        }
    }
}



