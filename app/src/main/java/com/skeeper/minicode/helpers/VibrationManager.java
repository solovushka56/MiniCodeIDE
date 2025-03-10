package com.skeeper.minicode.helpers;

import android.content.Context;
import android.os.Vibrator;

public class VibrationManager {
    public static enum VibrationTypeEnum {
        VIBRATION_LONG,
        VIBRATION_CLICK_MEDIUM

    }
    public static void vibrate(long timeMsec, Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator() == true) //BUG HERE maybe !!!!!!!!!!!
        {
            vibrator.vibrate(timeMsec);
        }

    }


    public static void vibrate(VibrationTypeEnum vibrationType, Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator() == true) //BUG HERE maybe !!!!!!!!!!!
        {
            // todo
//            vibrator.vibrate(timeMsec);
        }

    }
}
