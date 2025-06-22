package com.skeeper.minicode.domain.contracts.repos.git;

import com.skeeper.minicode.domain.exceptions.git.GitException;

/// repo that manages clone/push etc, ie remote interacts
/// with Git repositories
public interface IGitCloneRepository {
    void clone(String url,
               String username,
               String token,
               int timeout) throws GitException;
}
