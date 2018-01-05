package com.changcai.buyer.view.indicator.commonnavigator.titles;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.TextView;

import com.changcai.buyer.util.AndroidUtil;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.view.indicator.commonnavigator.abs.IMeasurablePagerTitleView;


/**
 * 带文本的指示器标题
 * 博客: http://hackware.lucode.net
 * Created by hackware on 2016/6/26.
 */
public class SimplePagerTitleView extends AppCompatTextView implements IMeasurablePagerTitleView {
    protected int mSelectedColor;
    protected int mNormalColor;

    /**
     * 注意padding /2
     */
    protected int padding;

    protected Typeface mSelectedTypeface;

    public SimplePagerTitleView(Context context) {
        super(context, null);
        init(context);
    }

    private void init(Context context) {
        setGravity(Gravity.CENTER);
        padding = AndroidUtil.dip2px(context, 10);
        setPadding(padding, 0, padding, 0);
        setSingleLine();
        setEllipsize(TextUtils.TruncateAt.END);
    }


    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        if (this.padding == padding) return;
        this.padding = padding;
        setPadding(this.padding, 0, this.padding, 0);
    }

    @Override
    public void onSelected(int index, int totalCount) {
        setTextColor(mSelectedColor);
        if (mSelectedTypeface != null)
            setTypeface(mSelectedTypeface);
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        setTextColor(mNormalColor);
        setNormalTypeface();
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
    }

    @Override
    public int getContentLeft() {
        Rect bound = new Rect();
        getPaint().getTextBounds(getText().toString(), 0, getText().length(), bound);
        int contentWidth = bound.width();
        return getLeft() + getWidth() / 2 - contentWidth / 2;
    }

    @Override
    public int getContentTop() {
        Paint.FontMetrics metrics = getPaint().getFontMetrics();
        float contentHeight = metrics.bottom - metrics.top;
        return (int) (getHeight() / 2 - contentHeight / 2);
    }

    @Override
    public int getContentRight() {
        Rect bound = new Rect();
        getPaint().getTextBounds(getText().toString(), 0, getText().length(), bound);
        int contentWidth = bound.width();
        return getLeft() + getWidth() / 2 + contentWidth / 2;
    }

    @Override
    public int getContentBottom() {
        Paint.FontMetrics metrics = getPaint().getFontMetrics();
        float contentHeight = metrics.bottom - metrics.top;
        return (int) (getHeight() / 2 + contentHeight / 2);
    }

    public int getSelectedColor() {
        return mSelectedColor;
    }

    public void setSelectedColor(int selectedColor) {
        mSelectedColor = selectedColor;
    }

    public int getNormalColor() {
        return mNormalColor;
    }

    public void setNormalColor(int normalColor) {
        mNormalColor = normalColor;
    }

    public void setNormalTypeface() {
        if (getTypeface() != Typeface.DEFAULT)
            this.setTypeface(Typeface.DEFAULT);
    }

    public void setSelectedTypeface(Typeface selectedTypeface) {
        this.mSelectedTypeface = selectedTypeface;
    }
}
