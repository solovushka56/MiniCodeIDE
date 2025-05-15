package com.skeeper.minicode.data.local;

import android.content.Context;

import com.skeeper.minicode.domain.contracts.other.IFileDirectoryProvider;

import java.io.File;

import javax.inject.Inject;

public class FileDirectoryProvider implements IFileDirectoryProvider {
    private final Context context;

    @Inject
    public FileDirectoryProvider(Context context) {
        this.context = context;
    }


    @Override
    public File getFilesDir() {
        return context.getFilesDir();
    }
}
