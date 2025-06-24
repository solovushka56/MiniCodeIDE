package com.skeeper.minicode.domain.contracts.repos.git;

import com.skeeper.minicode.domain.exceptions.git.GitException;

import org.eclipse.jgit.api.CloneCommand;

/// repo that manages clone/push etc, ie remote interacts
/// with Git repositories
public interface IGitCloneRepository {
    void clone(CloneCommand cloneCommand) throws GitException;
}
