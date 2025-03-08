package com.skeeper.minicode.models;

import com.skeeper.minicode.helpers.ProjectRectColorBinding;
import com.skeeper.minicode.helpers.ProjectRectColorBindings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProjectModel {

    private int id;
    private String projectName;
    private String projectPath;
    private String tags;


    private String mainRectColorHex;
    private String innerRectColorHex;

    public ProjectModel(int id, String projectName, String projectPath) {
        this.id = id;
        this.projectName = projectName;
        this.projectPath = projectPath;
        initRandomPalette();
    }

    private void initRandomPalette() {

        List<ProjectRectColorBinding> colorBindings = new ArrayList<>(
                ProjectRectColorBindings.bindingsList);
        Collections.shuffle(colorBindings);

        var randomizedPalette = colorBindings.get(0);

        setMainRectColorHex(randomizedPalette.mainRectColor);
        setInnerRectColorHex(randomizedPalette.innerRectColor);

    }


//    public ProjectRectColorBinding getRectPalette() {
//        return rectPalette;
//    }
//    public void setRectPalette(ProjectRectColorBinding rectPalette) {
//        this.rectPalette = rectPalette;
//    }


    public String getMainRectColorHex() {
        return mainRectColorHex;
    }
    public void setMainRectColorHex(String mainRectColorHex) {
        this.mainRectColorHex = mainRectColorHex;
    }


    public String getInnerRectColorHex() {
        return innerRectColorHex;
    }
    public void setInnerRectColorHex(String innerRectColorHex) {
        this.innerRectColorHex = innerRectColorHex;
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
