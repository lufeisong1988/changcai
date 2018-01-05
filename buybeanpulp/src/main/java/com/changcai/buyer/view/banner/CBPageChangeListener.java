package com.changcai.buyer.view.banner;

import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sai on 15/7/29.
 * 翻页指示器适配器
 * <p>
 * imported by GONGGUODONG377@pingan.com.cn
 */
public class CBPageChangeListener implements ViewPager.OnPageChangeListener {
    private ArrayList<ImageView> pointViews;
    private Bitmap[] indicator;
    private ViewPager.OnPageChangeListener onPageChangeListener;

    public CBPageChangeListener(ArrayList<ImageView> pointViews, Bitmap indicator[]) {
        this.pointViews = pointViews;
        this.indicator = indicator;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (onPageChangeListener != null) onPageChangeListener.onPageScrollStateChanged(state);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (onPageChangeListener != null)
            onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int index) {
        for (int i = 0; i < pointViews.size(); i++) {
            pointViews.get(index).setImageBitmap(indicator[1]);
            if (index != i) {
                pointViews.get(i).setImageBitmap(indicator[0]);
            }
        }
        if (onPageChangeListener != null) onPageChangeListener.onPageSelected(index);

    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }
}
