package com.skeeper.minicode.presentation.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.skeeper.minicode.core.singleton.ProjectManager;
import com.skeeper.minicode.domain.contracts.repos.IProjectRepository;
import com.skeeper.minicode.domain.enums.TemplateType;
import com.skeeper.minicode.domain.models.ProjectModel;
import com.skeeper.minicode.domain.usecases.project.management.CreateTemplateUseCase;
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

    private final MutableLiveData<List<ProjectModel>> models =
            new MutableLiveData<>(new ArrayList<>());

    private final MutableLiveData<ProjectCreationState> projectCreationState =
            new MutableLiveData<>(ProjectCreationState.IDLE);

    private final IProjectRepository projectRepository; // TODO

    @Inject ProjectManager projectManager;

    @Inject
    public ProjectsViewModel(
            @NonNull Application application,
            ProjectManager projectManager,
            IProjectRepository projectRepository
    ) {
        super(application);
        this.projectManager = projectManager;
        this.projectRepository = projectRepository;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdownNow();
    }

    public MutableLiveData<List<ProjectModel>> getModels() {
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

                new CreateTemplateUseCase(projectRepository).execute(
                        getApplication(),
                        projectName,
                        tempType);

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
        var projectModel = new ProjectModel(
                projectName,
                projectDescription,
                projectDir.getPath(),
                tags,
                mainRectColorHex,
                innerRectColorHex
        );
        projectManager.generateMetadata(projectModel);
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