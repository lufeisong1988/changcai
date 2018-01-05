package com.changcai.buyer.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by liuxingwei on 2017/6/29.
 */

public class MyScrollView extends ScrollView{

    private ScrollViewSlideListener scrollViewSlideListener;

    public interface ScrollViewSlideListener {
        void scrollChanged(int l, int t, int oldl, int oldt);
    }
    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setScrollViewSlideListener(ScrollViewSlideListener scrollViewSlideListener) {
        this.scrollViewSlideListener = scrollViewSlideListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollViewSlideListener !=null){
            scrollViewSlideListener.scrollChanged(l,t,oldl,oldt);
        }
    }
}
