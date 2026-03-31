package com.skeeper.minicode.presentation.ui.component;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.button.MaterialButton;
import com.skeeper.minicode.R;

public class CompilePanelView extends ConstraintLayout {
    private View root;
    public ProgressBar compileProgress;
    public MaterialButton runCodeButton;
    public ImageButton hideCompilePanelButton;
    public TextView compileText;

    private final int ANIM_DURATION = 400;
    private final android.view.animation.AccelerateDecelerateInterpolator INTERPOLATOR =
            new android.view.animation.AccelerateDecelerateInterpolator();


    public CompilePanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    private void init(Context ctx) {
        LayoutInflater.from(ctx).inflate(R.layout.compile_panel, this, true);
        root = findViewById(R.id.compilePanel);
        compileProgress = findViewById(R.id.compileProgress);
        runCodeButton = findViewById(R.id.runCodeButton);
        hideCompilePanelButton = findViewById(R.id.hideCompilePanelBtn);
        compileText = findViewById(R.id.compileConsole);

    }


    public void setText(String txt) {
        compileText.setText(txt == null ? "" : txt);
    }


    public void show() {
        if (getVisibility() == View.VISIBLE) return;


        int parentWidth = ((View) getParent()).getWidth();
        int widthSpec = MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        measure(widthSpec, heightSpec);



        int targetHeight = getMeasuredHeight();

        setVisibility(View.VISIBLE);
        this.bringToFront();
        ViewCompat.setElevation(this, 16);
        this.setTranslationZ(16);
        setAlpha(0f);

        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.height = 0;
        setLayoutParams(lp);

        ValueAnimator heightAnimator = ValueAnimator.ofInt(0, targetHeight);
        heightAnimator.setDuration(400);
        heightAnimator.addUpdateListener(a -> {
            lp.height = (int) a.getAnimatedValue();
            setLayoutParams(lp);
        });

        ValueAnimator alphaAnimator = ValueAnimator.ofFloat(0f, 1f);
        alphaAnimator.setDuration(400);
        alphaAnimator.addUpdateListener(a -> setAlpha((float) a.getAnimatedValue()));

        heightAnimator.start();
        alphaAnimator.start();

    }

    public void hide() {
        if (getVisibility() != View.VISIBLE) return;

        int initialHeight = getHeight();
        ViewGroup.LayoutParams lp = getLayoutParams();

        ValueAnimator heightAnimator = ValueAnimator.ofInt(initialHeight, 0);
        heightAnimator.setDuration(400);
        heightAnimator.addUpdateListener(a -> {
            lp.height = (int) a.getAnimatedValue();
            setLayoutParams(lp);
        });

        ValueAnimator alphaAnimator = ValueAnimator.ofFloat(1f, 0f);
        alphaAnimator.setDuration(400);
        alphaAnimator.addUpdateListener(a -> setAlpha((float) a.getAnimatedValue()));

        heightAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setVisibility(View.INVISIBLE);
            }
        });

        heightAnimator.start();
        alphaAnimator.start();
    }



}