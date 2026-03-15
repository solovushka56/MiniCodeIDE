package com.skeeper.minicode.data.sources.preferences;

import android.content.SharedPreferences;

import com.skeeper.minicode.core.constants.ProjectConstants;
import com.skeeper.minicode.domain.contracts.other.providers.ISharedPreferencesProvider;

import javax.inject.Inject;

public class UserPreferencesProvider {

    private final SharedPreferences prefs;

    private static final String KEY_IS_FIRST_RUN = "isFirstRun";
    private static final String SERVER_URL = "customServerUrl";

    @Inject
    public UserPreferencesProvider(ISharedPreferencesProvider sharedPreferencesProvider) {
        this.prefs = sharedPreferencesProvider.getSharedPreferences();
    }

    public boolean isFirstAppRun() {
        return prefs.getBoolean(KEY_IS_FIRST_RUN, true);
    }

    public void setFirstAppRun(boolean value) {
        prefs.edit().putBoolean(KEY_IS_FIRST_RUN, value).apply();
    }

    public String getSavedServerIp() {
        return prefs.getString(SERVER_URL, ProjectConstants.SERVER_URL);
    }

    public void setSavedServerIp(String value) {
        prefs.edit().putString(SERVER_URL, value).apply();
    }
}