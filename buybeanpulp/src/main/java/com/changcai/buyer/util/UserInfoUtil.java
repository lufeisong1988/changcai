package com.changcai.buyer.util;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

/**
 * 用户信息相关
 *
 */
public class UserInfoUtil {

    // 设置cookie
    public static void synCookies(Context context, String url, String cookie) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setCookie(url, cookie);
        CookieSyncManager.getInstance().sync();
    }

    // 设置cookie
    public static String getCookies(Context context, String url) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        try {
            return cookieManager.getCookie(url);
        }catch (NullPointerException e){
            return "";
        }

    }

    // 清除cookie
    public static void removeCookie(Context context) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        CookieSyncManager.getInstance().sync();
    }
}
