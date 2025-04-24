package com.skeeper.minicode.helpers.animations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

public class ViewAnimator {

    public static void collapseLeft(View view, long duration, boolean removeViewOnFinish) {

        int startWidth = view.getWidth();
        ValueAnimator animator = ValueAnimator.ofInt(startWidth, 0);

        animator.setDuration(duration);
        animator.setInterpolator(new DecelerateInterpolator());

        if (removeViewOnFinish) {
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    ((ViewGroup) view.getParent()).removeView(view);
                }
            });
        }
        animator.addUpdateListener(animation -> {
            int animatedValue = (int) animation.getAnimatedValue();
            view.getLayoutParams().width = animatedValue;
            view.requestLayout();
        });
        animator.start();
    }

    public static void collapseRight(View view, long duration, boolean removeViewOnFinish) {
        int initialWidth = view.getWidth();


        ValueAnimator animator = ValueAnimator.ofInt(initialWidth, 0);
        animator.setDuration(duration);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        if (removeViewOnFinish) {
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    ((ViewGroup) view.getParent()).removeView(view);
                }
            });
        }
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                view.getLayoutParams().width = animatedValue;
                view.setTranslationX(initialWidth - animatedValue);
                view.requestLayout();
            }
        });

        animator.start();
    }
}

