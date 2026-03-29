package com.skeeper.minicode.presentation.ui.activity;

import static com.skeeper.minicode.presentation.ui.activity.ProjectCloneActivity.extractLastWordFromUrl;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.skeeper.minicode.R;
import com.skeeper.minicode.core.constants.ProjectConstants;
import com.skeeper.minicode.data.sources.preferences.UserPreferencesProvider;
import com.skeeper.minicode.databinding.ActivityCompilationSettingsBinding;
import com.skeeper.minicode.presentation.viewmodels.SharedPrefsViewModel;

import javax.inject.Inject;

public class CompilationSettingsActivity extends AppCompatActivity {
    ActivityCompilationSettingsBinding binding;

    @Inject
    UserPreferencesProvider userPreferencesProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityCompilationSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWindow().setNavigationBarColor(getResources().getColor(R.color.activity_bottom));
        SharedPreferences preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);

        binding.buttonConfirm.setOnClickListener(v -> {
            String text = binding.serverUrlEditText.getText().toString();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("customServerUrl", text);
            editor.apply();
            finish();
        });

        binding.buttonReset.setOnClickListener(v -> {
            binding.serverUrlEditText.setText(ProjectConstants.SERVER_URL);
        });

        ClipboardManager clipboardManager = (ClipboardManager)
                getSystemService(Context.CLIPBOARD_SERVICE);
        binding.buttonPaste.setOnClickListener(v -> {
            if (clipboardManager != null && clipboardManager.hasPrimaryClip()) {
                CharSequence pasteData = clipboardManager
                        .getPrimaryClip()
                        .getItemAt(0)
                        .getText();
                if (pasteData != null) {
                    EditText editText = binding.serverUrlEditText;
                    editText.setText(pasteData);
                    String pasteDataStr = pasteData.toString();
                    binding.serverUrlEditText.setText(
                            extractLastWordFromUrl(pasteDataStr));
                }
                else {
                    Toast.makeText(this, "Buffer is empty!",
                            Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, "No data in Buffer!",
                        Toast.LENGTH_SHORT).show();
            }
        });
        binding.buttonReset.setOnClickListener(v -> {
            binding.serverUrlEditText.setText(ProjectConstants.SERVER_URL);
        });

        String url = preferences.contains("customServerUrl")
                ? preferences.getString("customServerUrl", "")
                : ProjectConstants.SERVER_URL;
        binding.serverUrlEditText.setText(url);

    }
}