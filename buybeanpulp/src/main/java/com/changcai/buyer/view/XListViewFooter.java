/**
 * @file XFooterView.java
 * @create Mar 31, 2012 9:33:43 PM
 * @author Maxwin
 * @description XListView's footer
 */
package com.changcai.buyer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changcai.buyer.R;


public class XListViewFooter extends LinearLayout {

    public final static int STATE_NORMAL = 0;//普通状态
    public final static int STATE_READY = 1;//上拉准备刷新
    public final static int STATE_LOADING = 2;//正在加载
    public final static int STATE_NO_MORE = 3;//没有更多数据
    public final static int STATE_LOAD_MORE_FAIL = 4;


    private Context mContext;

    private View mContentView;
    private RotateDotsProgressView mProgressBar;
    private TextView mHintView;

    public XListViewFooter(Context context) {
        super(context);
        initView(context);
    }

    public XListViewFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    /**
     * 设置状态
     *
     * @param state
     */
    public void setState(int state) {
        mHintView.setVisibility(View.INVISIBLE);
        mHintView.setVisibility(View.INVISIBLE);
        if (state == STATE_READY) {
            mHintView.setVisibility(View.VISIBLE);
            mHintView.setText(R.string.xlistview_footer_hint_ready);
            mProgressBar.setVisibility(View.INVISIBLE);
//            mHintView.setTextColor(getResources().getColor(R.color.storm_gray));
        } else if (state == STATE_LOADING) {
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.showAnimation(true);
            mHintView.setVisibility(View.VISIBLE);
            mHintView.setText(R.string.xlistview_footer_hint_loading);
//            mHintView.setTextColor(getResources().getColor(R.color.storm_gray));
        } else if (state == STATE_NO_MORE) {
            mHintView.setVisibility(VISIBLE);
            mHintView.setText(R.string.xlistview_footer_hint_no_more);
            mProgressBar.setVisibility(View.INVISIBLE);
//            mHintView.setTextColor(getResources().getColor(R.color.storm_gray));
        } else {
            mHintView.setVisibility(View.VISIBLE);
            mHintView.setText(R.string.xlistview_footer_hint_normal);
            mProgressBar.setVisibility(View.INVISIBLE);
//            mHintView.setTextColor(getResources().getColor(R.color.storm_gray));
//            mProgressBar.refreshDone(true);

        }
    }

    @SuppressWarnings("deprecation")
    public void setState(int state, int noMoreString) {
        mHintView.setVisibility(View.INVISIBLE);

        mHintView.setVisibility(View.INVISIBLE);
        if (state == STATE_READY) {
            mHintView.setVisibility(View.VISIBLE);
            mHintView.setText(R.string.xlistview_footer_hint_ready);
            mProgressBar.setVisibility(View.INVISIBLE);
            mHintView.setTextColor(getResources().getColor(R.color.storm_gray));
        } else if (state == STATE_LOADING) {
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.showAnimation(true);
            mHintView.setVisibility(View.VISIBLE);
            mHintView.setText(R.string.xlistview_footer_hint_loading);
            mHintView.setTextColor(getResources().getColor(R.color.storm_gray));
        } else if (state == STATE_NO_MORE) {
            mHintView.setVisibility(VISIBLE);
            mHintView.setText(noMoreString);
            mProgressBar.setVisibility(View.INVISIBLE);
            mHintView.setTextColor(getResources().getColor(R.color.storm_gray));
        } else if (state == STATE_LOAD_MORE_FAIL) {
            mHintView.setVisibility(VISIBLE);
            mHintView.setText(R.string.xlistview_footer_hint_no_more_fail);
            mProgressBar.setVisibility(View.INVISIBLE);
            mHintView.setTextColor(getResources().getColor(R.color.global_blue));
        } else {
            mHintView.setVisibility(View.VISIBLE);
            mHintView.setText(R.string.xlistview_footer_hint_normal);
            mHintView.setTextColor(getResources().getColor(R.color.storm_gray));
            mProgressBar.setVisibility(View.INVISIBLE);
            mProgressBar.refreshDone(true);

        }
    }

    /**
     * 设置footer里底部的高度
     *
     * @param height
     */
    public void setBottomMargin(int height) {
        if (height < 0) return;
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        lp.bottomMargin = height;
        mContentView.setLayoutParams(lp);
    }

    /**
     * 获得footer里底部的高度
     *
     * @return
     */
    public int getBottomMargin() {
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        return lp.bottomMargin;
    }


    /**
     * normal status
     */
    public void normal() {
        mHintView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }


    /**
     * loading status
     */
    public void loading() {
        mHintView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * hide footer when disable pull load more
     */
    public void hide() {
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        lp.height = 0;
        mContentView.setLayoutParams(lp);
    }

    /**
     * show footer
     */
    public void show() {
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        lp.height = LayoutParams.WRAP_CONTENT;
        mContentView.setLayoutParams(lp);
    }

    /**
     * 初始化布局
     *
     * @param context
     */
    @SuppressWarnings("deprecation")
    private void initView(Context context) {
        mContext = context;
        LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.xlistview_footer_2, null);
        addView(moreView);
        moreView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

        mContentView = moreView.findViewById(R.id.xlistview_footer_content);
        mProgressBar = (RotateDotsProgressView) moreView.findViewById(R.id.xlistview_footer_progressbar);
        mHintView = (CustomFontTextView) moreView.findViewById(R.id.xlistview_footer_hint_textview);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
