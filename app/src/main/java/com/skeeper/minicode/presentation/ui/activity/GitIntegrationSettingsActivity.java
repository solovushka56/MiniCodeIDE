package com.skeeper.minicode.presentation.ui.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.skeeper.minicode.R;
import com.skeeper.minicode.databinding.ActivityGitIntegrationSettingsBinding;

public class GitIntegrationSettingsActivity extends AppCompatActivity {

    ActivityGitIntegrationSettingsBinding binding;

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







        binding.buttonConfirm.setOnClickListener(v -> {
            onBackPressed();

        });
    }
}