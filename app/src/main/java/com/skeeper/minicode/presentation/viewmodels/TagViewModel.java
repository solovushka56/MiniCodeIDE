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
    @Inject
    public TagViewModel(GenerateMetadataUseCase generateMetadataUseCase,
                        IProjectRepository projectRepository,
                        ISerializer<ProjectModel> projectModelISerializer) {
        this.generateMetadataUseCase = generateMetadataUseCase;
        this.projectRepository = projectRepository;
        this.loadMetadataUseCase = new LoadMetadataUseCase(projectRepository);
        saveTagsUseCase = new SaveTagsUseCase(
                loadMetadataUseCase,
                generateMetadataUseCase,
                projectRepository, projectModelISerializer
                );
        loadTagsUseCase = new LoadTagsUseCase(loadMetadataUseCase);
    }

    public MutableLiveData<List<String>> getTags() {
        return tags;
    }


    public void loadProjectTags(String projectName) {
        executor.execute(() -> {
            var loadedTags = loadTagsUseCase.execute(projectName);
            tags.postValue(loadedTags);
        });
    }

    public void saveProjectTags(String[] newTags, String projectName) { // to repo or usecase
        var mapper = new ProjectMapper();
        var dataModel = mapper.mapFromDomain(projectRepository.loadProject(projectName));
        dataModel.setTags(newTags);
        try {
            generateMetadataUseCase.execute(mapper.mapToDomain(dataModel));
        } catch (DomainIOException e) {
            throw new RuntimeException(e);
        }
        tags.setValue(Arrays.asList(newTags));


//        executor.execute(() -> {
//            try {
//                saveTagsUseCase.execute(newTags, projectName);
//                tags.postValue(Arrays.asList(newTags));
//            } catch (DomainIOException e) {
//                Log.e("TAGS_SAVING", "Failed saver tags in tag vm");
//            }
//
//        });

    }

}
