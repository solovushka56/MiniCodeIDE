package com.skeeper.minicode.data.di;

import android.content.Context;

import com.skeeper.minicode.data.local.FileDirectoryProvider;
import com.skeeper.minicode.domain.contracts.other.IFileDirectoryProvider;

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
    @Provides
    @Singleton
    static Context provideContext(@ApplicationContext Context context) {
        return context;
    }


//    @Provides
//    @Singleton
//    public FileDirectoryProvider provideFileDirectoryProvider(Context context) {
//        return new FileDirectoryProvider(context);
//    }
    @Binds
    public abstract IFileDirectoryProvider bindFileDirectoryProvider(FileDirectoryProvider provider);

}
