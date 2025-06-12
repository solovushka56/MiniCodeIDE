package com.skeeper.minicode.data.models;

import com.google.gson.annotations.SerializedName;

public class HighlightColorModel {
    @SerializedName("keyword")
    public int keywordColor;

    @SerializedName("type")
    public int typeColor;

    @SerializedName("class")
    public int classColor;

    @SerializedName("method")
    public int methodColor;

    @SerializedName("bracket")
    public int bracketColor;

    @SerializedName("string")
    public int stringColor;

}