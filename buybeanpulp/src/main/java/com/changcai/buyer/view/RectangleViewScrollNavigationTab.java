package com.changcai.buyer.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.changcai.buyer.R;
import com.changcai.buyer.view.indicator.NavigatorHelper;
import com.changcai.buyer.view.indicator.commonnavigator.abs.CommonNavigatorAdapter;
import com.changcai.buyer.view.indicator.commonnavigator.abs.IPagerTitleView;

/**
 * Created by liuxingwei on 2017/6/21.
 * <p>
 * 包含title 和  指示器indicator
 */

public class RectangleViewScrollNavigationTab extends FrameLayout {

    private HorizontalScrollView mScrollView;
    private LinearLayout mTitleContainer;
    private LinearLayout mIndicatorContainer;
    private CommonNavigatorAdapter mAdapter;
    private NavigatorHelper mNavigatorHelper;

    private ScrollNavigationTabFunction scrollNavigationTabFunction;

    /**
     *
     */
    private boolean mReselectWhenLayout = true; // PositionData准备好时，是否重新选中当前页，为true可保证在极端情况下指示器状态正确
    private int mRightPadding;
    private int mLeftPadding;
    private float mScrollPivotX = 0.5f; // 滚动中心点 0.0f - 1.0f
    private boolean mAdjustMode;

    public RectangleViewScrollNavigationTab(@NonNull Context context) {
        super(context);
        mNavigatorHelper = new NavigatorHelper();
    }


    private void init() {
        removeAllViews();
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.scroll_navigation_tab, this);

        mScrollView = (HorizontalScrollView) rootView.findViewById(R.id.scroll_view);   // mAdjustMode为true时，mScrollView为null

        mTitleContainer = (LinearLayout) rootView.findViewById(R.id.title_container);
        mTitleContainer.setPadding(mLeftPadding, 0, mRightPadding, 0);

        mIndicatorContainer = (LinearLayout) rootView.findViewById(R.id.indicator_container);

        initTitlesAndIndicator();
    }

    private void initTitlesAndIndicator() {
        for (int i = 0, j = mNavigatorHelper.getTotalCount(); i < j; i++) {
            IPagerTitleView v = mAdapter.getTitleView(getContext(), i);
            if (v instanceof View) {
                View view = (View) v;
                LinearLayout.LayoutParams lp;
                if (mAdjustMode) {
                    lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                    lp.weight = mAdapter.getTitleWeight(getContext(), i);
                } else {
                    lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
                }
                mTitleContainer.addView(view, lp);
            }
        }
//        if (mAdapter != null) {
//            scrollNavigationTabFunction = mAdapter.getIndicator(getContext());
//            if (scrollNavigationTabFunction instanceof View) {
//                LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                mIndicatorContainer.addView((View) scrollNavigationTabFunction, lp);
//            }
//        }
    }

    public void setAdapter(CommonNavigatorAdapter adapter) {
        if (mAdapter == adapter) {
            return;
        }
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mObserver);
        }
        mAdapter = adapter;
        if (mAdapter != null) {
            mAdapter.registerDataSetObserver(mObserver);
            mNavigatorHelper.setTotalCount(mAdapter.getCount());
            if (mTitleContainer != null) {  // adapter改变时，应该重新init，但是第一次设置adapter不用，onAttachToMagicIndicator中有init
                mAdapter.notifyDataSetChanged();
            }
        } else {
            mNavigatorHelper.setTotalCount(0);
            init();
        }
    }

    private DataSetObserver mObserver = new DataSetObserver() {

        @Override
        public void onChanged() {
            mNavigatorHelper.setTotalCount(mAdapter.getCount());    // 如果使用helper，应始终保证helper中的totalCount为最新
            init();
        }

        @Override
        public void onInvalidated() {
            // 没什么用，暂不做处理
        }
    };
}
