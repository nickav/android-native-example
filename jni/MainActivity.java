package com.example.NativeExample;

import android.app.*;
import android.os.*;
import android.view.*;
import android.graphics.*;
import android.content.*;
import android.util.*;

public class MainActivity extends NativeActivity {
    static {
        System.loadLibrary("main");
    }
    public native void nativeSetSafeArea(int top, int bottom, int left, int right);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(lp);
        }

        hideSystemUI();
        getSafeArea();
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
            getSafeArea();
        }
    }

    private void hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController controller = getWindow().getInsetsController();
            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                controller.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            );
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void getSafeArea() {
        View decorView = getWindow().getDecorView();
        WindowInsets insets = decorView.getRootWindowInsets();
        if (insets != null) {
            int safeTop = insets.getSystemWindowInsetTop();
            int safeBottom = insets.getSystemWindowInsetBottom();
            int safeLeft = insets.getSystemWindowInsetLeft();
            int safeRight = insets.getSystemWindowInsetRight();
            
            Log.i("SafeArea", "Top: " + safeTop + ", Bottom: " + safeBottom + ", Left: " + safeLeft + ", Right: " + safeRight);
            
            nativeSetSafeArea(safeTop, safeBottom, safeLeft, safeRight);
        } else {
            nativeSetSafeArea(0, 0, 0, 0);
        }
    }

    public void vibrate() {
        Vibrator v = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(50, 50));
        } else {
            v.vibrate(500);
        }
    }
}
