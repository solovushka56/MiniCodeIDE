package com.skeeper.minicode.domain.models;

import com.skeeper.minicode.domain.enums.ExtensionType;

import java.util.List;

public class LangModel {

    ExtensionType langType;

    List<String> keywords;
    List<String> operators;
    List<String> primitiveTypes;
    List<String> objectTypes;
    List<String> attributes;


    public LangModel(ExtensionType langType, List<String> keywords, List<String> operators,
                     List<String> objectTypes, List<String> primitiveTypes,
                     List<String> attributes)
    {
        this.langType = langType;
        this.keywords = keywords;
        this.operators = operators;
        this.objectTypes = objectTypes;
        this.primitiveTypes = primitiveTypes;
        this.attributes = attributes;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public List<String> getOperators() {
        return operators;
    }

    public List<String> getObjectTypes() {
        return objectTypes;
    }

    public List<String> getPrimitiveTypes() {
        return primitiveTypes;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public ExtensionType getLangType() {
        return langType;
    }
}
