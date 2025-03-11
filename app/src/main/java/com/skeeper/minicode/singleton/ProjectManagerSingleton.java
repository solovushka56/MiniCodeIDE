package com.skeeper.minicode.singleton;

import com.skeeper.minicode.models.ProjectModel;

public class ProjectManagerSingleton {

    private static ProjectManagerSingleton instance = null;
    private ProjectManagerSingleton() {}
    public static ProjectManagerSingleton getInstance() {
        if (instance == null) instance = new ProjectManagerSingleton();
        return instance;
    }



    public void createProject(ProjectModel modelToCreate) {


    }

    private void saveProjectModel(ProjectModel model) {

    }



    public void removeProject() {

    }

    public void editProjectName() {

    }


    public void editProjectDescription() {

    }




}
