package com.skeeper.minicode;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.skeeper.minicode.databinding.ActivityCodeEditorSettingsBinding;

public class CodeEditorSettingsActivity extends AppCompatActivity {

    ActivityCodeEditorSettingsBinding binding;

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


        binding.buttonConfirm.setOnClickListener(v -> {
            showPopupWindow(v);
        });


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

//        confirmButton.setOnClickListener(v -> {
////            String key = keyText.getText().toString();
////            String value = valueText.getText().toString();
////            if (key.isEmpty() || value.isEmpty()) {
////                return;
////            }
////            var existingMap = GsonMapHelper.loadMap(this, "keySymbols");
////            existingMap.put(key, value);
////            Map<String, String> map = new HashMap<>();
////            map.put(key, value);
////            GsonMapHelper.saveMap(this, "keySymbols", map);
////            KeySymbolsService.addSnippet(this, key, value);
////
////            Map<String, String> map = SharedPreferencesHelper.loadMap(
////                    this,
////                    KeySymbolsService.mapSharedKey);
//
//
//
//        });

    }




}