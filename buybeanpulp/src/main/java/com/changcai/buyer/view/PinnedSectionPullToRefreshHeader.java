package com.changcai.buyer.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.util.DateUtil;
import com.changcai.buyer.view.indicator.VerticalProgressDotsView;
import com.jingchen.pulltorefresh.PullToRefreshLayout;

/**
 * Created by lufeisong on 2017/8/31.
 */

public class PinnedSectionPullToRefreshHeader extends RelativeLayout implements PullToRefreshLayout.OnPullProcessListener {
    String TAG = "customHeader";
    private Context context;

    //	private ImageView mArrowImageView;
    private VerticalProgressDotsView mProgressBar;//动态图
    private LinearLayout refreshIngLayout;//下拉刷新，松开刷新 ll
    private TextView refreshIngTextView;//下拉刷新，松开刷新  tv
    private LinearLayout refreshCompleteLayout;//刷新结束
    private TextView refreshCompleteText;

    public PinnedSectionPullToRefreshHeader(Context context) {
        super(context);
        this.context = context;
        ViewGroup.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view =  LayoutInflater.from(context).inflate(R.layout.xlistview_header_2,null);
        view.setLayoutParams(lp);
        mProgressBar = (VerticalProgressDotsView) view.findViewById(R.id.xlistview_header_progressbar);
        refreshIngLayout = (LinearLayout)view. findViewById(R.id.xlistview_header_text);
        refreshIngTextView = (TextView)view. findViewById(R.id.xlistview_header_hint_textview);
        refreshCompleteLayout = (LinearLayout)view. findViewById(R.id.ll_refresh_complete);
        refreshCompleteText = (TextView) view.findViewById(R.id.tv_refresh);
        this.addView(view);

    }


    @Override
    public void onPrepare(View v, int which) {
        Log.i(TAG,"onPrepare");
    }

    @Override
    public void onStart(View v, int which) {
        Log.i(TAG,"onStart");
        refreshIngLayout.setVisibility(VISIBLE);
        refreshCompleteLayout.setVisibility(GONE);
        refreshIngTextView.setText("松开刷新");
    }

    @Override
    public void onHandling(View v, int which) {
        Log.i(TAG,"onHandling");
        refreshIngLayout.setVisibility(VISIBLE);
        refreshCompleteLayout.setVisibility(GONE);
        refreshIngTextView.setText("刷新中");
        mProgressBar.setAllDotsWhite();
        mProgressBar.bottomToTop();
    }

    @Override
    public void onFinish(View v, int which) {
        Log.i(TAG,"onFinish");
//        mProgressBar.refreshComplete(true);
        refreshIngLayout.setVisibility(GONE);
        mProgressBar.setVisibility(GONE);
        refreshCompleteLayout.setVisibility(VISIBLE);
        refreshCompleteText.setText("最后更新 " + DateUtil.getStringDate());
    }

    @Override
    public void onPull(View v, float pullDistance, int which) {
        Log.i(TAG,"onPull" + pullDistance);
        mProgressBar.setVisibility(VISIBLE);
        refreshIngLayout.setVisibility(VISIBLE);
        refreshCompleteLayout.setVisibility(GONE);
        setDotsBackground(pullDistance);
        if(pullDistance < getHeight()){
            refreshIngTextView.setText("下拉刷新");
        }





    }
    /**
     * 计算百分比动态的显示和隐藏
     * @param visibleHeightPercent
     */
    private void setDotsBackground(float visibleHeightPercent) {
        if (visibleHeightPercent < getHeight() / 3) {
            mProgressBar.setAllDotsWhite();
        } else if (visibleHeightPercent > getHeight() / 3  && visibleHeightPercent < getHeight() / 3 * 2) {
            mProgressBar.setBottomState();
        } else if (visibleHeightPercent > getHeight() / 3 * 2 && visibleHeightPercent < getHeight()) {
            mProgressBar.setCenterState();
        } else if (visibleHeightPercent >= getHeight()) {
            mProgressBar.setAllDotsBlack();
        }
    }
}
