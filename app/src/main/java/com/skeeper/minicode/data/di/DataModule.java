package com.skeeper.minicode.data.di;

import android.content.Context;

import com.skeeper.minicode.data.local.FileDirectoryProvider;
import com.skeeper.minicode.data.local.ResourcesProvider;
import com.skeeper.minicode.data.local.SharedPreferencesProvider;
import com.skeeper.minicode.data.repos.UserRepository;
import com.skeeper.minicode.domain.contracts.other.providers.IFileDirectoryProvider;
import com.skeeper.minicode.domain.contracts.other.providers.IResourcesProvider;

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


//    @Provides
//    @Singleton
//    static ResourcesProvider provideResourcesProvider(Context context) {
//        return new ResourcesProvider(context);
//    }
//    @Provides
//    @Singleton
//    static ResourcesProvider provideResourcesProvider(Context context) {
//        return new ResourcesProvider(context);
//    }

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

}
