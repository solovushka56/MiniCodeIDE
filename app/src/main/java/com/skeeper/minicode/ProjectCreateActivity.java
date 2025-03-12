package com.skeeper.minicode;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.skeeper.minicode.databinding.ActivityProjectCreateBinding;
import com.skeeper.minicode.helpers.DateTimeHelper;
import com.skeeper.minicode.helpers.ProjectRectColorBinding;
import com.skeeper.minicode.models.ProjectModel;
import com.skeeper.minicode.singleton.ProjectManager;




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



            var rectPalette = new ProjectRectColorBinding();
            ProjectModel model = new ProjectModel(0,
                    projName,
                    projDescription,
                    "projFilepath",
                    new String[] {"s", "jiva", "kotlet"},
                    rectPalette.getMainRectColor(),
                    rectPalette.getInnerRectColor(),
                    "13.02 00"
            );
            Toast.makeText(this, currentDateTime, Toast.LENGTH_SHORT).show();

            ProjectManager.createProject(this, model, false);

        });



    }
}