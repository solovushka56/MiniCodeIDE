package com.skeeper.minicode.helpers.animations;

import android.animation.TimeInterpolator;
import android.view.View;
import android.view.ViewPropertyAnimator;

import androidx.annotation.Nullable;

public class ViewScaleComponent {

    private final View actor;

    public float scaleX;
    public float scaleY;
    public long scaleTimeMsec;


    //    @Nullable public Runnable onFinishedAction;
    @Nullable
    public TimeInterpolator interpolator;


    public ViewScaleComponent(View actor, long scaleTimeMsec, float scaleX, float scaleY, @Nullable Runnable onFinishedAction, @Nullable TimeInterpolator interpolator) {
        this.actor = actor;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleTimeMsec = scaleTimeMsec;
//        this.onFinishedAction = onFinishedAction;
        this.interpolator = interpolator;
    }


    public void scaleView(@Nullable Runnable newOnFinishedAction) {
        scaleView(actor, scaleTimeMsec, scaleX, scaleY,
                newOnFinishedAction,
                interpolator);
    }


    public void scaleViewReturning(@Nullable Runnable newOnFinishedAction, float explosiveTimePercent) {
        float scaleUpTime = scaleTimeMsec * explosiveTimePercent;
        float scaleReturnTime =  scaleTimeMsec * (1 - explosiveTimePercent);
        scaleView(actor, (long)scaleUpTime, scaleX, scaleY,
                () -> { scaleView(actor, (long) scaleReturnTime, 1f, 1f, newOnFinishedAction, interpolator); },
                interpolator);
    }

    public void scaleViewReturning(@Nullable Runnable newOnFinishedAction, long scaleUpTimeMs, long scaleDownTimeMs) {
        scaleView(actor, scaleUpTimeMs, scaleX, scaleY,
                () -> { scaleView(actor, scaleDownTimeMs, 1f, 1f, newOnFinishedAction, interpolator); },
                interpolator);
    }


    public void scaleToDefault(long timeMs, @Nullable Runnable endAction) {
        scaleView(actor, timeMs, 1f, 1f, endAction, interpolator);
    }
    public void scaleToDefault(@Nullable Runnable endAction) {
        scaleView(actor, scaleTimeMsec, 1f, 1f, endAction, interpolator);
    }



    public static void scaleView(View view, long timeMsec,
                                 float scale_X, float scale_Y, @Nullable Runnable action,
                                 @Nullable TimeInterpolator interpolator)
    {
        ViewPropertyAnimator animation = view.animate();

        if (action != null) animation.withEndAction(action);
        if (interpolator != null) animation.setInterpolator(interpolator);

        animation.setDuration(timeMsec)
                .scaleX(scale_X).scaleY(scale_Y).start();
    }
}
