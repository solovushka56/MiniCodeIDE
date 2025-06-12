package com.skeeper.minicode.domain.models;


public record ProjectModel(String name,
                           String description,
                           String path,
                           String[] tags,
                           String mainRectColorHex,
                           String innerRectColorHex) { }