package com.skeeper.minicode.core.helpers.animations;

public class BackInterpolations
{

    private static final float overshootDefault = 1.75f;

    public static float backIn(float t) {
        return t * t * ((overshootDefault + 1) * t - overshootDefault);
    }
    public static float backOut(float t) {
        return (--t) * t * ((overshootDefault + 1) * t + overshootDefault) + 1;
    }
    public static float backInOut(float t) {
        t *= 2;
        if (t < 1) {
            return 0.5f * (t * t * ((overshootDefault + 1) * t - overshootDefault));
        } else {
            t -= 2;
            return 0.5f * (t * t * ((overshootDefault + 1) * t + overshootDefault) + 2);
        }
    }


    public static float backIn(float t, float overshoot) {
        return t * t * ((overshoot + 1) * t - overshoot);
    }
    public static float backOut(float t, float overshoot) {
        return (--t) * t * ((overshoot + 1) * t + overshoot) + 1;
    }
    public static float backInOut(float t, float overshoot) {
        t *= 2;
        if (t < 1) {
            return 0.5f * (t * t * ((overshoot + 1) * t - overshoot));
        } else {
            t -= 2;
            return 0.5f * (t * t * ((overshoot + 1) * t + overshoot) + 2);
        }
    }

}