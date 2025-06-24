package com.skeeper.minicode.data.repos.git;

import com.skeeper.minicode.domain.contracts.repos.git.IGitCloneRepository;
import com.skeeper.minicode.domain.exceptions.git.GitException;

import org.eclipse.jgit.api.CloneCommand;

public class GitCloneRepository implements IGitCloneRepository {
    @Override
    public void clone(CloneCommand cloneCommand) throws GitException {

    }
}
