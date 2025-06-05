package com.skeeper.minicode.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.skeeper.minicode.domain.contracts.other.providers.ISharedPreferencesProvider;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class SharedPreferencesProvider implements ISharedPreferencesProvider {
    private static final String PREFS_NAME = "AppPrefs";

    private final Context context;

    @Inject
    public SharedPreferencesProvider(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

}
