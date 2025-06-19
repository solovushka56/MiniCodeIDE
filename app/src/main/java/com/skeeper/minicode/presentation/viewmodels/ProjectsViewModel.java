package com.skeeper.minicode.presentation.viewmodels;

import static dagger.hilt.android.internal.Contexts.getApplication;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.skeeper.minicode.R;
import com.skeeper.minicode.core.singleton.ProjectManager;
import com.skeeper.minicode.data.models.ProjectModelParcelable;
import com.skeeper.minicode.data.repos.ProjectRepository;
import com.skeeper.minicode.domain.enums.TemplateType;
import com.skeeper.minicode.utils.FileUtils;
import com.skeeper.minicode.utils.helpers.ProjectRectColorBinding;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ProjectsViewModel extends AndroidViewModel {

    private static final String THREAD_POOL_NAME = "projects-vm-pool";
    private final ExecutorService executor = Executors.newFixedThreadPool(
            2,
            r -> new Thread(r, THREAD_POOL_NAME)
    );

    private final MutableLiveData<List<ProjectModelParcelable>> models =
            new MutableLiveData<>(new ArrayList<>());

    private final MutableLiveData<ProjectCreationState> projectCreationState =
            new MutableLiveData<>(ProjectCreationState.IDLE);

//    private final ProjectRepository projectRepository = new ProjectRepository(); // TODO

    @Inject ProjectManager projectManager;

    @Inject
    public ProjectsViewModel(
            @NonNull Application application,
            ProjectManager projectManager
    ) {
        super(application);
        this.projectManager = projectManager;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdownNow();
    }

    public MutableLiveData<List<ProjectModelParcelable>> getModels() {
        return models;
    }
    public MutableLiveData<ProjectCreationState> getProjectCreationState() {
        return projectCreationState;
    }

    public void loadModelsAsync() {
        executor.execute(() -> {
            var projects = projectManager.loadAllProjectModels();
            models.postValue(projects);

        });
    }

    public void createProjectAsync(String projectName,
                                   String projectDescription,
                                   String[] tags, TemplateType tempType) {

        if (projectName == null || projectName.trim().isEmpty()) {
            projectCreationState.setValue(ProjectCreationState.error(
                    "Project name cannot be empty!"));
            return;
        }

        projectCreationState.setValue(ProjectCreationState.CREATING);
        executor.execute(() -> {
            try {

                if (projectManager.projectExists(projectName)) {
                    projectCreationState.postValue(ProjectCreationState.error(
                            "Project with this name already exists"));
                    return;
                }

                File projectDir = createProjectFolder(projectName);
                if (projectDir == null) {
                    projectCreationState.postValue(ProjectCreationState.error(
                            "Error create project folder!"));
                    return;
                }

                ProjectRectColorBinding rectPalette = new ProjectRectColorBinding();
                generateProjectMetadata(
                        projectDir,
                        projectName,
                        projectDescription,
                        tags,
                        rectPalette.getMainRectColor(),
                        rectPalette.getInnerRectColor()
                );
                createTemplate(projectName, tempType);
                projectCreationState.postValue(ProjectCreationState.SUCCESS);

                loadModelsAsync();

            } catch (Exception e) {
                projectCreationState.postValue(ProjectCreationState.error(
                        "Error creating project: " + e.getMessage()));
            }

        });
    }



    private @Nullable File createProjectFolder(String name) {
        if (!isCorrectProjectName(name))
            return null;
        File projectsFolder = projectManager.getProjectsStoreFolder();
        if (projectsFolder == null || !projectsFolder.exists()) {
            return null;
        }
        File projectDir = new File(projectsFolder, name);
        if (projectDir.exists())
            return null;


        return projectDir.mkdirs() ? projectDir : null;
    }

    private boolean isCorrectProjectName(String name) {
        return name != null &&
                !name.trim().isEmpty() &&
                !name.contains(File.separator) &&
                !name.contains("/") &&
                !name.contains("\\");
    }

    private void generateProjectMetadata(File projectDir,
                                         String projectName,
                                         String projectDescription,
                                         String[] tags,
                                         String mainRectColorHex,
                                         String innerRectColorHex) throws IOException {
        ProjectModelParcelable projectModel = new ProjectModelParcelable(
                projectName,
                projectDescription,
                projectDir.getPath(),
                tags,
                mainRectColorHex,
                innerRectColorHex
        );
        projectManager.generateMetadata(projectDir, projectModel);
    }
    
    private void createTemplate(String projName, TemplateType tempType) {
        if (tempType == TemplateType.NONE) return;

        String fullFileName = (tempType == TemplateType.JAVA)
                ? "Main" + ".java"
                : "main" + ".py";
        try {
            projectManager.saveFile(projectManager.getProjectDir(projName), fullFileName,
                    getTemplateContent(tempType));
        } catch (IOException e) {
        }
    }

    private String getTemplateContent(TemplateType type) {
        int resId;
        if (type == TemplateType.JAVA) {
            resId = R.string.java_template;
        } else {
            resId = R.string.python_template;
        }
        return getApplication()
                .getString(resId)
                .replace("\t", "   ");
    }

    public enum ProjectCreationState {
        IDLE,
        LOADING,
        CREATING,
        SUCCESS,
        ERROR;

        private String errorMessage;

        public String getErrorMessage() {
            return errorMessage;
        }

        public boolean isError() {
            return this == ERROR;
        }

        public static ProjectCreationState error(String message) {
            ProjectCreationState state = ERROR;
            state.errorMessage = message;
            return state;
        }
    }





}