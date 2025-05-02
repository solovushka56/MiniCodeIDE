package com.skeeper.minicode.utils.helpers.animations;



public class QuartInterpolations {

    public static float quartIn(float input) {
        return input * input * input * input;
    }
    public static float quartOut(float input) {
        float t = input - 1;
        return 1 - t * t * t * t;
    }

    public static float quartInOut(float input) {
        if (input < 0.5f) {
            return 8 * input * input * input * input;
        } else {
            float t = input - 1;
            return 1 - 8 * t * t * t * t;
        }
    }


}
