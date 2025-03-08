package com.skeeper.minicode;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Toast;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skeeper.minicode.adapters.ProjectAdapter;
import com.skeeper.minicode.databinding.ActivityTestBinding;
import com.skeeper.minicode.models.ProjectModel;

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


        models.add(new ProjectModel(1, "tutorial", "/0/sil/bindssd/intsaf/boor"));
        models.add(new ProjectModel(2, "startup", "/0/saf/boor"));
        models.add(new ProjectModel(3, "startup2", "/0/sil/binintsaf/boor"));
        setProjectsRecycler();

    }
    private void setProjectsRecycler() {


        var recyclerView = binding.projectsRecyclerView;
        var adapter = new ProjectAdapter(this, models);

        recyclerView.setLayoutManager(new LinearLayoutManager(
                this, RecyclerView.VERTICAL, false));

        recyclerView.setAdapter(adapter);

        Toast.makeText(this, this.toString(), Toast.LENGTH_LONG).show();

    }

}