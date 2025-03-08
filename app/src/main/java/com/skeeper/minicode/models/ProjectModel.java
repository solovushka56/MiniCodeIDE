package com.skeeper.minicode.models;

public class ProjectModel {

    private int id;
    private String projectName;
    private String projectPath;
    private String tags;

//    private String mainRectColorHex;
//    private String filepathRectColorHex;


    public ProjectModel(int id, String projectName, String projectPath) {
        this.id = id;
        this.projectName = projectName;
        this.projectPath = projectPath;
//        this.tags = tags;
    }


    public String getTags() {
        return tags;
    }
    public void setTags(String tags) {
        this.tags = tags;
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
