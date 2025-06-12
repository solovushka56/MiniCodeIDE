package com.skeeper.minicode.presentation.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.skeeper.minicode.R;
import com.skeeper.minicode.core.constants.PrefsKeys;
import com.skeeper.minicode.databinding.ActivityGitIntegrationSettingsBinding;
import com.skeeper.minicode.presentation.viewmodels.SecurePrefViewModel;
import com.skeeper.minicode.utils.helpers.SystemBarsHelper;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class GitIntegrationSettingsActivity extends AppCompatActivity {

    ActivityGitIntegrationSettingsBinding binding;
    SecurePrefViewModel securePrefViewModel;

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
        getWindow().setNavigationBarColor(getResources().getColor(R.color.violet));
        securePrefViewModel = new ViewModelProvider(this).get(SecurePrefViewModel.class);


        binding.buttonConfirm.setOnClickListener(v -> {
            String username = binding.gitUsernameTextEdit.getText().toString();
            String pass = binding.gitPatEditText.getText().toString();

            if (username.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Fill in in the missing fields!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            securePrefViewModel.saveSecureData(PrefsKeys.USERNAME, username);
            securePrefViewModel.saveSecureData(PrefsKeys.TOKEN, pass);
            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();

        });



    }
}