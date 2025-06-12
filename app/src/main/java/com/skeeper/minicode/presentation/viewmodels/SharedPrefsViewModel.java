package com.skeeper.minicode.presentation.viewmodels;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.skeeper.minicode.core.constants.PrefsKeys;

public class SharedPrefsViewModel extends AndroidViewModel {

    private final SharedPreferences sharedPreferences;
    private final MutableLiveData<Boolean> registered = new MutableLiveData<>();

    public SharedPrefsViewModel(@NonNull Application application) {
        super(application);
        sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(application);
        loadInitialValues();
        sharedPreferences.registerOnSharedPreferenceChangeListener(
                prefChangeListener);
    }

    private void loadInitialValues() {
        registered.setValue(sharedPreferences.getBoolean("registered", false));
    }

    private final SharedPreferences.OnSharedPreferenceChangeListener prefChangeListener =
            (prefs, key) -> {
                if (PrefsKeys.REGISTERED.equals(key)) {
                    registered.postValue(prefs.getBoolean(key, false));
                }
            };


    public LiveData<Boolean> getIsRegistered() {
        return registered;
    }

    public void setIsRegistered(boolean value) {
        sharedPreferences.edit()
                .putBoolean(PrefsKeys.REGISTERED, value)
                .apply();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(prefChangeListener);
    }
}