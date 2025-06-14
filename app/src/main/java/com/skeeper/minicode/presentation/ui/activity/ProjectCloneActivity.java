package com.skeeper.minicode.presentation.ui.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.skeeper.minicode.R;
import com.skeeper.minicode.databinding.ActivityProjectCloneBinding;
import com.skeeper.minicode.presentation.viewmodels.GitViewModel;
import com.skeeper.minicode.core.singleton.ProjectManager;
import com.skeeper.minicode.presentation.viewmodels.SecurePrefViewModel;
import com.skeeper.minicode.utils.helpers.NetworkConnectionHelper;

import java.io.File;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProjectCloneActivity extends AppCompatActivity {
    ActivityProjectCloneBinding binding;

    @Inject
    ProjectManager projectManager;
    GitViewModel gitViewModel;
    File projectDir = null;
    SecurePrefViewModel securePrefViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityProjectCloneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWindow().setNavigationBarColor(getResources().getColor(R.color.violet));

        gitViewModel = new ViewModelProvider(this).get(GitViewModel.class);
        securePrefViewModel = new ViewModelProvider(this).get(SecurePrefViewModel.class);

        securePrefViewModel.getUsername().observe(this, username -> {
            if (username != null) gitViewModel.setUsername(username);
        });
        securePrefViewModel.getToken().observe(this, token -> {
            if (token != null) gitViewModel.setToken(token);
        });

        securePrefViewModel.loadUsername();
        securePrefViewModel.loadToken();


        gitViewModel.getCloningState().observe(this, state -> {
            switch (state) {
                case START ->
                        Toast.makeText(this, "Start cloning...", Toast.LENGTH_LONG).show();
                case FAILED -> {
                    Toast.makeText(this, "Failed to Clone!", Toast.LENGTH_LONG).show();
                    returnToMenu();
                }
                case COMPLETE -> {
                    Toast.makeText(this, "Cloning end!", Toast.LENGTH_LONG).show();
                    returnToMenu();
                }
            }
        });

        binding.buttonClone.setOnClickListener(v -> {
            if (!NetworkConnectionHelper.hasConnection(this)) {
                Toast.makeText(this, "No network connection!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            disableUI();
            String projectName = binding.projectNameEditText
                    .getText().toString().replaceAll("\\s", "");
            String repoUrl = binding.projectUrlEditText
                    .getText().toString().replaceAll("\\s", "");

            if (repoUrl.isEmpty() || projectName.isEmpty()) {
                Toast.makeText(this, "Fill in the missing fields!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (projectManager.projectExists(projectName)) {
                Toast.makeText(
                        this,
                        "Project with this name already exists!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            gitViewModel.cloneProject(repoUrl, projectName);
        });


        ClipboardManager clipboardManager = (ClipboardManager)
                getSystemService(Context.CLIPBOARD_SERVICE);

        binding.buttonPaste.setOnClickListener(v -> {
            if (clipboardManager != null && clipboardManager.hasPrimaryClip()) {
                CharSequence pasteData = clipboardManager
                        .getPrimaryClip()
                        .getItemAt(0)
                        .getText();
                if (pasteData != null) {
                    EditText editText = binding.projectUrlEditText;
                    editText.setText(pasteData);
                }
                else {
                    Toast.makeText(this, "Buffer is empty!",
                            Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, "No data in Buffer!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void returnToMenu() {
        Intent intent = new Intent(ProjectCloneActivity.this, MenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }


    private void disableUI() {
        binding.buttonClone.setEnabled(false);
        binding.buttonPaste.setEnabled(false);
        binding.projectNameEditText.setEnabled(false);
        binding.projectUrlEditText.setEnabled(false);
        binding.buttonClone.setAlpha(0.5f);
    }


}