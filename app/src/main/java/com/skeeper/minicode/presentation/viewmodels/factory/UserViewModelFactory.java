package com.skeeper.minicode.presentation.viewmodels.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.skeeper.minicode.data.repos.UserRepository;
import com.skeeper.minicode.presentation.viewmodels.UserViewModel;

public class UserViewModelFactory implements ViewModelProvider.Factory {
//    private final UserRepository userRepository;
//
//    public UserViewModelFactory(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @NonNull
//    @Override
//    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
//        if (modelClass.isAssignableFrom(UserViewModel.class)) {
//            return (T) new UserViewModel(userRepository);
//        }
//        throw new IllegalArgumentException("Unknown ViewModel class");
//    }
}