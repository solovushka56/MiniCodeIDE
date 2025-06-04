package com.skeeper.minicode.presentation.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.skeeper.minicode.core.singleton.ProjectManager;
import com.skeeper.minicode.domain.contracts.other.IFileDirectoryProvider;
import com.skeeper.minicode.domain.enums.RepoCloningState;
import com.skeeper.minicode.domain.models.ProjectModel;
import com.skeeper.minicode.utils.helpers.ProjectRectColorBinding;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LsRemoteCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RemoteRefUpdate;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class GitViewModel extends ViewModel {
    IFileDirectoryProvider fileDirectoryProvider;
    private File projectDir;

    private int id;
    private String projectName;
    private String projectDescription;
    private String projectPath;
    private String[] tags;


    @Inject
    ProjectManager projectManager;

    private MutableLiveData<RepoCloningState> cloningState = new MutableLiveData<>();


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
    public void commitAndPushProject(
            String projectName,
            String pushUrl,
            String commitName,
            String commitMessage)
    {
        try (Git git = Git.open(projectManager.getProjectDir(projectName))) {
//            try (Git git = Git.open(projectManager.getProjectDir(projectName))) {
//                git.add().addFilepattern(".").call();
//                git.commit().setMessage(commitName).call();
//                git.push()
//                        .setRemote(pushUrl)
//                        .setCredentialsProvider(new UsernamePasswordCredentialsProvider(
//                                "",
//                                ""))
//                        .call();
//            }
            createCommit(git, commitMessage);
            Iterable<PushResult> results = git.push()
                    .setRemote(pushUrl)
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(
                            "", ""))
                    .call();
            for (PushResult r : results) {
                for(RemoteRefUpdate update : r.getRemoteUpdates()) {
                    if(update.getStatus() != RemoteRefUpdate.Status.OK && update.getStatus() != RemoteRefUpdate.Status.UP_TO_DATE) {
                        String errorMessage = "Push failed: "+ update.getStatus();
                        Log.e("JGIT_PUSH", errorMessage);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("JGIT_PUSH", e.getMessage() != null ? e.getMessage() : "failed");
        }

    }

    private void createCommit(Git git, String commitMessage) throws IOException, GitAPIException {

        // run the add
        git.add()
                .addFilepattern(".")
                .call();

        RevCommit revCommit = git.commit()
                .setMessage(commitMessage)
                .call();
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


}
