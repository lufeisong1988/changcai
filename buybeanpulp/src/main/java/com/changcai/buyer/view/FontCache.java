package com.changcai.buyer.view;

import android.content.Context;
import android.graphics.Typeface;

import java.io.File;
import java.util.HashMap;

/**
 * Created by liuxingwei on 2017/5/25.
 */

public class FontCache {

    private static HashMap<String, Typeface> fontCache = new HashMap<>();
    /**
     * 字体库文件夹名
     */
    private static String folderName = "fonts";

    /**
     * 防止每个自定义的textView频繁读取assets文件cache在hashMap中
     * @param fontname
     * @param context
     * @return
     */
    public static Typeface getTypeface(String fontname, Context context) {
        Typeface typeface = fontCache.get(fontname);

        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), folderName.concat(File.separator).concat(fontname));
            } catch (Exception e) {
                return null;
            }
            fontCache.put(fontname, typeface);
        }

        return typeface;
    }
}
