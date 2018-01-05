package com.changcai.buyer.util;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public final class DensityConst {
    /** 默认密度 */
    public static float density = 1.0f;
    /** 默认每英寸像素数 */
    public static int densityDpi = 160;
    public static float width = 0;
    public static float height = 0;
    /**
     * 初始化与密度相关的所有变量值
     * 
     * @param activity
     */
    public static void initDensity(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        density = dm.density;
        densityDpi = dm.densityDpi;
        WindowManager wm = activity.getWindowManager();

        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
    }

    /**
     * dip转化为像素
     * 
     * @param dip
     * @return
     */
    public static int getPx(int dip) {
        return (int)(dip*(densityDpi/160));

    }

    /**
     * 像素转化为dip
     * 
     * @param px
     * @return
     */
    public static int getDip(int px) {
        return (int)((px*160)/densityDpi);
    }
}