package com.skeeper.minicode.domain.contracts.repos.git;

import com.skeeper.minicode.domain.exceptions.git.GitException;

public interface IGitPushRepository { /// todo
    void push(String projectDir, String username, String token) throws GitException;
}
