package com.changcai.buyer.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by liuxingwei on 2017/6/21.
 */

public class ScrollNavigationTabStrip extends FrameLayout {

    private ScrollNavigationTabFunction scrollNavigationTabFunction;

    public ScrollNavigationTabStrip(@NonNull Context context) {
        super(context);
    }

    public ScrollNavigationTabStrip(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void navigationTabOnScroll(float fraction) {
        if (scrollNavigationTabFunction != null) {
            scrollNavigationTabFunction.updateIndicatorPosition(fraction);
        }
    }

    public ScrollNavigationTabFunction getScrollNavigationTabFunction() {
        return scrollNavigationTabFunction;
    }

    public void setScrollNavigationTabFunction(ScrollNavigationTabFunction scrollNavigationTabFunction) {
        if (this.scrollNavigationTabFunction == scrollNavigationTabFunction) return;
        if (this.scrollNavigationTabFunction != null) {
            scrollNavigationTabFunction.onDetachFromViewGroup();
        }
        this.scrollNavigationTabFunction = scrollNavigationTabFunction;
        removeAllViews();

        if (scrollNavigationTabFunction instanceof View) {
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            addView((View) scrollNavigationTabFunction, layoutParams);
            scrollNavigationTabFunction.onAttachView();
        }
    }
}
