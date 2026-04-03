package com.skeeper.minicode.presentation.viewmodels;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.skeeper.minicode.data.mappers.ProjectMapper;
import com.skeeper.minicode.domain.contracts.repos.project.IProjectRepository;
import com.skeeper.minicode.domain.exceptions.file.DomainIOException;
import com.skeeper.minicode.domain.models.ProjectModel;
import com.skeeper.minicode.domain.serialization.ISerializer;
import com.skeeper.minicode.domain.usecases.project.management.metadata.GenerateMetadataUseCase;
import com.skeeper.minicode.domain.usecases.project.management.metadata.LoadMetadataUseCase;
import com.skeeper.minicode.domain.usecases.project.tags.LoadTagsUseCase;
import com.skeeper.minicode.domain.usecases.project.tags.SaveTagsUseCase;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class TagViewModel extends ViewModel {

    private static final String THREAD_POOL_NAME = "tag-vm-pool";
    private final ExecutorService executor = Executors.newFixedThreadPool(
            2, r -> new Thread(r, THREAD_POOL_NAME));

    private final MutableLiveData<List<String>> tags = new MutableLiveData<>();
    private final LoadMetadataUseCase loadMetadataUseCase;
    private final GenerateMetadataUseCase generateMetadataUseCase;
    private final IProjectRepository projectRepository;
    private final SaveTagsUseCase saveTagsUseCase;
    private final LoadTagsUseCase loadTagsUseCase;
    private final ProjectMapper mapper;
    @Inject
    public TagViewModel(GenerateMetadataUseCase generateMetadataUseCase,
                        IProjectRepository projectRepository,
                        ISerializer<ProjectModel> projectModelISerializer, ProjectMapper mapper) {
        this.generateMetadataUseCase = generateMetadataUseCase;
        this.projectRepository = projectRepository;
        this.loadMetadataUseCase = new LoadMetadataUseCase(projectRepository);
        this.mapper = mapper;
        saveTagsUseCase = new SaveTagsUseCase(
                loadMetadataUseCase,
                generateMetadataUseCase,
                projectRepository, projectModelISerializer
        );
        loadTagsUseCase = new LoadTagsUseCase(loadMetadataUseCase);
    }



    public void loadProjectTags(String projectName) {
        executor.execute(() -> {
            var loadedTags = loadTagsUseCase.execute(projectName);
            tags.postValue(loadedTags);
        });
    }

    public void saveProjectTags(String[] updatedTags, String projectName) { // to repo or usecase
        executor.execute(() -> {
            var project = projectRepository.loadProject(projectName);
            var modifiedProject = new ProjectModel(
                    project.name(),
                    project.description(),
                    project.path(),
                    updatedTags,
                    project.mainRectColorHex(),
                    project.innerRectColorHex(),
                    project.mainFilePath());
            try {
                generateMetadataUseCase.execute(modifiedProject);
            }
            catch (DomainIOException e) {
                throw new RuntimeException(e);
            }
            tags.postValue(Arrays.asList(updatedTags));
        });
    }


    public MutableLiveData<List<String>> getTags() {
        return tags;
    }

}
