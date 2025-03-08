package com.skeeper.minicode.helpers;

public class ProjectRectColorBinding {
    public String mainRectColor;
    public String innerRectColor;


    public ProjectRectColorBinding(String mainRectColor, String innerRectColor) {
        this.mainRectColor = mainRectColor;
        this.innerRectColor = innerRectColor;
    }

    public String getMainRectColor() {
        return mainRectColor;
    }
    public void setMainRectColor(String mainRectColor) {
        this.mainRectColor = mainRectColor;
    }

    public String getInnerRectColor() {
        return innerRectColor;
    }
    public void setInnerRectColor(String innerRectColor) {
        this.innerRectColor = innerRectColor;
    }
}
