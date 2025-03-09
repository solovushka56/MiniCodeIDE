package com.skeeper.minicode.models;

public class KeySymbolItemModel {

    private int id;
    private String symbolKey;
    private String symbolValue;

    public KeySymbolItemModel(int id, String symbolKey, String symbolValue) {
        this.id = id;
        this.symbolKey = symbolKey;
        this.symbolValue = symbolValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSymbolKey() {
        return symbolKey;
    }

    public void setSymbolKey(String symbolKey) {
        this.symbolKey = symbolKey;
    }

    public String getSymbolValue() {
        return symbolValue;
    }

    public void setSymbolValue(String symbolValue) {
        this.symbolValue = symbolValue;
    }
}
