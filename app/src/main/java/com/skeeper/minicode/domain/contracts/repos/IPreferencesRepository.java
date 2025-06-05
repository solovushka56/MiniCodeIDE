package com.skeeper.minicode.domain.contracts.repos;

public interface IPreferencesRepository {
    boolean getIsFirstAppRun();
    void setIsFirstAppRun(boolean value);
}
