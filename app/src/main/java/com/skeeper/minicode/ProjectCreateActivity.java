package com.skeeper.minicode;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.skeeper.minicode.databinding.ActivityProjectCreateBinding;
import com.skeeper.minicode.helpers.ProjectRectColorBinding;
import com.skeeper.minicode.models.ProjectModel;
import com.skeeper.minicode.singleton.ProjectManagerSingleton;

import java.io.File;

public class ProjectCreateActivity extends AppCompatActivity {


    ActivityProjectCreateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);


        binding = ActivityProjectCreateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        binding.buttonCreate.setOnClickListener(v -> {
            String projName = binding.projectNameEditText.getText().toString();
            String projDescription = binding.projectDescripton.getTag().toString();

            String projFilepath = (new File(ProjectManagerSingleton
                    .getAllProjectsFolder(this), projName))
                    .getAbsolutePath();
            var rectPalette = new ProjectRectColorBinding();
            ProjectModel model = new ProjectModel(0,
                    projName,
                    projDescription,
                    projFilepath,
                    new String[] {"s", "jiva", "kotlet"},
                    rectPalette.getMainRectColor(),
                    rectPalette.getInnerRectColor() );


        });



    }
}