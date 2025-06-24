package com.skeeper.minicode.presentation.ui.activity;

import static android.view.View.GONE;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.skeeper.minicode.R;
import com.skeeper.minicode.databinding.ActivityProjectOpenViewBinding;
import com.skeeper.minicode.data.models.ProjectModelParcelable;
import com.skeeper.minicode.core.singleton.ProjectManager;
import com.skeeper.minicode.presentation.viewmodels.ProjectsViewModel;

import java.io.File;
import java.util.Arrays;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProjectOpenActivity extends AppCompatActivity {

    @Inject ProjectManager projectManager;

    private ActivityProjectOpenViewBinding binding;
    ProjectsViewModel projectsViewModel;
//    ProjectItemView projectItemView = null;
    ProjectModelParcelable boundModel = null;
    String time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);


        binding = ActivityProjectOpenViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        projectsViewModel = new ViewModelProvider(this).get(ProjectsViewModel.class);
        initByModel();


        binding.projectOpenButton.setOnClickListener(v -> {
            var intent = new Intent(ProjectOpenActivity.this, CodeEditorActivity.class);
            intent.putExtra("projectName", boundModel.getProjectName());
            Log.e("TRANSITION", "to code editor");
            startActivity(intent);
        });

        binding.buttonPanelRemove.setOnClickListener(v -> {
            projectManager.deleteProject(boundModel.getProjectName());
            startActivity(new Intent(ProjectOpenActivity.this, MenuActivity.class));
        });
        binding.buttonPanelEditName.setOnClickListener( v -> {
            showRenamePopup();
//            Toast.makeText(this, "In development...", Toast.LENGTH_SHORT).show();

        });

        if (!Arrays.asList(boundModel.getTags()).contains("git") ) {
            binding.commitAndPushButton.setVisibility(GONE);
        }
        binding.commitAndPushButton.setOnClickListener(v -> {
            var intent = new Intent(ProjectOpenActivity.this, ProjectPushActivity.class);
            intent.putExtra("PROJECT_NAME", boundModel.getProjectName());
            startActivity(intent);
        });


    }

    private void initByModel() {
        boundModel = (ProjectModelParcelable) getIntent().getParcelableExtra("projectModel");
        String projectFilepath = boundModel.getProjectPath();
        String projectName = boundModel.getProjectName();
        String mainRectColor = boundModel.getMainRectColorHex();
        String innerRectColor = boundModel.getInnerRectColorHex();
        binding.projectCard.setMainRectColor(Color.parseColor(mainRectColor));
        binding.projectCard.setInnerRectColor(Color.parseColor(innerRectColor));
        binding.projectCard.setProjectName(projectName);
        binding.projectCard.setProjectFilepathText(projectFilepath);

        if (!boundModel.getProjectDescription().isEmpty()){
            binding.projectDescriptonText.setText(boundModel.getProjectDescription());
        }
    }

    private void showRenamePopup() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_rename_file, null);

        Button positiveButton = dialogView.findViewById(R.id.positiveButton);
        Button negativeButton = dialogView.findViewById(R.id.negativeButton);
        TextInputEditText input = dialogView.findViewById(R.id.newNameTextEdit);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        positiveButton.setOnClickListener(v -> {
            String newName = input.getText().toString().trim();

            if (projectManager.projectExists(newName)) {
                input.setError("Project with this name already exists!");
                return;
            }
            projectsViewModel.renameProject(boundModel.getProjectName(), newName);
//            projectManager.renameProject(boundModel.getProjectName(), newName);
            dialog.dismiss();
        });

        negativeButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

}