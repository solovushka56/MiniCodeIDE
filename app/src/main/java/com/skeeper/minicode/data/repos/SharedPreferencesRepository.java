package com.skeeper.minicode.data.repos;

import android.content.SharedPreferences;

import com.skeeper.minicode.domain.contracts.other.providers.ISharedPreferencesProvider;
import com.skeeper.minicode.domain.contracts.repos.IPreferencesRepository;

import javax.inject.Inject;

public class SharedPreferencesRepository implements IPreferencesRepository {

    private static final String PREFS_NAME = "AppPrefs";
    private static final String KEY_FIRST_RUN = "isFirstRun";

    private final SharedPreferences prefs;

    @Inject
    public SharedPreferencesRepository(ISharedPreferencesProvider sharedPreferencesProvider) {
        prefs = sharedPreferencesProvider.getSharedPreferences();
    }


    @Override
    public boolean getIsFirstAppRun() {
        return prefs.getBoolean(KEY_FIRST_RUN, true);
    }

    @Override
    public void setIsFirstAppRun(boolean value) {
        prefs.edit().putBoolean(KEY_FIRST_RUN, value).apply();
    }
}
