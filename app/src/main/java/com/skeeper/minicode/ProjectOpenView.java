package com.skeeper.minicode;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.skeeper.minicode.databinding.ActivityProjectOpenViewBinding;
import com.skeeper.minicode.models.ProjectModel;
import com.skeeper.minicode.singleton.ProjectManager;

import java.time.LocalDateTime;

public class ProjectOpenView extends AppCompatActivity {

    private ActivityProjectOpenViewBinding binding;

//    ProjectItemView projectItemView = null;
    ProjectModel boundModel = null;
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

        initByModel();


        binding.projectOpenButton.setOnClickListener(v -> {
            var intent = new Intent(ProjectOpenView.this, CodeEditorActivity.class);
//            intent.putExtra("projectRef", "/src/0/name"); //todo
            intent.putExtra("projectName", boundModel.getProjectName());
            startActivity(intent);
        });

        binding.buttonPanelRemove.setOnClickListener(v -> {
            ProjectManager.deleteProject(this, boundModel.getProjectName());
            startActivity(new Intent(ProjectOpenView.this, MenuActivity.class));
        });
        binding.buttonPanelEditName.setOnClickListener( v -> {
            Toast.makeText(this, "In development...", Toast.LENGTH_SHORT).show();
        });
    }

    private void initByModel() {
        boundModel = (ProjectModel) getIntent().getParcelableExtra("projectModel");
        String projectFilepath = boundModel.getProjectPath();
        String projectName = boundModel.getProjectName();
        String mainRectColor = boundModel.getMainRectColorHex();
        String innerRectColor = boundModel.getInnerRectColorHex();
        String creationDateTime = boundModel.getCreationDateTime();
//        Toast.makeText(this, creationDateTime, Toast.LENGTH_SHORT).show();
        binding.projectCard.setMainRectColor(Color.parseColor(mainRectColor));
        binding.projectCard.setInnerRectColor(Color.parseColor(innerRectColor));
        binding.projectCard.setProjectName(projectName);
        binding.projectCard.setProjectFilepathText(projectFilepath);

        if (!boundModel.getProjectDescription().isEmpty()){
            binding.projectDescriptonText.setText(boundModel.getProjectDescription());
        }
//        binding.textCreationDate.setText(creationDateTime);
    }

}