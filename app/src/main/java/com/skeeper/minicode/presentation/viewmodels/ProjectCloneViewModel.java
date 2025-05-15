package com.skeeper.minicode.presentation.viewmodels;

import androidx.lifecycle.ViewModel;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LsRemoteCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;

import java.io.File;
import java.util.Collection;

public class ProjectCloneViewModel extends ViewModel {
    private File repoSavePath; // ne nuzno

    public ProjectCloneViewModel(File repoSavePath) {
        this.repoSavePath = repoSavePath;
    }

    public ProjectCloneViewModel() {
    }

    // to other thread
    public void startClone(String uri) {
        new Thread(() -> {
            CloneCommand clone = Git.cloneRepository()
                    .setURI("https://github.com/solovushka56/MiniCodeIDE.git")
                    .setDirectory(repoSavePath);
            try (Git git = clone.call()) {

            } catch (GitAPIException e) {
                e.printStackTrace();
            }


        }).start();
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
