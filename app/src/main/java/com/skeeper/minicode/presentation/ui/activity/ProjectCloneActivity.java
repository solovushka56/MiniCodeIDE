package com.skeeper.minicode.presentation.ui.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.skeeper.minicode.R;
import com.skeeper.minicode.databinding.ActivityProjectCloneBinding;
import com.skeeper.minicode.core.helpers.ProjectRectColorBinding;
import com.skeeper.minicode.domain.models.ProjectModel;
import com.skeeper.minicode.core.singleton.ProjectManager;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;

import java.io.File;

public class ProjectCloneActivity extends AppCompatActivity {
    ActivityProjectCloneBinding binding;

    ProjectModel projectModel = null;
    File projectDir = null;
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

        binding.buttonCreate.setOnClickListener(v -> {
            String projectName = binding.projectNameEditText.getText().toString();
            String projectUrl = binding.projectUrlEditText.getText().toString();

            if (ProjectManager.projectExists(this, projectName)) {
                Toast.makeText(
                        this,
                        "Project with this name already exists!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (projectName.isEmpty() || projectUrl.isEmpty()) {
                Toast.makeText(this, "Fill in the missing fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            cloneProjectFromGit(projectName,"cloned from git");


        });
    }


    private void cloneProjectFromGit(String projName, String projDescription) {
        var rectPalette = new ProjectRectColorBinding();
        ProjectModel model = new ProjectModel(0,
                projName,
                projDescription,
                "projFilepath",
                new String[] {"cloned"},
                rectPalette.getMainRectColor(),
                rectPalette.getInnerRectColor(),
                "today"
        );

        if (!ProjectManager.createProject(this, model, false)) return;
        projectDir = ProjectManager.getProjectDir(this, projName);
        new CloneRepositoryTask().execute("git@github.com:solovushka56/Homework.git");
    }


    private class CloneRepositoryTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            String repoUrl = params[0];
            try {
                CloneCommand cloneCommand = Git.cloneRepository()
                        .setURI(repoUrl)
                        .setDirectory(projectDir)
                        .setBranch("main");
                cloneCommand.call();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(ProjectCloneActivity.this, "успех клон", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ProjectCloneActivity.this, "Ошибка при клонировании репозитория.", Toast.LENGTH_LONG).show();
            }
        }
    }



}