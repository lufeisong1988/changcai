package com.changcai.buyer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.ActionBarOverlayLayout;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.TextureView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by liuxingwei on 2017/5/25.
 *
 * 采用自定义字体库后会有位移偏差
 */

public class CustomFontTextView extends AppCompatTextView {


    public CustomFontTextView(Context context) {
        super(context);
        init(context);
    }

    /**
     * 自定义字体应用平方light字体库
     * 可以自定义属性 通过属性加载不同类型的字体
     * @param context
     */
    private void init(Context context) {
        setTypeface(FontCache.getTypeface("ping_fang_light.ttf",context));


    }



    /**
     * 修正误差3PX
     */
//    @Override
//    public void layout(@Px int l, @Px int t, @Px int r, @Px int b) {
//        super.layout(l, t, r, b+10);
//    }

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);init(context);
    }

    public CustomFontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);init(context);
    }


}
