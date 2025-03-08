package com.skeeper.minicode.models;

public class ProjectModel {

    private int id;
    private String projectName;
    private String projectPath;
    private String tags;

    private String mainRectColorHex;
    private String filepathRectColorHex;


    public ProjectModel(int id, String projectName, String projectPath, String tags, String mainRectColorHex, String filepathRectColorHex) {
        this.id = id;
        this.projectName = projectName;
        this.projectPath = projectPath;
//        this.tags = tags;
        this.mainRectColorHex = mainRectColorHex;
        this.filepathRectColorHex = filepathRectColorHex;
    }


    public String getTags() {
        return tags;
    }
    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getMainRectColorHex() {
        return mainRectColorHex;
    }
    public void setMainRectColorHex(String mainRectColorHex) {
        this.mainRectColorHex = mainRectColorHex;
    }

    public String getFilepathRectColorHex() {
        return filepathRectColorHex;
    }
    public void setFilepathRectColorHex(String filepathRectColorHex) {
        this.filepathRectColorHex = filepathRectColorHex;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }


    public String getProjectName() {
        return projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }


    public String getProjectPath() {
        return projectPath;
    }
    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }


}
