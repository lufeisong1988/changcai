/**
 * @file XListViewHeader.java
 * @create Apr 18, 2012 5:22:27 PM
 * @author Maxwin
 * @description XListView's header
 */
package com.changcai.buyer.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.view.indicator.VerticalProgressDotsView;


public class XListViewHeader extends LinearLayout {
    private LinearLayout mContainer;
    //	private ImageView mArrowImageView;
    private VerticalProgressDotsView mProgressBar;
    private TextView mHintTextView;
    private LinearLayout mRefreshTimeView;
    private int mState = STATE_NORMAL;
    private LinearLayout timeLayout;

//	private Animation mRotateUpAnim;
//	private Animation mRotateDownAnim;

    private final int ROTATE_ANIM_DURATION = 180;

    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_REFRESHING = 2;


    private String mReadyStateText = getContext().getString(R.string.xlistview_header_hint_ready);
    private LinearLayout llRefreshComplete;

    private TextView textViewComplete;


    public XListViewHeader(Context context) {
        super(context);
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public XListViewHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        // 初始情况，设置下拉刷新view高度为0
        LayoutParams lp = new LayoutParams(
                LayoutParams.FILL_PARENT, 0);
        mContainer = (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.xlistview_header_2, null);
        addView(mContainer, lp);
        setGravity(Gravity.BOTTOM);

        mHintTextView = (TextView) findViewById(R.id.xlistview_header_hint_textview);
        mProgressBar = (VerticalProgressDotsView) findViewById(R.id.xlistview_header_progressbar);
        mRefreshTimeView = (LinearLayout) findViewById(R.id.xlistview_header_refresh_time_layout);
        timeLayout = (LinearLayout) findViewById(R.id.xlistview_header_text);
        llRefreshComplete = (LinearLayout) findViewById(R.id.ll_refresh_complete);
        textViewComplete = (TextView) findViewById(R.id.tv_refresh);
    }

    /**
     * set visibility of refresh layout
     *
     * @param shouldShow
     */
    public void setRefreshTimeViewVisibility(boolean shouldShow) {
//        if (mRefreshTimeView != null) {
//            mRefreshTimeView.setVisibility(shouldShow ? View.VISIBLE : View.GONE);
//        }
        mRefreshTimeView.setVisibility(GONE);
    }

    public void setState(int state, float mVisibleHeightPercent) {
        setDotsBackground(mVisibleHeightPercent);

        if (state == mState) return;

        if (state == STATE_REFRESHING) {
//			mArrowImageView.setVisibility(GONE);// 显示进度
//			mArrowImageView.clearAnimation();
//			mArrowImageView.setVisibility(View.INVISIBLE);
            mProgressBar.setAllDotsWhite();
            mProgressBar.bottomToTop();
//			mProgressBar.setVisibility(View.VISIBLE);
        } else {    // 显示箭头图片
//			mArrowImageView.setVisibility(View.VISIBLE);
//			mProgressBar.setVisibility(View.INVISIBLE);
//			mArrowImageView.setVisibility(GONE);

            mProgressBar.refreshComplete(true);
        }

        switch (state) {
            case STATE_NORMAL:
                if (mState == STATE_READY) {
//				mArrowImageView.startAnimation(mRotateDownAnim);
                }
                if (mState == STATE_REFRESHING) {
//				mArrowImageView.clearAnimation();
                    mProgressBar.refreshComplete(true);

                }
                mHintTextView.setText(R.string.xlistview_header_hint_normal);
                break;
            case STATE_READY:
                if (mState != STATE_READY) {
//				mArrowImageView.clearAnimation();
//				mArrowImageView.startAnimation(mRotateUpAnim);
                    mHintTextView.setText(mReadyStateText);
                }
                break;
            case STATE_REFRESHING:
                mHintTextView.setText(R.string.xlistview_header_hint_loading);

                break;
            default:
        }

        mState = state;
    }

    public boolean isRefreshing() {
        return mState == STATE_REFRESHING;
    }

    /**
     * 设置下拉头有效高度
     *
     * @param height
     */
    public void setVisiableHeight(int height) {
        if (height < 0)
            height = 0;
        LayoutParams lp = (LayoutParams) mContainer
                .getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    /**
     * 获得下拉头有效高度
     *
     * @return
     */
    public int getVisiableHeight() {
        return mContainer.getLayoutParams().height;
    }

    /**
     * 设置【下拉刷新、正在加载】字体颜色
     *
     * @param color
     */
    public void setHeaderTextColor(int color) {
        mHintTextView.setTextColor(color);
    }

    public void setHeaderBgNewStyle() {
        mContainer.setBackgroundColor(getResources().getColor(R.color.white80));
        setHeaderTextColor(getResources().getColor(R.color.black));
    }

    public void setHeaderBgDefaultStyle() {
        mContainer.setBackgroundColor(getResources().getColor(R.color.white80));
        setHeaderTextColor(getResources().getColor(R.color.black));
    }

    /**
     * 计算百分比动态的显示和隐藏
     * @param visibleHeightPercent
     */
    private void setDotsBackground(float visibleHeightPercent) {
        if (visibleHeightPercent < 0.3f) {
            mProgressBar.setAllDotsWhite();
        } else if (visibleHeightPercent > 0.5f && visibleHeightPercent < 0.7f) {
            mProgressBar.setBottomState();
        } else if (visibleHeightPercent > 0.8f && visibleHeightPercent < 0.9f) {
            mProgressBar.setCenterState();
        } else if (visibleHeightPercent >= 1f) {
            mProgressBar.setAllDotsBlack();
        }
    }

    /**
     * 设置下拉刷新显示文字
     *
     * @param text
     */
    public void setReadyStateText(String text) {
        mReadyStateText = text;
    }

    public void setRefreshComplete(String completeText) {
        if (completeText != null) {
            textViewComplete.setText(completeText);
        }
        mProgressBar.setVisibility(GONE);
        timeLayout.setVisibility(GONE);
        llRefreshComplete.setVisibility(VISIBLE);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(llRefreshComplete, llRefreshComplete.ALPHA, 0, 1);
        objectAnimator.setDuration(600);
        objectAnimator.start();
    }

    public void resetRefreshViewVisible() {
        llRefreshComplete.setVisibility(GONE);
        mProgressBar.setVisibility(VISIBLE);
        timeLayout.setVisibility(VISIBLE);
        textViewComplete.setText("已刷新");
    }

    public void setHeaderMargin(int left,  int top,  int right,  int bottom) {
        LinearLayout.LayoutParams layoutParams = (LayoutParams) mContainer.getLayoutParams();
        layoutParams.setMargins(left,top,right,bottom);
    }
}
