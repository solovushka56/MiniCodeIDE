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
import com.skeeper.minicode.core.singleton.ProjectManager;
import com.skeeper.minicode.databinding.ActivityProjectPushBinding;
import com.skeeper.minicode.domain.models.ProjectModel;
import com.skeeper.minicode.presentation.viewmodels.GitViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class ProjectPushActivity extends AppCompatActivity {


    @Inject
    ProjectManager projectManager;
    ActivityProjectPushBinding binding;
    GitViewModel gitViewModel;

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

        projectName = getIntent().getStringExtra("PROJECT_NAME");
        gitViewModel = new ViewModelProvider(this).get(GitViewModel.class);
//        Toast.makeText(this, String.valueOf(projectName == null), Toast.LENGTH_LONG).show();


        binding.buttonPushAndCommit.setOnClickListener(v -> {
            String pushUrl = binding.pushUriEditText.getText().toString();
            String commitName = binding.commitEditText.getText().toString();
            String commitMessage = binding.commitMessageEditText.getText().toString();

            if (pushUrl.isEmpty() || commitName.isEmpty()) {
                Toast.makeText(this, "Fill in the missing text fields!", Toast.LENGTH_SHORT).show();
            }

            gitViewModel.commitAndPushProject(projectName, pushUrl, commitName, commitMessage);

        });

    }
}