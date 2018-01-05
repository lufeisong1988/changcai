package com.changcai.buyer.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;

import com.changcai.buyer.CommonApplication;
import com.changcai.buyer.common.Constants;
import com.umeng.analytics.MobclickAgent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


/**
 * Created by liuxingwei on 2016/11/26.
 */

public class SPUtil {

    private static String PreferenceName = "Constant";

    /**
     * 缓存对象
     *
     * @param key
     * @param t
     * @param <T>
     * @return
     */
    public static <T> boolean saveObjectToShare(String key, T t) {
        return saveObjectToShare(CommonApplication.getInstance(), PreferenceName, key, t);
    }

    /**
     * 缓存对象
     *
     * @param context
     * @param key
     * @param t
     * @param <T>
     * @return
     */
    public static <T> boolean saveObjectToShare(Context context, String key, T t) {
        return saveObjectToShare(context, PreferenceName, key, t);
    }

    /**
     * 缓存对象
     *
     * @param context
     * @param name
     * @param key
     * @param t
     * @param <T>
     * @return
     */
    public static <T> boolean saveObjectToShare(Context context, String name, String key, T t) {
        try {
            SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            if (t == null) {
                editor.putString(key, "");
                SharedPreferencesCompat.apply(editor);
                return true;
            }
            ByteArrayOutputStream toByte = new ByteArrayOutputStream();
            ObjectOutputStream oos;

            oos = new ObjectOutputStream(toByte);
            oos.writeObject(t);
            String payCityMapBase64 = new String(Base64.encode(toByte.toByteArray(), Base64.DEFAULT));
            editor.putString(key, payCityMapBase64);
            SharedPreferencesCompat.apply(editor);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            MobclickAgent.reportError(context, e.getLocalizedMessage() + "SPUtil-IOException");
            return false;
        }
    }

    /**
     * 从缓存中获得存储的对象
     *
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getObjectFromShare(String key) {
        return getObjectFromShare(CommonApplication.getInstance(), PreferenceName, key);
    }

    /**
     * 从缓存中获取存储的对象
     *
     * @param context
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getObjectFromShare(Context context, String key) {
        return getObjectFromShare(context, PreferenceName, key);
    }

    /**
     * 从缓存中获取存储的对象
     *
     * @param context
     * @param name
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getObjectFromShare(Context context, String name, String key) {
        try {
            SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
            String payCityMapBase64 = sp.getString(key, "");
            if (payCityMapBase64.length() == 0) {
                return null;
            }
            byte[] base64Bytes = Base64.decode(payCityMapBase64, Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (T) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(context, "getObjectFromShare----error" + e.getMessage());
        }
        return null;
    }

    /**
     * clear SharedPreferences data
     *
     * @param key
     */
    public static void clearObjectFromShare(String key) {
        SharedPreferences.Editor editor = CommonApplication.getInstance().getSharedPreferences(PreferenceName, Context.MODE_PRIVATE).edit();
        editor.remove(key);
        editor.apply();
    }

    /**
     * 缓存字符串
     *
     * @param key
     * @param value
     */
    public static void saveString(String key, String value) {
        SharedPreferences sp = CommonApplication.getInstance().getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到缓存的字符串
     *
     * @param key
     * @return
     */
    public static String getString(String key) {
        SharedPreferences sp = CommonApplication.getInstance().getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    /**
     * 缓存boolen类型数据
     *
     * @param key
     * @param value
     */
    public static void saveboolean(String key, boolean value) {
        SharedPreferences sp = CommonApplication.getInstance().getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        SharedPreferencesCompat.apply(editor);
        ;
    }

    /**
     * 得到boolen类型数据
     *
     * @param key
     * @return
     */
    public static boolean getBoolean(String key) {
        SharedPreferences sp = CommonApplication.getInstance().getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    /**
     * 保存int类型数据
     *
     * @param key
     * @param value
     */
    public static void saveInt(String key, int value) {
        SharedPreferences sp = CommonApplication.getInstance().getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        SharedPreferencesCompat.apply(editor);
        ;
    }

    /**
     * 得到int类型数据
     *
     * @param key
     * @return
     */
    public static int getInt(String key) {
        SharedPreferences sp = CommonApplication.getInstance().getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        return sp.getInt(key, 0);
    }

    /**
     * 保存float类型数据
     *
     * @param key
     * @param value
     */
    public static void saveFloat(String key, float value) {
        SharedPreferences sp = CommonApplication.getInstance().getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(key, value);
        SharedPreferencesCompat.apply(editor);
        ;
    }

    /**
     * 得到float类型数据
     *
     * @param key
     * @return
     */
    public static float getFloat(String key) {
        SharedPreferences sp = CommonApplication.getInstance().getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        return sp.getFloat(key, 0);
    }

    /**
     * 保存long类型数据
     *
     * @param key
     * @param value
     */
    public static void saveLong(String key, long value) {
        SharedPreferences sp = CommonApplication.getInstance().getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        SharedPreferencesCompat.apply(editor);
        ;
    }

    /**
     * 得到long类型数据
     *
     * @param key
     * @return
     */
    public static long getLong(String key) {
        SharedPreferences sp = CommonApplication.getInstance().getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        return sp.getLong(key, 0);
    }

    /**
     * Manually save a Bundle object to SharedPreferences.
     *
     * @param header
     * @param bundleData
     */
    private void saveBundle(String header, Bundle bundleData) {
        SharedPreferences sp = CommonApplication.getInstance().getSharedPreferences(PreferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        Set<String> keySet = bundleData.keySet();
        Iterator<String> it = keySet.iterator();

        while (it.hasNext()) {
            String key = it.next();
            Object o = bundleData.get(key);
            if (o == null) {
                editor.remove(header + key);
            } else if (o instanceof Integer) {
                editor.putInt(header + key, (Integer) o);
            } else if (o instanceof Long) {
                editor.putLong(header + key, (Long) o);
            } else if (o instanceof Boolean) {
                editor.putBoolean(header + key, (Boolean) o);
            } else if (o instanceof CharSequence) {
                editor.putString(header + key, ((CharSequence) o).toString());
            } else if (o instanceof Bundle) {
                saveBundle(header + key, ((Bundle) o));
            }
        }

        SharedPreferencesCompat.apply(editor);
    }




    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            SharedPreferencesCompat.apply(editor);
        }
    }
}
