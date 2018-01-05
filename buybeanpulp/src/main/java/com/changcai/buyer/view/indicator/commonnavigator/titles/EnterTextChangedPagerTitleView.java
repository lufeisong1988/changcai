package com.changcai.buyer.view.indicator.commonnavigator.titles;

import android.content.Context;

/**
 * Created by liuxingwei on 2017/7/28.
 */

public class EnterTextChangedPagerTitleView extends SimplePagerTitleView {
    public EnterTextChangedPagerTitleView(Context context) {
        super(context);
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
        super.onEnter(index, totalCount, enterPercent, leftToRight);
        setTextColor(mSelectedColor);
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
        super.onLeave(index, totalCount, leavePercent, leftToRight);
        setTextColor(mNormalColor);
    }
}
