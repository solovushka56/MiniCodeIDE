package com.skeeper.minicode;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.skeeper.minicode.databinding.ActivityMenuBinding;

public class MenuActivity extends AppCompatActivity {


    private ActivityMenuBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);

        binding = ActivityMenuBinding
                .inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.projectsButton.setOnClickListener(v -> {
            setFragment(new ProjectsFragment());
        });
        binding.settingsButton.setOnClickListener(v -> {
            setFragment(new SettingsFragment());
        });
        binding.tutorialsButton.setOnClickListener(v -> {
            setFragment(new TutorialsFragment());
        });


    }


    public void setFragment(Fragment newFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainFragmentLayout, newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}