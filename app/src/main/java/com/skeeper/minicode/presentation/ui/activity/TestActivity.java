package com.skeeper.minicode.presentation.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.skeeper.minicode.databinding.ActivityTestBinding;
import com.skeeper.minicode.domain.models.ProjectModel;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private ActivityTestBinding binding;


    public List<ProjectModel> models = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



    }

}