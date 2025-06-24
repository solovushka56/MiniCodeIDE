package com.skeeper.minicode.data.di;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.skeeper.minicode.data.local.FileDirectoryProvider;
import com.skeeper.minicode.data.local.ResourcesProvider;
import com.skeeper.minicode.data.local.SharedPreferencesProvider;
import com.skeeper.minicode.data.operations.ProjectOperations;
import com.skeeper.minicode.data.parsers.MetadataParser;
import com.skeeper.minicode.data.repos.project.ProjectRepository;
import com.skeeper.minicode.data.repos.UserRepository;
import com.skeeper.minicode.data.repos.filerepos.FileContentRepository;
import com.skeeper.minicode.data.repos.filerepos.FileStoreRepository;
import com.skeeper.minicode.domain.contracts.operations.IProjectOperations;
import com.skeeper.minicode.domain.contracts.other.providers.IFileDirectoryProvider;
import com.skeeper.minicode.domain.contracts.other.providers.IResourcesProvider;
import com.skeeper.minicode.domain.contracts.repos.file.IFileContentRepository;
import com.skeeper.minicode.domain.contracts.repos.project.IProjectRepository;
import com.skeeper.minicode.domain.contracts.repos.file.IFileStoreRepository;
import com.skeeper.minicode.domain.models.ProjectModel;
import com.skeeper.minicode.domain.serialization.ISerializer;
import com.skeeper.minicode.domain.usecases.project.management.RenameProjectUseCase;
import com.skeeper.minicode.domain.usecases.project.management.metadata.GenerateMetadataUseCase;
import com.skeeper.minicode.domain.usecases.project.management.metadata.LoadMetadataUseCase;

import java.io.File;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class DataModule {
    private static String TAG = "DI_MODULE";

    @Binds
    public abstract IFileDirectoryProvider bindFileDirectoryProvider(FileDirectoryProvider provider);


    @Provides
    @Singleton
    static Context provideContext(@ApplicationContext Context context) {
        return context;
    }

    @Provides
    @Singleton
    static UserRepository provideUserManager(@ApplicationContext Context context) {
        return new UserRepository(context);
    }

    @Provides
    @Singleton
    static FileDirectoryProvider provideFileDirectoryProvider(Context context) {
        return new FileDirectoryProvider(context);
    }

    @Provides
    @Singleton
    static SharedPreferencesProvider provideSharedPreferencesProvider(Context context) {
        return new SharedPreferencesProvider(context);
    }


    @Provides
    @Singleton
    static IResourcesProvider provideIResourcesProvider(Context context) {
        return new ResourcesProvider(context);
    }


    @Provides
    @Singleton
    static File provideStorageDir(@ApplicationContext Context context) {
        return context.getFilesDir();
    }

    @Provides
    @Singleton
    static Gson provideGson() {
        Log.i(TAG, "Gson provided");
        return new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }


    @Provides
    @Singleton
    static IProjectOperations provideProjectOperations(
            IFileDirectoryProvider fileDirectoryProvider, Gson gson) {
        return new ProjectOperations(fileDirectoryProvider, gson);
    }

    @Provides
    @Singleton
    static IProjectRepository provideProjectRepository(
            IProjectOperations projectOperations, ISerializer<ProjectModel> serializer, Gson gson) {
        return new ProjectRepository(projectOperations, serializer, gson);
    }

    @Provides
    @Singleton
    static IFileStoreRepository provideFileStoreRepository() {
        return new FileStoreRepository();
    }

    @Provides
    @Singleton
    static IFileContentRepository provideFileContentRepository() {
        return new FileContentRepository();
    }


    @Provides
    @Singleton
    static ISerializer<ProjectModel> provideProjectModelSerializer(Gson gson) {
        return new MetadataParser(gson);
    }

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
