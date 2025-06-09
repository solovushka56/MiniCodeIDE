package com.skeeper.minicode.presentation.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.skeeper.minicode.R;
import com.skeeper.minicode.core.singleton.ProjectManager;
import com.skeeper.minicode.databinding.ActivityMainBinding;
import com.skeeper.minicode.core.singleton.SnippetsManager;
import com.skeeper.minicode.presentation.viewmodels.SnippetViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SnippetViewModel snippetViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWindow().setNavigationBarColor(Color.TRANSPARENT);

        snippetViewModel = new ViewModelProvider(this).get(SnippetViewModel.class);
        snippetViewModel.saveSnippetsFilePresetIfNotExists();

        binding.startButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(intent);

        });
        binding.sourceCodeButton.setOnClickListener(v -> {
            String sourceURI = "https://github.com/solovushka56/MiniCodeIDE";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sourceURI));

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
            else {
                Toast.makeText(
                        MainActivity.this,
                        "Not found",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
}