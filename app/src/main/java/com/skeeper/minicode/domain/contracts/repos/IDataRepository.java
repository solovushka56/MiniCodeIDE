package com.skeeper.minicode.domain.contracts.repos;

public interface IDataRepository<T> {
    T load();
    void save();
}
