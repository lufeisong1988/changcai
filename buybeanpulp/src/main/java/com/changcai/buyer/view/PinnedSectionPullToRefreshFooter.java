package com.changcai.buyer.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.jingchen.pulltorefresh.PullToRefreshLayout;

/**
 * Created by lufeisong on 2017/9/2.
 */

public class PinnedSectionPullToRefreshFooter extends RelativeLayout implements PullToRefreshLayout.OnPullProcessListener {
    String TAG = "customHeader";
    private Context context;

    //	private ImageView mArrowImageView;
    private RotateDotsProgressView mProgressBar;//动态图
    private TextView refreshIngTextView;//下拉刷新，松开刷新  tv

    public PinnedSectionPullToRefreshFooter(Context context) {
        super(context);
        this.context = context;
        ViewGroup.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view =  LayoutInflater.from(context).inflate(R.layout.xlistview_footer_2,null);
        view.setLayoutParams(lp);
        mProgressBar = (RotateDotsProgressView) view.findViewById(R.id.xlistview_footer_progressbar);
        refreshIngTextView = (TextView)view. findViewById(R.id.xlistview_footer_hint_textview);
        this.addView(view);

    }


    @Override
    public void onPrepare(View v, int which) {
        mProgressBar.setVisibility(GONE);
        Log.i(TAG,"onPrepare");
    }

    @Override
    public void onStart(View v, int which) {
        Log.i(TAG,"onStart");
        refreshIngTextView.setText("加载中");
        mProgressBar.setVisibility(VISIBLE);
        mProgressBar.showAnimation(true);
    }

    @Override
    public void onHandling(View v, int which) {
        Log.i(TAG,"onHandling");

    }

    @Override
    public void onFinish(View v, int which) {
        Log.i(TAG,"onFinish");
        mProgressBar.setVisibility(GONE);
        mProgressBar.refreshDone(true);
    }

    @Override
    public void onPull(View v, float pullDistance, int which) {
        Log.i(TAG,"onPull" + pullDistance);
        if(Math.abs(pullDistance) < getHeight()){
            refreshIngTextView.setText("上拉加载更多");
        }





    }

}
