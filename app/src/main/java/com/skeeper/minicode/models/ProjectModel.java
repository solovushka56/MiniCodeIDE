package com.skeeper.minicode.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.skeeper.minicode.helpers.ProjectRectColorBinding;
import com.skeeper.minicode.helpers.ProjectRectColorBindings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProjectModel implements Parcelable {

    private int id;
    private String projectName;
    private String projectPath;
    private String[] tags;

    private String mainRectColorHex;
    private String innerRectColorHex;

    private String projectDescription;

    public ProjectModel(int id, String projectName, String projectPath) {
        this.id = id;
        this.projectName = projectName;
        this.projectPath = projectPath;
        initRandomPalette();
    }


    protected ProjectModel(Parcel in) {
        id = in.readInt();
        projectName = in.readString();
        projectPath = in.readString();
        tags = in.createStringArray();
        mainRectColorHex = in.readString();
        innerRectColorHex = in.readString();
        projectDescription = in.readString();
    }


    public static final Creator<ProjectModel> CREATOR = new Creator<ProjectModel>() {
        @Override
        public ProjectModel createFromParcel(Parcel in) {
            return new ProjectModel(in);
        }

        @Override
        public ProjectModel[] newArray(int size) {
            return new ProjectModel[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
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

    public String getProjectDescription() {
        return projectDescription;
    }
    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }




}
