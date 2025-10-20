package com.skeeper.minicode.presentation.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.skeeper.minicode.core.singleton.ProjectManager;
import com.skeeper.minicode.domain.contracts.repos.project.IProjectRepository;
import com.skeeper.minicode.domain.exceptions.project.ProjectOperationException;
import com.skeeper.minicode.domain.models.ProjectModel;
import com.skeeper.minicode.domain.usecases.project.management.CreateTemplateUseCase;
import com.skeeper.minicode.domain.usecases.project.management.RenameProjectUseCase;
import com.skeeper.minicode.domain.usecases.project.management.metadata.GenerateMetadataUseCase;
import com.skeeper.minicode.domain.usecases.project.management.metadata.LoadMetadataUseCase;
import com.skeeper.minicode.utils.args.ProjectCreateArgs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ProjectsViewModel extends AndroidViewModel {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<List<ProjectModel>> models =
            new MutableLiveData<>(new ArrayList<>());

    private final MutableLiveData<ProjectCreationState> projectCreationState =
            new MutableLiveData<>(ProjectCreationState.IDLE);


    private final IProjectRepository projectRepository; // TODO

    private final RenameProjectUseCase renameProjectUseCase;
    private final GenerateMetadataUseCase generateMetadataUseCase;
    private final LoadMetadataUseCase loadMetadataUseCase;



    @Inject ProjectManager projectManager;

    @Inject
    public ProjectsViewModel(
            @NonNull Application application,
            ProjectManager projectManager,
            IProjectRepository projectRepository,
            GenerateMetadataUseCase generateMetadataUseCase,
            RenameProjectUseCase renameProjectUseCase
            ) {
        super(application);
        this.projectManager = projectManager;
        this.projectRepository = projectRepository;
        this.generateMetadataUseCase = generateMetadataUseCase;
        this.renameProjectUseCase = renameProjectUseCase;
        loadMetadataUseCase = new LoadMetadataUseCase(projectManager.getRepository());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdownNow();
    }


    public void loadModelsAsync() {
        executor.execute(() -> {
            var projects = projectManager.loadAllProjectModels();
            models.postValue(projects);

        });
    }



    public void createProjectAsync(ProjectCreateArgs args) {

        if (args.name() == null || args.name().trim().isEmpty()) {
            projectCreationState.setValue(ProjectCreationState.error(
                    "Project name cannot be empty!"));
            return;
        }

        projectCreationState.setValue(ProjectCreationState.CREATING);
        executor.execute(() -> {
            try {
                if (projectManager.projectExists(args.name())) {
                    projectCreationState.postValue(ProjectCreationState.error(
                            "Project with this name already exists"));
                    return;
                }

                File projectDir = createProjectFolder(args.name());
                if (projectDir == null) {
                    projectCreationState.postValue(ProjectCreationState.error(
                            "Error create project folder!"));
                    return;
                }

                generateMetadataUseCase.execute(new ProjectCreateArgs(
                                args.name(),
                                args.description(),
                                args.tags(),
                                args.templateType()));

                new CreateTemplateUseCase(projectRepository).execute(
                        getApplication(),
                        args.name(),
                        args.templateType());

                projectCreationState.postValue(ProjectCreationState.SUCCESS);
                loadModelsAsync();

            }
            catch (Exception e) {
                projectCreationState.postValue(ProjectCreationState.error(
                        "Error creating project: " + e.getMessage()));
            }
        });
    }

    public void renameProject(String oldName, String newName) throws ProjectOperationException {
        renameProjectUseCase.execute(oldName, newName);
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

    public MutableLiveData<List<ProjectModel>> getModels() {
        return models;
    }
    public MutableLiveData<ProjectCreationState> getProjectCreationState() {
        return projectCreationState;
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