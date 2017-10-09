package com.tino.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;


public class DensityUtil {
    private static int screenHeight = 0;
    private static int screenWidth = 0;

    public static int dip2px(Context var0, float var1) {
        return (int) ((var1 * var0.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int px2dip(Context var0, float var1) {
        return (int) ((var1 / var0.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int sp2px(Context var0, float var1) {
        return (int) ((var1 * var0.getResources().getDisplayMetrics().scaledDensity) + 0.5f);
    }

    public static int px2sp(Context var0, float var1) {
        return (int) ((var1 / var0.getResources().getDisplayMetrics().scaledDensity) + 0.5f);
    }

    public static int getScreenWidth(Context c) {
        if (screenWidth == 0) {
            Display display = ((WindowManager) c.getSystemService("window")).getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }
        return screenWidth;
    }


}
