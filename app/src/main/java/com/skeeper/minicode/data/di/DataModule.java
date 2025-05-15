package com.skeeper.minicode.data.di;

import android.content.Context;

import com.skeeper.minicode.data.local.FileDirectoryProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DataModule {

    @Provides
    @Singleton
    public FileDirectoryProvider provideFileDirectoryProvider(Context context) {
        return new FileDirectoryProvider(context);
    }
}
