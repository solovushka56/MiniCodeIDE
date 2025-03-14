package com.skeeper.minicode;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;


public class UndoRedoPanel extends ConstraintLayout {

    private View btnToggle;
    private LinearLayout panel;
    private boolean isPanelExpanded = false;

    public UndoRedoPanel(Context context) {
        super(context);
        init(context, null);
    }

    public UndoRedoPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.sample_undo_redo_panel, this, true);

        btnToggle = findViewById(R.id.btnToggle);
        panel = findViewById(R.id.panel);

        btnToggle.setOnClickListener(v -> togglePanel());

    }


    private void togglePanel() {
        if (isPanelExpanded) {
            collapsePanel();
        } else {
            expandPanel();
        }
        isPanelExpanded = !isPanelExpanded;
    }

    private void expandPanel() {
        panel.setVisibility(View.VISIBLE);
        Animation slideDown = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);
        panel.startAnimation(slideDown);
    }

    private void collapsePanel() {
        Animation slideUp = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
        panel.startAnimation(slideUp);
        panel.setVisibility(View.GONE);
    }


}