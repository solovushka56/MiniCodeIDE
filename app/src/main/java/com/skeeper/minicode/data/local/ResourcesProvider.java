package com.skeeper.minicode.data.local;

import android.content.Context;
import android.content.res.Resources;

import com.skeeper.minicode.domain.contracts.other.providers.IResourcesProvider;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class ResourcesProvider implements IResourcesProvider {
    private final Context context;

    @Inject
    public ResourcesProvider(@ApplicationContext Context context) {
        this.context = context;
    }


    @Override
    public Resources getResources() {
        return context.getResources();
    }
}
