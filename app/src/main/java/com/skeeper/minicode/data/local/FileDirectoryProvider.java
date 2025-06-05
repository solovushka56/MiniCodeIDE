package com.skeeper.minicode.data.local;

import android.content.Context;

import com.skeeper.minicode.domain.contracts.other.providers.IFileDirectoryProvider;

import java.io.File;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class FileDirectoryProvider implements IFileDirectoryProvider {
    private final Context context;

    @Inject
    public FileDirectoryProvider(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public File getFilesDir() {
        return context.getFilesDir();
    }
}
