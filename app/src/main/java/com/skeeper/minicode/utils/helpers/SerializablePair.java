package com.skeeper.minicode.utils.helpers;

public class SerializablePair {
    private String key;
    private String value;

    public SerializablePair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}