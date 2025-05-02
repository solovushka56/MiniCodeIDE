package com.skeeper.minicode.domain.models;

import java.util.List;

public class LangModel {
    List<String> keywords;
    List<String> operators;
    List<String> objectTypes;
    List<String> primitiveTypes;
    List<String> attributes;
    String constructionBodyDefinition;
    String defaultCodeTemplate; // todo
    String defaultClassTemplate; // todo

    public LangModel(List<String> keywords, List<String> operators,
                     List<String> objectTypes, List<String> primitiveTypes,
                     List<String> attributes, String constructionBodyDefinition)
    {
        this.keywords = keywords;
        this.operators = operators;
        this.objectTypes = objectTypes;
        this.primitiveTypes = primitiveTypes;
        this.attributes = attributes;
        this.constructionBodyDefinition = constructionBodyDefinition;
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

    public String getConstructionBodyDefinition() {
        return constructionBodyDefinition;
    }
}
