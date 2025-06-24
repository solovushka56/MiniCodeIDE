package com.skeeper.minicode.presentation.viewmodels;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.skeeper.minicode.core.singleton.ProjectManager;
import com.skeeper.minicode.data.mappers.ProjectMapper;
import com.skeeper.minicode.domain.contracts.repos.IProjectRepository;
import com.skeeper.minicode.domain.exceptions.file.DomainIOException;
import com.skeeper.minicode.domain.usecases.project.management.metadata.GenerateMetadataUseCase;
import com.skeeper.minicode.domain.usecases.project.management.metadata.LoadMetadataUseCase;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

public class TagViewModel extends ViewModel {

    private static final String THREAD_POOL_NAME = "tag-vm-pool";
    private final ExecutorService executor = Executors.newFixedThreadPool(
            2, r -> new Thread(r, THREAD_POOL_NAME));

    private final MutableLiveData<List<String>> tags = new MutableLiveData<>();
    private final LoadMetadataUseCase loadMetadataUseCase;
    private final GenerateMetadataUseCase generateMetadataUseCase;
    private final IProjectRepository projectRepository;

    @Inject
    public TagViewModel(GenerateMetadataUseCase generateMetadataUseCase,
                        IProjectRepository projectRepository) {
        this.generateMetadataUseCase = generateMetadataUseCase;
        this.projectRepository = projectRepository;
        this.loadMetadataUseCase = new LoadMetadataUseCase(projectRepository);
    }

    public MutableLiveData<List<String>> getTags() {
        return tags;
    }


    public void loadProjectTags(String projectName) {
        executor.execute(() -> {
            var proj = loadMetadataUseCase.execute(projectName);
            tags.postValue(Arrays.asList(proj.tags()));
        });
    }

    public void saveProjectTags(String[] newTags, String projectName) throws DomainIOException { // to repo or usecase
        var mapper = new ProjectMapper();
        var dataModel = mapper.mapFromDomain(projectRepository.loadProject(projectName));
        dataModel.setTags(newTags);
        generateMetadataUseCase.execute(mapper.mapToDomain(dataModel));
    }

}
