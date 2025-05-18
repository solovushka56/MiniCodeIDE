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
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.RemoteAddCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

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


    public void commitAndPushProject(String projectName, String pushUrl, String commitName, String commitMessage) {
        try {
//            Git git = Git.open(projectManager.getProjectDir(projectName));
//
//            // add remote repo:
//            RemoteAddCommand remoteAddCommand = git.remoteAdd();
//            remoteAddCommand.setName("origin");
//            remoteAddCommand.setUri(new URIish(pushUrl));
//            // you can add more settings here if needed
//            remoteAddCommand.call();
//
//            // push to remote:
//            PushCommand pushCommand = git.push();
//            pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(
//                    "solovushka56", ""));
//            // you can add more settings here if needed
//            pushCommand.call();
//            Git git = Git.open(projectManager.getProjectDir(projectName));
//
//            git.add().addFilepattern(".").call();
//
//            git.commit()
//                    .setMessage("Commit from Android app")
//                    .call();
//
//
//            CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(
//                    "solovushka56", "");
//
//            PushCommand pushCommand = git.push()
//                    .setRemote(pushUrl)
//                    .setCredentialsProvider(credentialsProvider)
//                    .setForce(true);
//
//            pushCommand.call();
//            git.close();
/// ///////////////////////////////////////////////////////////////
//            PushCommand pushCommand = git.push();
//            pushCommand.setRemote(pushUrl)
//                    .setCredentialsProvider(
//                            new UsernamePasswordCredentialsProvider("solovushka56", ""));
//
//            pushCommand.call();
//            Git git = Git.open(projectManager.getProjectDir(projectModel.getProjectName()));
//            git.add().addFilepattern(".").call();
//            git.commit().setMessage("noice commit").call();
//            git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider(
//                            "solovushka56",
//                            ""))
//                    .call();

        } catch (Exception e) {
            Log.e("LOL1", e.getMessage() != null ? e.getMessage() : "null exc");
            throw new RuntimeException(e);
        }

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
            projectManager.generateProjectIdeFiles(projectDir, model);
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


}
