package com.skeeper.minicode.presentation.viewmodels;

import android.app.Application;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.skeeper.minicode.core.constants.PrefsKeys;
import com.skeeper.minicode.data.repos.UserRepository;
import com.skeeper.minicode.utils.helpers.SecurePreferences;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

public class SecurePrefViewModel extends AndroidViewModel {

    private final SecurePreferences securePrefs;
    private final MutableLiveData<Pair<String, String>> secureData = new MutableLiveData<>();

    private final MutableLiveData<String> usernameLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> tokenLiveData = new MutableLiveData<>();

    public SecurePrefViewModel(@NonNull Application application) {
        super(application);
        securePrefs = new SecurePreferences(application, "secure_prefs");
    }

    public void saveSecureData(String key, String value) {
        securePrefs.putString(key, value);
    }

    public void loadSecureData(String key) {
        String value = securePrefs.getString(key);
        secureData.postValue(new Pair<>(key, value));
    }

    public LiveData<Pair<String, String>> getSecureData() {
        return secureData;
    }


    public void loadUsername() {
        String value = securePrefs.getString(PrefsKeys.USERNAME);
        usernameLiveData.postValue(value);
    }

    public void loadToken() {
        String value = securePrefs.getString(PrefsKeys.TOKEN);
        tokenLiveData.postValue(value);
    }



    public MutableLiveData<String> getToken() {
        return tokenLiveData;
    }

    public MutableLiveData<String> getUsername() {
        return usernameLiveData;
    }
}

