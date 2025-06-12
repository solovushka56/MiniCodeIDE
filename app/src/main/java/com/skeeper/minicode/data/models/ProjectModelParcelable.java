package com.skeeper.minicode.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.skeeper.minicode.utils.helpers.ProjectRectColorBinding;
import com.skeeper.minicode.utils.helpers.ProjectRectColorBindings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ProjectModelParcelable implements Parcelable {

    private String projectName;
    private String projectDescription;
    private String projectPath;
    private String[] tags;
    private String mainRectColorHex;
    private String innerRectColorHex;

    public ProjectModelParcelable(String projectName, String projectPath) {
        this.projectName = projectName;
        this.projectPath = projectPath;
        initRandomPalette();
    }

    public ProjectModelParcelable(String projectName,
                                  String projectDescription,
                                  String projectPath,
                                  String[] tags,
                                  String mainRectColorHex,
                                  String innerRectColorHex) {

        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.projectPath = projectPath;
        this.tags = tags;
        this.mainRectColorHex = mainRectColorHex;
        this.innerRectColorHex = innerRectColorHex;
    }

    protected ProjectModelParcelable(Parcel in) {
        projectName = in.readString();
        projectPath = in.readString();
        tags = in.createStringArray();
        mainRectColorHex = in.readString();
        innerRectColorHex = in.readString();
        projectDescription = in.readString();
    }

    public static final Creator<ProjectModelParcelable> CREATOR = new Creator<>() {
        @Override
        public ProjectModelParcelable createFromParcel(Parcel in) {
            return new ProjectModelParcelable(in);
        }

        @Override
        public ProjectModelParcelable[] newArray(int size) {
            return new ProjectModelParcelable[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(projectName);
        dest.writeString(projectPath);
        dest.writeStringArray(tags);
        dest.writeString(mainRectColorHex);
        dest.writeString(innerRectColorHex);
        dest.writeString(projectDescription);
    }




    //todo: move method to other entity
    private void initRandomPalette() {
        List<ProjectRectColorBinding> colorBindings = new ArrayList<>(
                ProjectRectColorBindings.bindingsList);
        Collections.shuffle(colorBindings);

        var randomizedPalette = colorBindings.get(0);

        setMainRectColorHex(randomizedPalette.mainRectColor);
        setInnerRectColorHex(randomizedPalette.innerRectColor);
    }

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

    public String[] getTags() {
        return tags;
    }
    public void setTags(String[] tags) {
        this.tags = tags;
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

    public String getProjectDescription() {
        return projectDescription;
    }
    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }


}
