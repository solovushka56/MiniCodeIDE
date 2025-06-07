package com.skeeper.minicode.domain.models;

import com.skeeper.minicode.domain.enums.ExtensionType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileItem {
    private String name;
    private boolean isDirectory;
    private boolean isExpanded;
    private int level;
    private List<FileItem> children;
    private File directory;

    public FileItem(File directory, String name, boolean isDirectory, int level) {
        this.name = name;
        this.isDirectory = isDirectory;
        this.level = level;
        this.children = new ArrayList<>();
        this.isExpanded = false;
        this.directory = directory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<FileItem> getChildren() {
        return children;
    }

    public void setChildren(List<FileItem> children) {
        this.children = children;
    }

    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    public String getPath() {
        return directory != null
                ? new File(directory, name).getAbsolutePath()
                : name;
    }

}