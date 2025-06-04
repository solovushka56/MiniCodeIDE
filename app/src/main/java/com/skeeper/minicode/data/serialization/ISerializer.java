package com.skeeper.minicode.data.serialization;

// todo move to infrastructure
public interface ISerializer<T> {
    String serialize(T model);
    T deserialize(String json);
}
