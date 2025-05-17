package com.skeeper.minicode.presentation.viewmodels;

//import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.ViewModel;

import com.skeeper.minicode.data.repos.UserRepository;
import com.skeeper.minicode.domain.models.UserModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class UserViewModel extends ViewModel {
    private UserRepository userRepository;

    @Inject
    public UserViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void saveCredentials(String username, String pass, String email) {
        userRepository.saveUserCredentials(username, pass, email);
    }

    public boolean verifyCredentials(String username, String pass, String email) { // pass is true or not
        return userRepository.verifyUserCredentials(username, pass, email);
    }
}
