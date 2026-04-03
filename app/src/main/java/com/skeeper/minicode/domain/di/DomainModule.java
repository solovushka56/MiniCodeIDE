package com.skeeper.minicode.domain.di;

import com.skeeper.minicode.domain.contracts.repos.file.IFileContentRepository;
import com.skeeper.minicode.domain.contracts.repos.file.IFileStoreRepository;
import com.skeeper.minicode.domain.contracts.repos.project.IProjectRepository;
import com.skeeper.minicode.domain.models.ProjectModel;
import com.skeeper.minicode.domain.serialization.ISerializer;
import com.skeeper.minicode.domain.usecases.project.management.RenameProjectUseCase;
import com.skeeper.minicode.domain.usecases.project.management.metadata.GenerateMetadataUseCase;
import com.skeeper.minicode.domain.usecases.project.management.metadata.LoadMetadataUseCase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DomainModule {
    @Provides
    @Singleton
    static GenerateMetadataUseCase provideGenMetadataUseCase(
            IProjectRepository projectRepository,
            IFileStoreRepository fileStoreRepository,
            IFileContentRepository fileContentRepository,
            ISerializer<ProjectModel> serializer) {

        return new GenerateMetadataUseCase(projectRepository,
                fileStoreRepository, fileContentRepository, serializer);
    }


    @Provides
    @Singleton
    static LoadMetadataUseCase provideLoadMetadataUseCase(
            IProjectRepository projectRepository) {
        return new LoadMetadataUseCase(projectRepository);
    }
    @Provides
    @Singleton
    static RenameProjectUseCase provideRenameProjectUseCase(
            IProjectRepository projectRepository,
            IFileStoreRepository fileStoreRepository,
            LoadMetadataUseCase loadMetadataUseCase,
            GenerateMetadataUseCase generateMetadataUseCase) {

        return new RenameProjectUseCase(projectRepository, fileStoreRepository,
                loadMetadataUseCase, generateMetadataUseCase);
    }


}
