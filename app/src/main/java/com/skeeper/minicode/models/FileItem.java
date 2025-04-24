package com.skeeper.minicode.models;

import java.util.ArrayList;
import java.util.List;

public class FileItem {
    private String name;
    private boolean isDirectory;
    private boolean isExpanded;
    private int level;
    private List<FileItem> children;

    public FileItem(String name, boolean isDirectory, int level) {
        this.name = name;
        this.isDirectory = isDirectory;
        this.level = level;
        this.children = new ArrayList<>();
        this.isExpanded = false;
    }

    public boolean isDirectory() { return isDirectory; }
    public String getName() { return name; }
    public int getLevel() { return level; }
    public List<FileItem> getChildren() { return children; }
    public boolean isExpanded() { return isExpanded; }
    public void setExpanded(boolean expanded) { isExpanded = expanded; }
}