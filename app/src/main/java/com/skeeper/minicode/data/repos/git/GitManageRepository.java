package com.skeeper.minicode.data.repos.git;

import android.util.Log;

import com.skeeper.minicode.domain.models.ProjectModel;

import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class GitManageRepository {

    @Inject
    public GitManageRepository() {}

    public void setRepoBranch(ProjectModel project, String branchName) throws Exception {
        try (Git git = Git.open(new File(project.path()))) {

            var status = git.status().call();
            if (!status.isClean()) {
                throw new IllegalStateException(
                        "Repo has uncommitted changes. Make a commit first"
                       //Commit/stash/discard
                );
            }

            List<Ref> localBranches = git.branchList().call();
            boolean localExists = false;

            for (Ref ref : localBranches) {
                if (ref.getName().equals("refs/heads/" + branchName)) {
                    localExists = true;
                    break;
                }
            }

            if (localExists) {
                git.checkout()
                        .setName(branchName)
                        .call();
                return;
            }

            List<Ref> remoteBranches = git.branchList()
                    .setListMode(ListBranchCommand.ListMode.REMOTE)
                    .call();

            boolean remoteExists = false;
            for (Ref ref : remoteBranches) {
                if (ref.getName().equals("refs/remotes/origin/" + branchName)) {
                    remoteExists = true;
                    break;
                }
            }

            if (remoteExists) {
                git.checkout()
                        .setCreateBranch(true)
                        .setName(branchName)
                        .setStartPoint("origin/" + branchName)
                        .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK)
                        .call();
                return;
            }

            throw new IllegalStateException("Branch not found: " + branchName);
        }
    }

    public String getCurrentRepoBranch(ProjectModel project) throws Exception {
        var git = Git.open(new File(project.path()));
        var repo = git.getRepository();
        return repo.getBranch();
    }

    public List<String> getRepoBranches(ProjectModel project) throws Exception {
        try (Git git = Git.open(new File(project.path()))) {
            List<Ref> refs = git.branchList()
                    .setListMode(ListBranchCommand.ListMode.ALL)
                    .call();
            Set<String> result = new LinkedHashSet<>();
            for (Ref ref : refs) {
                String name = ref.getName();
                if (name.startsWith("refs/heads/")) {
                    result.add(name.substring("refs/heads/".length()));
                }
                else if (name.startsWith("refs/remotes/origin/")) {
                    String shortName = name.substring("refs/remotes/origin/".length());
                    if (!shortName.equals("HEAD")) {
                        result.add(shortName);
                    }
                }

            }
            Log.i("BRANCHES_GET", String.valueOf(new ArrayList<>(result).size()));
            return new ArrayList<>(result);
        }
    }


    public void pullChanges(String projectPath, String username, String token) throws Exception {
        try (var git = Git.open(new File(projectPath))) {

            var status = git.status().call();
            boolean isModified = //tracked
                    !status.getModified().isEmpty()
                    || !status.getChanged().isEmpty()
                    || !status.getAdded().isEmpty()
                    || !status.getRemoved().isEmpty()
                    || !status.getMissing().isEmpty()
                    || !status.getConflicting().isEmpty();

            if (isModified) throw new Exception(
                    "Repo has uncommitted changes. Make a commit first");

            var pullCommand = git.pull().setRemote("origin");

            if (username != null && token != null)
                pullCommand.setCredentialsProvider(
                        new UsernamePasswordCredentialsProvider(username, token));

            var result = pullCommand.call();
            if (!result.isSuccessful()) {
                throw new IllegalStateException(
                        "Pull failed: " + result.toString());
            }
        }
    }


    public String pushChanges(File projectDir,
                              String username,
                              String token,
                              String commitMessage
    ) {
        Repository repository = null;
        try {
            repository = new FileRepositoryBuilder()
                    .readEnvironment()
                    .findGitDir(projectDir)
                    .build();
        }
        catch (IOException e) { return "Error: " + e.getMessage(); }

        try (var git = new Git(repository)) {

            git.add().addFilepattern(".").call();
            git.commit().setMessage(commitMessage).call();
            git.push().setCredentialsProvider(
                    new UsernamePasswordCredentialsProvider(username, token)
            ).call();

            return "Push successful";
        }
        catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }


    public String getRemoteUrl(File repoDir) throws Exception {
        try (Git git = Git.open(repoDir)) {
            var config = git.getRepository().getConfig();
            return config.getString("remote", "origin", "url");
        }
    }
}
