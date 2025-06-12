package com.skeeper.minicode.presentation.ui.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.skeeper.minicode.R;
import com.skeeper.minicode.presentation.ui.component.SnippetPanelViewItem;
import com.skeeper.minicode.databinding.ActivityCodeEditorSettingsBinding;
import com.skeeper.minicode.utils.helpers.SystemBarsHelper;
import com.skeeper.minicode.utils.helpers.VibrationManager;
import com.skeeper.minicode.utils.helpers.animations.ViewAnimator;
import com.skeeper.minicode.domain.models.SnippetModel;
import com.skeeper.minicode.core.singleton.SnippetsManager;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

//  todo view model
@AndroidEntryPoint
public class CodeEditorSettingsActivity extends AppCompatActivity {

    ActivityCodeEditorSettingsBinding binding;

    List<SnippetModel> snippetsList;

    private TextInputEditText keyInputView;
    private TextInputEditText valueInputView;

    @Inject SnippetsManager snippetsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityCodeEditorSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWindow().setNavigationBarColor(getResources().getColor(R.color.violet));

        keyInputView = binding.keyTextEdit;
        valueInputView = binding.valueTextEdit;

        try {
            snippetsList = snippetsManager.loadList();
        } catch (IOException e) {
//            throw new RuntimeException(e);

        }
        for (var model : snippetsList) {
            createSnippetPanel(model);
        }

        binding.addSnippetButton.setOnClickListener(v -> {
            VibrationManager.vibrate(25, this);
            if (keyInputView.getText().toString().isEmpty() || valueInputView.getText().toString().isEmpty()) {
                Toast.makeText(this, "Enter key and value to add!", Toast.LENGTH_SHORT).show();
                return;
            }
            var newSnippetModel = new SnippetModel(
                    keyInputView.getText().toString(),
                    valueInputView.getText().toString());

            createSnippetPanel(newSnippetModel);
            snippetsList.add(newSnippetModel);

            keyInputView.setText("");
            valueInputView.setText("");

        });

        binding.buttonConfirm.setOnClickListener(v -> {
            try {
                snippetsManager.saveList(snippetsList);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            finish();
        });

    }

    private void createSnippetPanel(SnippetModel boundModel) {
        var snippetPanel = new SnippetPanelViewItem(this);
        snippetPanel.setBoundModel(boundModel);
        snippetPanel.setKeyValue(boundModel.getSymbolKey(), boundModel.getSymbolValue());
        var layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 0, 0, 30);
        snippetPanel.setLayoutParams(layoutParams);

        snippetPanel.removeButton.setOnClickListener(v -> {
            VibrationManager.vibrate(70, this);
            snippetsList.remove(boundModel);
            ViewAnimator.collapseLeft(snippetPanel, 300L, true);
            snippetPanel.animate().alpha(0f).setDuration(250L).setInterpolator(new DecelerateInterpolator()).withEndAction(() ->
                    snippetPanel.animate().scaleYBy(0f).setDuration(50L).setInterpolator(new DecelerateInterpolator()).start()
                    ).start();
//                binding.mainLinearLayout.removeView(snippetPanel);
        });
        binding.itemsLayout.addView(snippetPanel);
    }




}