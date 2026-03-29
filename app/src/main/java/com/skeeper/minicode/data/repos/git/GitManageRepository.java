package com.skeeper.minicode.data.repos.git;

import android.util.Log;

import com.skeeper.minicode.domain.models.ProjectModel;

import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;

import java.io.File;
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
}
