package com.skeeper.minicode.presentation.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.skeeper.minicode.core.singleton.ProjectManager;
import com.skeeper.minicode.domain.contracts.other.IFileDirectoryProvider;
import com.skeeper.minicode.domain.contracts.repos.IFileRepository;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LsRemoteCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.AbortedByHookException;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.ServiceUnavailableException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.UnmergedPathsException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ProjectCloneViewModel extends ViewModel {
    IFileDirectoryProvider fileDirectoryProvider;

    @Inject
    ProjectManager projectManager;

    private MutableLiveData<Boolean> isCloned = new MutableLiveData<>(false);



    @Inject
    public ProjectCloneViewModel(IFileDirectoryProvider fileDirectoryProvider) {
        this.fileDirectoryProvider = fileDirectoryProvider;
    }

    // to other thread
    public void cloneProject(String uri, String projName, String branch) {
        new Thread(() -> {
            CloneCommand clone = Git.cloneRepository()
                    .setURI("https://github.com/solovushka56/MiniCodeIDE.git")
                    .setDirectory(genProjectFolder(projName))
                    .setBranch(branch)
                    .setCallback(new CloneCommand.Callback() {
                        @Override
                        public void initializedSubmodules(Collection<String> submodules) {

                        }

                        @Override
                        public void cloningSubmodule(String path) {

                        }

                        @Override
                        public void checkingOut(AnyObjectId commit, String path) {
                            isCloned.setValue(true);
                            onRepoCloned();
                        }
                    });
//                    .setCallback();
            try (Git git = clone.call()) {

            } catch (GitAPIException e) {
                Log.e("JGIT", "Clone Failed");
            }
        }).start();
    }

    // todo : move to project repository and String proJname -> File projectDir or ProjectModel project
    public void commitAndPushProject(String projName) {
        try {
            Git git = Git.open(projectManager.getProjectDir(projName));
            git.add().addFilepattern(".").call();
            git.commit().setMessage("minicode commit").call();
            git.push()
                    .setRemote("origin") // refactor this todo
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider("login", "pat")).call();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoHeadException e) {
            throw new RuntimeException(e);
        } catch (UnmergedPathsException e) {
            throw new RuntimeException(e);
        } catch (NoFilepatternException e) {
            throw new RuntimeException(e);
        } catch (WrongRepositoryStateException e) {
            throw new RuntimeException(e);
        } catch (InvalidRemoteException e) {
            throw new RuntimeException(e);
        } catch (ServiceUnavailableException e) {
            throw new RuntimeException(e);
        } catch (ConcurrentRefUpdateException e) {
            throw new RuntimeException(e);
        } catch (AbortedByHookException e) {
            throw new RuntimeException(e);
        } catch (TransportException e) {
            throw new RuntimeException(e);
        } catch (NoMessageException e) {
            throw new RuntimeException(e);
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }

    }

    public void onRepoCloned() {

    }

    public File genProjectFolder(String name) {
        return new File(projectManager.getProjectsStoreFolder(), name);
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
