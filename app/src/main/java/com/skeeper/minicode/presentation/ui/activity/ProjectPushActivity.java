package com.skeeper.minicode.presentation.ui.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.skeeper.minicode.R;
import com.skeeper.minicode.core.singleton.ProjectManager;
import com.skeeper.minicode.databinding.ActivityProjectPushBinding;
import com.skeeper.minicode.presentation.viewmodels.GitPushViewModel;
import com.skeeper.minicode.presentation.viewmodels.SecurePrefViewModel;
import com.skeeper.minicode.presentation.viewmodels.SharedPrefsViewModel;
import com.skeeper.minicode.utils.helpers.NetworkConnectionHelper;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class ProjectPushActivity extends AppCompatActivity {

    @Inject ProjectManager projectManager;
    ActivityProjectPushBinding binding;
    GitPushViewModel gitPushViewModel;
    SecurePrefViewModel securePrefViewModel;
    SharedPrefsViewModel sharedPrefsViewModel;

    String projectName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityProjectPushBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWindow().setNavigationBarColor(getResources().getColor(R.color.violet));


        projectName = getIntent().getStringExtra("PROJECT_NAME");

        gitPushViewModel = new ViewModelProvider(this).get(GitPushViewModel.class);
        securePrefViewModel = new ViewModelProvider(this).get(SecurePrefViewModel.class);

        securePrefViewModel.getUsername().observe(this, username -> {
            if (username != null) gitPushViewModel.setUsername(username);
        });
        securePrefViewModel.getToken().observe(this, token -> {
            if (token != null) gitPushViewModel.setToken(token);
        });

        securePrefViewModel.loadUsername();
        securePrefViewModel.loadToken();

        binding.buttonPushAndCommit.setOnClickListener(v -> {
            if (!NetworkConnectionHelper.hasConnection(this)) {
                Toast.makeText(this, "No network connection!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (gitPushViewModel.username == null || gitPushViewModel.token == null) {
                Toast.makeText(this, "Credentials not loaded!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            String pushUrl = binding.pushUriEditText.getText().toString();
            String commitName = binding.commitEditText.getText().toString();
            String commitMessage = binding.commitMessageEditText.getText().toString();

            if (pushUrl.isEmpty() || commitName.isEmpty()) {
                Toast.makeText(this, "Fill in the missing text fields!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            String fullMessage;
            if (!commitMessage.isEmpty())
                fullMessage = commitName + "\n\n" + commitMessage;
            else
                fullMessage = commitName;

            gitPushViewModel.createCommitAndPush(
                    projectManager.getProjectDir(projectName),
                    fullMessage);
            disableUI();
        });


        gitPushViewModel.getPushResult().observe(this, result -> {
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void disableUI() {
        binding.buttonPushAndCommit.setEnabled(false);
        binding.pushUriEditText.setEnabled(false);
        binding.commitEditText.setEnabled(false);
        binding.commitMessageEditText.setEnabled(false);
        binding.buttonPushAndCommit.setAlpha(0.5f);
    }




}

