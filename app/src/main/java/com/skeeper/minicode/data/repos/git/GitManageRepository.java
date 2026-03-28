package com.skeeper.minicode.data.repos.git;

import com.skeeper.minicode.domain.models.ProjectModel;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class GitManageRepository {

    @Inject
    public GitManageRepository() {}

    public void setRepoBranch(ProjectModel project, String branch) throws Exception {
        var git = Git.open(new File(project.path()));
        git.checkout()
                .setName(branch)
                .call();
    }

    public String getCurrentRepoBranch(ProjectModel project) throws Exception {
        var git = Git.open(new File(project.path()));
        var repo = git.getRepository();
        return repo.getBranch();
    }

    public List<String> getRepoBranches(ProjectModel project) throws Exception {
        var git = Git.open(new File(project.path()));
        List<String> localBranches = git.branchList()
                .setListMode(ListBranchCommand.ListMode.ALL)
                .call()
                .stream()
                .map(Ref::getName)
                .map(Repository::shortenRefName)
                .collect(Collectors.toList());
        return localBranches;
    }
}
