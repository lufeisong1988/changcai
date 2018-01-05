package com.changcai.buyer.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

/**
 * @author GONGGUODONG377@pingan.com.cn
 */
public class ResUtil {

    private ResUtil() {

    }

    /**
     * 从资源转换为位图
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap drawableToBitamp(Context context, int resId) {
        try {
            Drawable drawable = context.getResources().getDrawable(resId);
            int w = drawable.getIntrinsicWidth();
            int h = drawable.getIntrinsicHeight();

            Bitmap.Config config =
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565;
            Bitmap bitmap = Bitmap.createBitmap(w, h, config);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, w, h);
            drawable.draw(canvas);

            return bitmap;

        } catch (Exception ex) {

        }

        return null;
    }
}
