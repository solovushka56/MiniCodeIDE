package com.skeeper.minicode.presentation.ui.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.skeeper.minicode.R;
import com.skeeper.minicode.databinding.ActivityProjectCloneBinding;
import com.skeeper.minicode.presentation.viewmodels.ProjectCloneViewModel;
import com.skeeper.minicode.presentation.viewmodels.factory.ProjectCloneViewModelFactory;
import com.skeeper.minicode.utils.helpers.ProjectRectColorBinding;
import com.skeeper.minicode.domain.models.ProjectModel;
import com.skeeper.minicode.core.singleton.ProjectManager;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;

public class ProjectCloneActivity extends AppCompatActivity {
    ActivityProjectCloneBinding binding;

    ProjectCloneViewModel cloneViewModel;

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

        cloneViewModel = new ViewModelProvider(this).get(ProjectCloneViewModel.class);

//        new Thread(() -> {
//            CloneCommand clone = Git.cloneRepository()
//                    .setURI("https://github.com/solovushka56/MiniCodeIDE.git")
//                    .setDirectory(getFilesDir());
//            try (Git git = clone.call()) {
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                Log.e("GIT EXC", e.getMessage());
//            }
//
//
//        }).start();


        binding.buttonCreate.setOnClickListener(v -> {
            String projectName = binding.projectNameEditText.getText().toString();
            String repoUrl = binding.projectUrlEditText.getText().toString();

//            if (ProjectManager.projectExists(this, projectName)) {
//                Toast.makeText(
//                        this,
//                        "Project with this name already exists!",
//                        Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if (repoUrl.isEmpty()) {
//                Toast.makeText(this, "Fill in the missing fields!", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            new Thread(() -> {
//                CloneCommand clone = Git.cloneRepository()
//                        .setURI("https://github.com/solovushka56/MiniCodeIDE.git")
//                        .setDirectory(ProjectManager.getProjectDir(this, "lol"));
//                try (Git git = clone.call()) {
//
//                } catch (GitAPIException e) {
//                    e.printStackTrace();
//                }
//
//
//            }).start();
//        });
        });
    }


//    private void cloneProjectFromGit(String repoUrl) {
//
//        String projName = "lol";//ProjectCloneViewModel.getRepoName(repoUrl);
//
//        var rectPalette = new ProjectRectColorBinding();
//        ProjectModel model = new ProjectModel(0,
//                projName,
//                "cloned from git",
//                "projFilepath",
//                new String[] {"cloned"},
//                rectPalette.getMainRectColor(),
//                rectPalette.getInnerRectColor(),
//                "today"
//        );
//
//        if (!ProjectManager.createProject(this, model, false)) return;
//        projectDir = ProjectManager.getProjectDir(this, projName);
//
//
//
//    }




}