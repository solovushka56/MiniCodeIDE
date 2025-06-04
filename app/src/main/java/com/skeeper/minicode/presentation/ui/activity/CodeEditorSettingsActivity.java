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
import com.skeeper.minicode.utils.helpers.VibrationManager;
import com.skeeper.minicode.utils.helpers.animations.ViewAnimator;
import com.skeeper.minicode.domain.models.SnippetModel;
import com.skeeper.minicode.core.singleton.SnippetsManager;

import java.util.List;

public class CodeEditorSettingsActivity extends AppCompatActivity {

    ActivityCodeEditorSettingsBinding binding;

    List<SnippetModel> snippetsList;

    private TextInputEditText keyInputView;
    private TextInputEditText valueInputView;


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

        keyInputView = binding.keyTextEdit;
        valueInputView = binding.valueTextEdit;

        snippetsList = SnippetsManager.loadList(this);
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
            SnippetsManager.saveList(this, snippetsList);
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
        binding.mainLinearLayout.addView(snippetPanel);
    }

    private void showPopupWindow(View anchorView) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.pop_up_window_key_value, null);

        var popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );


        popupWindow.showAsDropDown(binding.header, 0, 0, Gravity.CENTER);

        TextInputEditText keyText = findViewById(R.id.keyTextEdit);
        TextInputEditText valueText = findViewById(R.id.valueTextEdit);
        Button confirmButton = findViewById(R.id.popupButtonConfirm);




    }

    private void addKey() {
        SnippetsManager.loadList(this, "keySymbolsData.json");

    }


}