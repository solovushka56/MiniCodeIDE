package com.skeeper.minicode.domain.models;

public class SnippetModel {

    private String symbolKey;
    private String symbolValue;

    public SnippetModel(String symbolKey, String symbolValue) {
        this.symbolKey = symbolKey;
        this.symbolValue = symbolValue;
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
