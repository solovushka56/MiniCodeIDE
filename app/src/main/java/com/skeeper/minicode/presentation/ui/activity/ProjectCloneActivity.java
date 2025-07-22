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
import com.skeeper.minicode.presentation.viewmodels.GitCloneViewModel;
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
    GitCloneViewModel gitViewModel;
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
        ClipboardManager clipboardManager = (ClipboardManager)
                getSystemService(Context.CLIPBOARD_SERVICE);

        gitViewModel = new ViewModelProvider(this).get(GitCloneViewModel .class);
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
                case START -> {
                    Toast.makeText(this, "Start cloning...", Toast.LENGTH_LONG).show();
                    setUiMode(false);
                }
                case FAILED -> {
                    Toast.makeText(this, "Failed to Clone!", Toast.LENGTH_LONG).show();
                    setUiMode(true);
                    returnToMenu();
                }
                case COMPLETE -> {
                    Toast.makeText(this, "Cloning end!", Toast.LENGTH_LONG).show();
                    setUiMode(true);
                    returnToMenu();
                }
            }
        });

        gitViewModel.getClonedPercent().observe(this, value -> {
            binding.progressbar.setProgress(value);
        });

        binding.buttonClone.setOnClickListener(v -> {
            if (!NetworkConnectionHelper.hasConnection(this)) {
                Toast.makeText(this, "No network connection!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
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

            gitViewModel.cloneProject(repoUrl, projectName, -1);
            setUiMode(false);
        });





        binding.buttonPaste.setOnClickListener(v -> {
            if (clipboardManager != null && clipboardManager.hasPrimaryClip()) {
                CharSequence pasteData = clipboardManager
                        .getPrimaryClip()
                        .getItemAt(0)
                        .getText();
                if (pasteData != null) {
                    EditText editText = binding.projectUrlEditText;
                    editText.setText(pasteData);
                    String pasteDataStr = pasteData.toString();
                    binding.projectNameEditText.setText(
                            extractLastWordFromUrl(pasteDataStr));
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


    private void setUiMode(boolean enabled) {
        binding.buttonClone.setEnabled(enabled);
        binding.buttonPaste.setEnabled(enabled);
        binding.projectNameEditText.setEnabled(enabled);
        binding.projectUrlEditText.setEnabled(enabled);
        binding.buttonClone.setAlpha(enabled ? 1f : 0.5f);
    }


    public static String extractLastWordFromUrl(String url) {
        if (url == null || url.isEmpty()) {
            return "";
        }
        
        String cleanedUrl = url.endsWith("/")
                ? url.substring(0, url.length() - 1)
                : url;

        String[] parts = cleanedUrl.split("/");

        for (int i = parts.length - 1; i >= 0; i--) {
            if (!parts[i].isEmpty()) {
                return parts[i];
            }
        }


        return "";
    }

}