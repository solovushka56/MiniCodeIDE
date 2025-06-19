package com.skeeper.minicode.presentation.viewmodels;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.skeeper.minicode.core.singleton.ProjectManager;
import com.skeeper.minicode.data.mappers.ProjectMapper;
import com.skeeper.minicode.domain.contracts.other.providers.IFileDirectoryProvider;
import com.skeeper.minicode.domain.enums.RepoCloningState;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class GitPushViewModel extends ViewModel {

    IFileDirectoryProvider fileDirectoryProvider;

    private File projectDir;

    private String projectName;
    private String projectDescription;
    private String projectPath;
    private String[] tags;

    private final MutableLiveData<String> pushResult = new MutableLiveData<>();

    public String username = null;
    public String token = null;

    @Inject ProjectManager projectManager;


    @Inject
    public GitPushViewModel(IFileDirectoryProvider fileDirectoryProvider) {
        this.fileDirectoryProvider = fileDirectoryProvider;
    }


    public File genProjectFolder(String name) {
        projectDir = new File(projectManager.getProjectsStoreFolder(), name);
        boolean created = projectDir.mkdirs();
        projectPath = projectDir.getAbsolutePath();
        return projectDir;
    }


    public void createCommitAndPush(File repoDir, String commitMessage) {
        if (username ==  null || token == null) return;
        GitPushTask task = new GitPushTask(
                this,
                repoDir,
                username,
                token,
                commitMessage);
        task.execute();
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
        GitPushTask(GitPushViewModel viewModel,
                    File projectDir,
                    String username,
                    String token,
                    String commitMessage) {
            this.viewModelRef = new WeakReference<>(viewModel);
            this.username = username;
            this.token = token;
            this.projectDir = projectDir;
            this.commitMessage = commitMessage;
        }

        @Override
        protected String doInBackground(Void... voids) {
            File gitDir = new File(projectDir, ".git");

            Repository repository = null;
            try {
                repository = new FileRepositoryBuilder()
                        .readEnvironment()
                        .findGitDir(projectDir)
                        .build();
            } catch (IOException e) {
                return "Error: " + e.getMessage();
            }
            try (Git git = new Git(repository)) {

                git.add()
                        .addFilepattern(".")
                        .call();

                git.commit()
                        .setMessage(commitMessage)
                        .call();

                git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider(
                        username, token)).call();

                return "Push successful";
            } catch (Exception e) {
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            GitPushViewModel viewModel = viewModelRef.get();
            if (viewModel != null) {
                viewModel.pushResult.setValue(result);
            }
        }

    }

}



