package com.skeeper.minicode;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.skeeper.minicode.databinding.ActivityProjectOpenViewBinding;

public class ProjectOpenView extends AppCompatActivity {

    private ActivityProjectOpenViewBinding binding;

    ProjectItemView projectItemView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityProjectOpenViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setContentView(R.layout.activity_project_open_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int id = getIntent().getIntExtra("id", 0);
        String projectFilepath = getIntent().getStringExtra("projectFilepath");
        String projectName = getIntent().getStringExtra("projectName");
        String mainRectColor = getIntent().getStringExtra("mainRectColor");
        String innerRectColor = getIntent().getStringExtra("innerRectColor");


        binding.projectCard.setMainRectColor(Color.parseColor(mainRectColor));
        binding.projectCard.setInnerRectColor(Color.parseColor(innerRectColor));
        binding.projectCard.setProjectName(projectName);
        binding.projectCard.setProjectFilepathText(projectFilepath);


        binding.projectOpenButton.setOnClickListener(v -> {
            var intent = new Intent(ProjectOpenView.this, CodeEditorActivity.class);
//            intent.putExtra("projectRef", "/src/0/name"); //todo
            startActivity(intent);

        });
    }
}