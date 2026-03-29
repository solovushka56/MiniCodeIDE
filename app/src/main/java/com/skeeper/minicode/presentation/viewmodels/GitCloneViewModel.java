package com.skeeper.minicode.presentation.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.skeeper.minicode.core.singleton.ProjectManager;
import com.skeeper.minicode.data.repos.file.LocalFileRepository;
import com.skeeper.minicode.domain.contracts.other.providers.IFileDirectoryProvider;
import com.skeeper.minicode.domain.enums.RepoCloningState;
import com.skeeper.minicode.domain.models.ProjectModel;
import com.skeeper.minicode.utils.helpers.ProjectRectColorBinding;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel

public class GitCloneViewModel extends ViewModel {

    IFileDirectoryProvider fileDirectoryProvider;
    private LocalFileRepository projectDirRepository;

    private File projectDir;
    private String projectName;

    private MutableLiveData<RepoCloningState> cloningState = new MutableLiveData<>();
    private MutableLiveData<Integer> clonedPercent = new MutableLiveData<>(0);

    public String username = null;
    public String token = null;


    @Inject
    ProjectManager projectManager;

    @Inject
    public GitCloneViewModel(IFileDirectoryProvider fileDirectoryProvider) {
        this.fileDirectoryProvider = fileDirectoryProvider;
    }


    public void cloneProject(String uri, String projName, int timeout) {
        this.projectName = projName;
        new Thread(() -> {
            CloneCommand clone = Git.cloneRepository()
                    .setURI(uri)
                    .setDirectory(genProjectFolder(projName))
                    .setProgressMonitor(new ProgressMonitor() {
                        private int totalTasks;
                        private int completedTasks;
                        private int lastProgress = -1;

                        @Override
                        public void start(int totalTasks) {
                            this.totalTasks = totalTasks;

                        }

                        @Override
                        public void beginTask(String title, int totalWork) {
                            completedTasks = 0;
                        }

                        @Override
                        public void update(int completed) {
                            completedTasks += completed;

                            int currentProgress = totalTasks > 0 ?
                                    (int) ((completedTasks / (float) totalTasks)) : 0;

                            if (currentProgress != lastProgress) {
                                lastProgress = currentProgress;
                                getClonedPercent().postValue(currentProgress);
                            }
                        }

                        @Override
                        public void endTask() {
                        }

                        @Override
                        public boolean isCancelled() {
                            return false;
                        }
                    });

            if (timeout != -1) {
                clone.setTimeout(timeout);
            }

            if (username != null && token != null) {
                clone.setCredentialsProvider(new
                        UsernamePasswordCredentialsProvider(username, token));
            }
            try {
                Git git = clone.call();
                onRepoCloned();
                getCloningState().postValue(RepoCloningState.COMPLETE);
                Log.i("JGIT", "Cloning successful");
            } catch (Exception e) {
                cloningState.postValue(RepoCloningState.FAILED);
                projectManager.deleteProject(projectName);
                Log.e("JGIT", "Clone Failed: " + e.getMessage());

            }
        }).start();

    }



    public void onRepoCloned() throws IOException {
        addMinicodeToGitExclude(projectDir);
        var rectPalette = new ProjectRectColorBinding();
        var model = new ProjectModel(
                projectName,
                "cloned from git",
                projectDir.getPath(),
                new String[] {"git"},
                rectPalette.getMainRectColor(),
                rectPalette.getInnerRectColor(),
                ""
        );
        try {
            projectManager.generateMetadata(model);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public File genProjectFolder(String name) {
        projectDir = new File(projectManager.getProjectsStoreFolder(), name);
        boolean created = projectDir.mkdirs();
        return projectDir;
    }

    public MutableLiveData<RepoCloningState> getCloningState() {
        return cloningState;
    }

    public MutableLiveData<Integer> getClonedPercent() {
        return clonedPercent;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setToken(String token) {
        this.token = token;
    }



    private void addMinicodeToGitExclude(File repoDir) throws IOException {
        File excludeFile = new File(repoDir, ".git/info/exclude");
        File parent = excludeFile.getParentFile();

        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        final String rule = ".minicode/";
        boolean alreadyExists = false;
        boolean endsWithNewLine = true;

        if (excludeFile.exists()) {
            try (var reader = new BufferedReader(new FileReader(excludeFile))) {
                String line;
                String lastLine = null;

                while ((line = reader.readLine()) != null) {
                    if (line.trim().equals(rule)) {
                        alreadyExists = true;
                        break;
                    }
                    lastLine = line;
                }

                if (lastLine != null) {
                    endsWithNewLine = true; // readLine съедает \n, для append ниже просто добавим свой
                }
            }
        }

        if (alreadyExists) return;

        try (var writer = new BufferedWriter(new FileWriter(excludeFile, true))) {
            if (excludeFile.length() > 0 && !endsWithNewLine) {
                writer.newLine();
            }
            writer.write(rule);
            writer.newLine();
        }
    }
}



