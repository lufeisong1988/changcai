package com.changcai.buyer.util;

import android.content.Context;
import android.widget.Toast;

import com.changcai.buyer.CommonApplication;

/**
 * Created by liuxingwei on 2017/1/24.
 */

public class ToastUtil {

    public static void showLong(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showLong(Context context, int msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showLong(String msg) {
        Toast.makeText(CommonApplication.getInstance().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    public static void showLong(int msg) {
        Toast.makeText(CommonApplication.getInstance().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    public static void showShort(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(Context context, int msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(String msg) {
        Toast.makeText(CommonApplication.getInstance().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(int msg) {
        Toast.makeText(CommonApplication.getInstance().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
