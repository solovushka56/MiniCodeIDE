package com.skeeper.minicode.presentation.ui.other;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.skeeper.minicode.R;

public class KeyValuePopUpWindow extends ConstraintLayout {

    private TextInputEditText keyText;
    private TextInputEditText valueText;
    private Button confirmButton;


    public KeyValuePopUpWindow(Context context) {
        super(context);
        init(context, null);
    }

    public KeyValuePopUpWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.pop_up_window_key_value, this, true);
        setVisibility(View.INVISIBLE);

        confirmButton = findViewById(R.id.popupButtonConfirm);
        keyText = findViewById(R.id.keyTextEdit);
        valueText = findViewById(R.id.valueTextEdit);

        animate(findViewById(R.id.parentCard), false);

        confirmButton.setOnClickListener(v -> {
            saveSnippet();
            animate(findViewById(R.id.parentCard), true);
        });

    }

    private void animate(View view, boolean reversed) {
        ObjectAnimator fadeInAnimator = ObjectAnimator.ofFloat(
                view,
                "alpha",
                0f,
                1f
        );

        fadeInAnimator.setDuration(1000);
        fadeInAnimator.setInterpolator(new LinearInterpolator());
        view.setVisibility(View.VISIBLE);
        if (reversed) fadeInAnimator.reverse();

        fadeInAnimator.start();
    }


    public void saveSnippet() {

    }


    public String getEnteredKey() {
        return keyText.getText().toString();
    }
    public String getEnteredValue() {
        return valueText.getText().toString();
    }





}
