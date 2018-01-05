package com.changcai.buyer.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.changcai.buyer.CommonApplication;
import com.changcai.buyer.R;
import com.changcai.buyer.ui.CommonWebViewActivity;
import com.changcai.buyer.view.ConfirmDialog;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLConfig;

import cn.jpush.android.api.JPushInterface;

/**
 * android系统相关常用操作
 *
 * @author Zhoujun 说明：提供一些android系统常用操作：如系统版本，图片操作等
 */
public class AndroidUtil {


    private static final int DEFAULT_MAX_BITMAP_DIMENSION = 2048;

    // http://stackoverflow.com/questions/15313807/android-maximum-allowed-width-height-of-bitmap

    /**
     * bitmap max height
     *
     * @return
     */
    public static int getMaxHeight() {
        EGL10 egl = (EGL10) EGLContext.getEGL();
        EGLDisplay display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

        // Initialise
        int[] version = new int[2];
        egl.eglInitialize(display, version);

        // Query total number of configurations
        int[] totalConfigurations = new int[1];
        egl.eglGetConfigs(display, null, 0, totalConfigurations);

        // Query actual list configurations
        EGLConfig[] configurationsList = new EGLConfig[totalConfigurations[0]];
        egl.eglGetConfigs(display, configurationsList, totalConfigurations[0], totalConfigurations);

        int[] textureSize = new int[1];
        int maximumTextureSize = 0;

        // Iterate through all the configurations to located the maximum texture size
        for (int i = 0; i < totalConfigurations[0]; i++) {
            // Only need to check for width since opengl textures are always squared
            egl.eglGetConfigAttrib(display, configurationsList[i], EGL10.EGL_MAX_PBUFFER_WIDTH, textureSize);

            // Keep track of the maximum texture size
            if (maximumTextureSize < textureSize[0])
                maximumTextureSize = textureSize[0];
        }

        // Release
        egl.eglTerminate(display);

        // Return largest texture size found, or default
        return Math.max(maximumTextureSize, DEFAULT_MAX_BITMAP_DIMENSION);

    }


    /**
     * 获取sdk版本
     *
     * @return
     */
    public static int getAndroidSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 返回当前程序版本号
     */
    public static int getAppVersionCode(Context context) {
        int versionCode = 0;
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            // 这里的context.getPackageName()可以换成你要查看的程序的包名
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionCode;
    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "0.0.0";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            // 这里的context.getPackageName()可以换成你要查看的程序的包名
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    /**
     * 获得设备识别认证码
     *
     * @return
     */
    public static String getIMEI(Context context) {
//        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        if (tm == null) {
//            return null;
//        }
//        return tm.getDeviceId();

//        String id;
//        //android.telephony.TelephonyManager
//        TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        if (mTelephony.getDeviceId() != null) {
//            id = mTelephony.getDeviceId();
//        } else {
//            //android.provider.Settings;
//            id = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//        }
        return JPushInterface.getUdid(context);
    }

    /**
     * 设置view高宽
     *
     * @param view
     * @param width
     * @param height
     */
    public static void resetViewSize(final View view, int width, int height) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.width = width;
        lp.height = height;
        view.setLayoutParams(lp);
    }

    /**
     * 设置View高
     *
     * @param view
     * @param height
     */
    public static void resetViewHeight(View view, int height) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.height = height;
        view.setLayoutParams(lp);
    }


    /**
     * 设置文本颜色
     *
     * @param textView
     * @param str1
     * @param color1   字符串str的颜色
     * @param textSize
     */
    public static void setTextSizeColor(TextView textView, String str1, int color1, int textSize) {
        SpannableStringBuilder style = new SpannableStringBuilder(str1);
        style.setSpan(new ForegroundColorSpan(color1), 0, str1.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        style.setSpan(new AbsoluteSizeSpan(textSize, true), 0, str1.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        textView.setText(style);
    }

    /**
     * 设置textview 颜色和文字大小 注意：保持后面的数组大小一样
     *
     * @param textView
     * @param strs
     * @param colors
     * @param textSizes
     */
    public static void setTextSizeColor(TextView textView, String[] strs, int[] colors, int[] textSizes) {
        StringBuilder builder = new StringBuilder();
        for (String str : strs) {
            builder.append(str);
        }
        SpannableStringBuilder style = new SpannableStringBuilder(builder.toString());
        int count = strs.length;
        int startIndex = 0;
        int endIndex = 0;
        for (int i = 0; i < count; i++) {
            endIndex += strs[i].length();
            style.setSpan(new ForegroundColorSpan(colors[i]), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            style.setSpan(new AbsoluteSizeSpan(textSizes[i], true), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            startIndex += strs[i].length();
        }
        textView.setText(style);
    }

    /**
     * 判断sd卡是否安装
     *
     * @return
     */
    public static boolean existSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获得屏幕密度
     *
     * @param context
     * @return
     */
    public static float getDensity(Context context) {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return dm.density;
    }

    /**
     * 获得屏幕参数
     *
     * @param context
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return dm;
    }

    /**
     * 获取状态栏高度
     *
     * @param activity
     * @return
     */
    @Deprecated
    public static int getTitleBar(Activity activity) {
        Window window = activity.getWindow();
        Rect frame = new Rect();
        window.getDecorView().getWindowVisibleDisplayFrame(frame);
        // frame.top就是通知栏的高度。
        int statusBarHeight = frame.top;

        // 标题栏跟状态栏的总体高度
        int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        // 标题栏的高度：用上面的值减去状态栏的高度及为标题栏高度
        int titleBarHeight = contentViewTop - statusBarHeight;
        return titleBarHeight;
    }

    /**
     * 获取状态栏高度
     *
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        return statusBarHeight;
    }

    /**
     * 屏幕截屏
     *
     * @param activity
     * @return
     */
    public static Bitmap takeScreenShot(Activity activity) {
        Window window = activity.getWindow();
        // View是你需要截图的View
        View view = window.getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        // 获取屏幕长和高
        int width = getDisplayMetrics(activity).widthPixels;
        int height = getDisplayMetrics(activity).heightPixels;
        // 去掉标题栏
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        int bHeight = b1.getHeight();
        int nHeight = height - statusBarHeight;
        if (height > bHeight) {
            nHeight = bHeight - statusBarHeight;
        }
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, nHeight);
        view.destroyDrawingCache();
        return b;
    }

    /**
     * 将百度地图画到图片上
     *
     * @param src
     * @param watermark
     * @param left
     * @param top
     * @return
     */
    public static Bitmap watermarkBitmap(Bitmap src, Bitmap watermark, int left, int top) {
        if (src == null) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight();
        // 需要处理图片太大造成的内存超过的问题,这里我的图片很小所以不写相应代码了
        Bitmap newb = Bitmap.createBitmap(w, h, Config.RGB_565);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
        Paint paint = new Paint();
        // 加入图片
        if (watermark != null) {
            int ww = watermark.getWidth();
            int wh = watermark.getHeight();
            // paint.setAlpha(50);
            // cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, paint);//
            // 在src的右下角画入水印
            cv.drawBitmap(watermark, left, top, paint);// 在src的左上角画入水印
        } else {
            Log.i("i", "water mark failed");
        }

        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        cv.restore();// 存储
        return newb;
    }

    /**
     * 把百度地图截图和屏幕截图拼在一起（解决截图，百度那块黑屏的问题）
     *
     * @param src
     * @param watermark
     * @param left
     * @param top
     * @param layoutServiceHeight   (维修点加油站的布局高度)
     * @param layoutServiceLocation (维修点加油站的布局位置)
     * @return
     */
    public static Bitmap waterMap(Bitmap src, Bitmap watermark, int left, int top, int layoutServiceHeight, int[] layoutServiceLocation) {
        if (src == null) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight();
        // 需要处理图片太大造成的内存超过的问题,这里我的图片很小所以不写相应代码了
        Bitmap newb = Bitmap.createBitmap(w, h, Config.RGB_565);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        Paint paint = new Paint();
        // 加入图片
        if (watermark != null) {
            int ww = watermark.getWidth();
            int wh = watermark.getHeight();
            // paint.setAlpha(50);
            // cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, paint);//
            cv.drawBitmap(watermark, left, top, paint);// 在src的左上角画入水印
            //补上透明度（设置交叉模式后修复问题）
            paint.setXfermode(new PorterDuffXfermode(Mode.DST_ATOP));
            Rect rect = new Rect(0, layoutServiceLocation[1], w, layoutServiceLocation[1] + layoutServiceHeight);
            paint.setColor(Color.argb(120, 0, 0, 0));
            cv.drawRect(rect, paint);
        } else {
            Log.i("i", "water mark failed");
        }
        //在魅族手机上要设置图片交叉模式
        paint.setXfermode(new PorterDuffXfermode(Mode.SCREEN));
        cv.drawBitmap(src, 0, 0, paint);// 在 0，0坐标开始画入src
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        cv.restore();// 存储
        return newb;
    }

    /**
     * 像素转换成屏幕密度
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 屏幕密度转换成像素;
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取透明度白色
     *
     * @param alpha
     * @return
     */
    public static int getWhiteAlpha(int alpha) {
        return Color.argb(alpha, 255, 255, 255);
    }

    /**
     * 从assets读取文件
     *
     * @param fileName
     * @param resource
     * @return
     */
    public static String getFromAssets(String fileName, Resources resource) {
        try {
            InputStreamReader inputReader = new InputStreamReader(resource.getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            inputReader.close();
            bufReader.close();
            return Result;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 判断GPS是否打开
     *
     * @param context
     * @return
     */
    public static boolean isGpsOPen(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gps) {
            return true;
        }

        return false;
    }

    /**
     * 强制帮用户打开GPS
     *
     * @param context
     */
    public static void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    // 获取ApiKey
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return apiKey;
    }

    /**
     * 毛玻璃效果
     *
     * @param context
     * @param path
     * @return
     */
    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static Bitmap fastblur(Context context, String path) {
        Bitmap sentBitmap = ImageUtil.compressBitmap(path);
        if (sentBitmap == null) {
//			sentBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg_login);
        }
        int radius = 25;
        if (Build.VERSION.SDK_INT > 16) {
            Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
            final RenderScript rs = RenderScript.create(context);
            final Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT);
            final Allocation output = Allocation.createTyped(rs, input.getType());
            final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(radius /* e.g. 3.f */);
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
            sentBitmap.recycle();
            sentBitmap = null;
            return bitmap;
        }
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        sentBitmap.recycle();
        sentBitmap = null;
        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }
        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        r = null;
        g = null;
        b = null;
        sir = null;
        pix = null;
        stack = null;
        dv = null;
        return (bitmap);
    }

    public static void startBrowser(Context context, Bundle b, boolean isSystem) {
        if (isSystem) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            if (b != null) {
                String url = b.getString("url");
                Uri content_url = Uri.parse(url);
                intent.setData(content_url);
                context.startActivity(intent);
            }
        } else {
            Intent intent = new Intent(context, CommonWebViewActivity.class);
            intent.putExtras(b);
            context.startActivity(intent);
            if (b.containsKey("percentY")) {
                float percent = b.getFloat("percentY", 0f);
                if (percent < 0.4) {
                    ((Activity) context).overridePendingTransition(R.anim.scale_to_30_percent, 0);
                } else if (percent >= 0.4 && percent <= 0.6) {
                    ((Activity) context).overridePendingTransition(R.anim.scale_to_60_percent, 0);
                } else if (percent > 0.6 && percent < 0.8) {
                    ((Activity) context).overridePendingTransition(R.anim.scale_to_70_percent, 0);
                } else if (percent >= 0.8) {
                    ((Activity) context).overridePendingTransition(R.anim.scale_to_80_percent, 0);
                }
            } else {
                ((Activity) context).overridePendingTransition(R.anim.scale_to_90_percent, 0);
            }


        }
    }


    public static void startBrowser(Context context, Bundle b, boolean isSystem, float percentY) {
        if (isSystem) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            if (b != null) {
                String url = b.getString("url");
                Uri content_url = Uri.parse(url);
                intent.setData(content_url);
                context.startActivity(intent);
            }
        } else {
            Intent intent = new Intent(context, CommonWebViewActivity.class);
            intent.putExtras(b);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(0, 0);
        }
    }

    @SuppressWarnings("deprecation")
    public static boolean isForegroundApp(Context context) {
        ActivityManager manager = ((ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE));
        if (Build.VERSION.SDK_INT >= 21) {
            List<ActivityManager.RunningAppProcessInfo> pis = manager.getRunningAppProcesses();
            ActivityManager.RunningAppProcessInfo topAppProcess = pis.get(0);
            if (topAppProcess != null && topAppProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {

                if (topAppProcess.processName.equalsIgnoreCase("com.changcai.buyer")) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            //getRunningTasks() is deprecated since API Level 21 (Android 5.0)
            List localList = manager.getRunningTasks(1);
            ActivityManager.RunningTaskInfo localRunningTaskInfo = (ActivityManager.RunningTaskInfo) localList.get(0);
            if ((localRunningTaskInfo.topActivity.getPackageName().equalsIgnoreCase("com.changcai.buyer"))) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }


    public static double getScreenInch(Activity context) {

        double mInch = 0;

        if (mInch != 0.0d) {
            return mInch;
        }

        try {
            int realWidth = 0, realHeight = 0;
            Display display = context.getWindowManager().getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            if (android.os.Build.VERSION.SDK_INT >= 17) {
                Point size = new Point();
                display.getRealSize(size);
                realWidth = size.x;
                realHeight = size.y;
            } else if (android.os.Build.VERSION.SDK_INT < 17
                    && android.os.Build.VERSION.SDK_INT >= 14) {
                Method mGetRawH = Display.class.getMethod("getRawHeight");
                Method mGetRawW = Display.class.getMethod("getRawWidth");
                realWidth = (Integer) mGetRawW.invoke(display);
                realHeight = (Integer) mGetRawH.invoke(display);
            } else {
                realWidth = metrics.widthPixels;
                realHeight = metrics.heightPixels;
            }

            mInch = formatDouble(Math.sqrt((realWidth / metrics.xdpi) * (realWidth / metrics.xdpi) + (realHeight / metrics.ydpi) * (realHeight / metrics.ydpi)), 1);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return mInch;
    }

    /**
     * Double类型保留指定位数的小数，返回double类型（四舍五入）
     * newScale 为指定的位数
     */
    private static double formatDouble(double d, int newScale) {
        BigDecimal bd = new BigDecimal(d);
        return bd.setScale(newScale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
