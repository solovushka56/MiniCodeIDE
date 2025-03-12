package com.skeeper.minicode.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProjectRectColorBinding {
    public String mainRectColor;
    public String innerRectColor;


    public ProjectRectColorBinding(String mainRectColor, String innerRectColor) {
        this.mainRectColor = mainRectColor;
        this.innerRectColor = innerRectColor;
    }

    public ProjectRectColorBinding() {
        setRandomPalette();
    }

    public void setRandomPalette() {
        List<ProjectRectColorBinding> colorBindings = new ArrayList<>(
                ProjectRectColorBindings.bindingsList);
        Collections.shuffle(colorBindings);

        var randomizedPalette = colorBindings.get(0);

        setMainRectColor(randomizedPalette.mainRectColor);
        setInnerRectColor(randomizedPalette.innerRectColor);
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
