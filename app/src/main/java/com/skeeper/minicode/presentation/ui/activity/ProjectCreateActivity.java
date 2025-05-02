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

import com.skeeper.minicode.R;
import com.skeeper.minicode.databinding.ActivityProjectCreateBinding;
import com.skeeper.minicode.utils.helpers.DateTimeHelper;
import com.skeeper.minicode.utils.helpers.ProjectRectColorBinding;
import com.skeeper.minicode.domain.models.ProjectModel;
import com.skeeper.minicode.core.singleton.ProjectManager;

import java.io.IOException;


public class ProjectCreateActivity extends AppCompatActivity {


    ActivityProjectCreateBinding binding;

    private final String currentDateTime = DateTimeHelper.getCurrentTime();

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


        binding.buttonCreate.setOnClickListener(v -> {
            String projName = binding.projectNameEditText.getText().toString();
            String projDescription = binding.projectDescripton.getText().toString();

            if (projName.isEmpty()) {
                Toast.makeText(this, "Enter project name!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (ProjectManager.projectExists(this, projName)) {
                Toast.makeText(
                        this,
                        R.string.project_already_exists_exception,
                        Toast.LENGTH_SHORT).show();
                return;
            }
            

            var rectPalette = new ProjectRectColorBinding();
            ProjectModel model = new ProjectModel(0,
                    projName,
                    projDescription,
                    "projFilepath",
                    new String[] {"s", "jiva", "kotlet"},
                    rectPalette.getMainRectColor(),
                    rectPalette.getInnerRectColor(),
                    "today"
            );
//            Toast.makeText(this, currentDateTime, Toast.LENGTH_SHORT).show();

            ProjectManager.createProject(this, model, false);
            try {
                ProjectManager.saveFile(
                        this,
                        model.getProjectName(),
                        "main.java",
                        "public class Main {\n" +
                                "\n" +
                                "    public static void main(String[] args) {\n" +
                                "        System.out.println(\"Hello, World!\");\n" +
                                "    }\n" +
                                "}");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            Intent intent = new Intent(ProjectCreateActivity.this, MenuActivity.class);
            startActivity(intent);
        });





    }
}