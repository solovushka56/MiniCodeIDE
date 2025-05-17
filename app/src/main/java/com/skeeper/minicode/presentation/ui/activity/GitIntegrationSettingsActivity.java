package com.skeeper.minicode.presentation.ui.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.skeeper.minicode.R;
import com.skeeper.minicode.databinding.ActivityGitIntegrationSettingsBinding;
import com.skeeper.minicode.presentation.viewmodels.UserViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class GitIntegrationSettingsActivity extends AppCompatActivity {

    ActivityGitIntegrationSettingsBinding binding;
    UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityGitIntegrationSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);


        binding.buttonConfirm.setOnClickListener(v -> {
            String username = binding.gitUsernameTextEdit.getText().toString();
            String pass = binding.gitPatEditText.getText().toString();
            String email = binding.userEmailEditText.getText().toString();
            userViewModel.saveCredentials(username, pass, email);
            onBackPressed();

        });
    }
}