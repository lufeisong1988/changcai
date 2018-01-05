package com.changcai.buyer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.changcai.buyer.R;

/**
 * Created by liuxingwei on 2017/5/27.
 */

public class Bulk extends View{
    private  Paint _paint;
    public Bulk(Context context) {
        super(context);init();
    }

    public Bulk(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);init();
    }

    public Bulk(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Bulk(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

   private void init(){
    setWillNotDraw(false);
    }
    @SuppressWarnings("deprecation")
    @Override
    protected void onDraw(Canvas canvas) {
        _paint = new Paint();
        _paint.setColor(getContext().getResources().getColor(R.color.global_blue));
        _paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(new Rect(getLeft(),getTop(),getLeft()+5,getTop()+13),_paint);
    }
}
