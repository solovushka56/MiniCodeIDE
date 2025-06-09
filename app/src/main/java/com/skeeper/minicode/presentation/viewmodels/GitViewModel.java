package com.skeeper.minicode.presentation.viewmodels;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.skeeper.minicode.core.singleton.ProjectManager;
import com.skeeper.minicode.domain.contracts.other.providers.IFileDirectoryProvider;
import com.skeeper.minicode.domain.enums.RepoCloningState;
import com.skeeper.minicode.domain.models.ProjectModel;
import com.skeeper.minicode.utils.helpers.ProjectRectColorBinding;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LsRemoteCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RemoteRefUpdate;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Collection;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class GitViewModel extends ViewModel {

    IFileDirectoryProvider fileDirectoryProvider;
    private File projectDir;

    private String projectName;
    private String projectDescription;
    private String projectPath;
    private String[] tags;

    private final MutableLiveData<String> pushResult = new MutableLiveData<>();

    @Inject ProjectManager projectManager;

    private MutableLiveData<RepoCloningState>
            cloningState = new MutableLiveData<>();


    @Inject
    public GitViewModel(IFileDirectoryProvider fileDirectoryProvider) {
        this.fileDirectoryProvider = fileDirectoryProvider;
    }

    public MutableLiveData<RepoCloningState> getCloningState() {
        return cloningState;
    }



    public void cloneProject(String uri, String projName) {
        this.projectName = projName;
        new Thread(() -> {
            CloneCommand clone = Git.cloneRepository()
                    .setURI(uri)
                    .setDirectory(genProjectFolder(projName))
                    .setProgressMonitor(new ProgressMonitor() {
                        @Override
                        public void start(int totalTasks) {
                            cloningState.postValue(RepoCloningState.START);
                        }

                        @Override
                        public void beginTask(String title, int totalWork) {

                        }

                        @Override
                        public void update(int completed) {

                        }

                        @Override
                        public void endTask() {
                        }

                        @Override
                        public boolean isCancelled() {
                            return false;
                        }
                    });
            try {
                Git git = clone.call();
            } catch (GitAPIException e) {
                cloningState.postValue(RepoCloningState.FAILED);
                Log.e("JGIT", "Clone Failed");

            }
            onRepoCloned();
        }).start();

    }
    public void onRepoCloned() {
        var rectPalette = new ProjectRectColorBinding();
        ProjectModel model = new ProjectModel(0,
                projectName,
                "cloned from git",
                "projFilepath",
                new String[] {"git"},
                rectPalette.getMainRectColor(),
                rectPalette.getInnerRectColor(),
                "today"
        );
        try {
            projectManager.generateMetadata(projectDir, model);
        } catch (IOException e) {
            throw new RuntimeException(e);
        };
        getCloningState().postValue(RepoCloningState.END);
    }
    public File genProjectFolder(String name) {
        projectDir = new File(projectManager.getProjectsStoreFolder(), name);
        boolean created = projectDir.mkdirs();
        projectPath = projectDir.getAbsolutePath();
        return projectDir;
    }



    public static String getRepoName(String repoUrl) {

        LsRemoteCommand lsRemote = Git.lsRemoteRepository();
        lsRemote.setRemote(repoUrl);

        try {
            Collection<Ref> refs = lsRemote.call();
            for (Ref ref : refs) {
                System.out.println(ref.getName() + " -> " + ref.getObjectId().getName());
            }
            String repoName = repoUrl.replaceAll(".*/([^/]+)\\.git$", "$1");
            return repoName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "no_name";
    }


    public void createCommitAndPush(File repoDir,
                                    String username,
                                    String passwordOrToken,
                                    String commitMessage)
    {
        GitPushTask task = new GitPushTask(
                this,
                repoDir,
                username,
                passwordOrToken,
                commitMessage);
        task.execute();
    }

    public MutableLiveData<String> getPushResult() {
        return pushResult;
    }

    private static class GitPushTask extends AsyncTask<Void, Void, String> {
        private final WeakReference<GitViewModel> viewModelRef;
        private final String username;
        private final String token;
        private final File projectDir;
        private final String commitMessage;
        GitPushTask(GitViewModel viewModel, File projectDir, String username, String token, String commitMessage) {
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
            GitViewModel viewModel = viewModelRef.get();
            if (viewModel != null) {
                viewModel.pushResult.setValue(result);
            }
        }
    }









}



