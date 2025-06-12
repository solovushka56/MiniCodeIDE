package com.skeeper.minicode.utils.helpers;

import android.app.Activity;

import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class SystemBarsHelper {
    public static void hideNavigationBar(Activity activity) {
        var windowInsetsController = WindowCompat.getInsetsController(
                        activity.getWindow(),
                        activity.getWindow().getDecorView());

        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        activity.getWindow().getDecorView().post(() -> {
            windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars());
        });

    }

}
