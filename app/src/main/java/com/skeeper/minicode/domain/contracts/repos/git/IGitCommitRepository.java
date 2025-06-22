package com.skeeper.minicode.domain.contracts.repos.git;

public interface IGitCommitRepository {
    void commit(String projectPath, String message);
}
