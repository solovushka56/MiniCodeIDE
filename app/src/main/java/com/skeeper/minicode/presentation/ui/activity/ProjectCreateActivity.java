package com.skeeper.minicode.presentation.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.skeeper.minicode.R;
import com.skeeper.minicode.databinding.ActivityProjectCreateBinding;
import com.skeeper.minicode.domain.enums.TemplateType;
import com.skeeper.minicode.presentation.viewmodels.ProjectsViewModel;
import com.skeeper.minicode.presentation.viewmodels.TemplateViewModel;
import com.skeeper.minicode.utils.args.ProjectCreateArgs;
import com.skeeper.minicode.utils.helpers.DateTimeHelper;
import com.skeeper.minicode.utils.helpers.ProjectRectColorBinding;
import com.skeeper.minicode.data.models.ProjectModelParcelable;
import com.skeeper.minicode.core.singleton.ProjectManager;

import java.io.IOException;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProjectCreateActivity extends AppCompatActivity {


    ActivityProjectCreateBinding binding;

    @Inject ProjectManager projectManager;
    ProjectsViewModel projectsViewModel;
    TemplateViewModel templateViewModel;
    private final String currentDateTime = DateTimeHelper.getCurrentTime();
    private String projName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);

        binding = ActivityProjectCreateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        projectsViewModel = new ViewModelProvider(this).get(ProjectsViewModel.class);
        templateViewModel = new ViewModelProvider(this).get(TemplateViewModel.class);

        projectsViewModel.getProjectCreationState().observe(this, state -> {
            if (state == ProjectsViewModel.ProjectCreationState.SUCCESS) {
                Intent intent = new Intent(ProjectCreateActivity.this, MenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        binding.buttonCreate.setOnClickListener(v -> {
            projName = binding.projectNameEditText.getText().toString();
            String projDescription = binding.projectDescripton.getText().toString();

            if (projName.isEmpty()) {
                Toast.makeText(this, "Enter project name!",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (projectManager.projectExists(projName)) {
                Toast.makeText(this,
                        R.string.project_already_exists_exception,
                        Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedId = binding.templateTypeGroup.getCheckedRadioButtonId();

            TemplateType selectedType = TemplateType.NONE;

            if (selectedId == R.id.optionJava) {
                selectedType = TemplateType.JAVA;
            } else if (selectedId == R.id.optionPython) {
                selectedType = TemplateType.PYTHON;
            }

            var args = new ProjectCreateArgs(
                    projName,
                    projDescription,
                    new String[] {"local"},
                    selectedType);
            projectsViewModel.createProjectAsync(args);

        });




    }
}