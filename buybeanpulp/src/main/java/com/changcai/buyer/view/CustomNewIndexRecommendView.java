package com.changcai.buyer.view;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.RecommendListBean;
import com.changcai.buyer.util.AndroidUtil;
import com.changcai.buyer.util.DateUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by lufeisong on 2017/11/17.
 */

public class CustomNewIndexRecommendView extends LinearLayout {
    private Context context;
    private LinearLayout ll_recommend;//为你推荐
    private CustomFontTextView tv_event, tv_time;
    private TradeListener tradeListener;

    public CustomNewIndexRecommendView(Context context) {
        super(context);
        init(context);
    }

    public CustomNewIndexRecommendView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomNewIndexRecommendView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.view_custom_new_index_recommend, this);
        ll_recommend = (LinearLayout) view.findViewById(R.id.ll_recommend);
        tv_event = (CustomFontTextView) view.findViewById(R.id.tv_event);
        tv_time = (CustomFontTextView) view.findViewById(R.id.tv_time);
        tv_time.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tradeListener.tradeCallback();
            }
        });
        tv_event.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tradeListener.tradeCallback();
            }
        });
    }

    public void setListener(TradeListener tradeListener) {
        this.tradeListener = tradeListener;
    }

    //更新为你推荐
    public void updateRecommend(final List<RecommendListBean.RecommendationListBean> recommendationList) {
        ll_recommend.removeAllViews();
        for (int i = 0; i < recommendationList.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_custom_new_index_recommend_item, null);
            TextView tv_recommend_info = (TextView) view.findViewById(R.id.tv_recommend_info);
            View line_one = view.findViewById(R.id.line_one);
            TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
            if (i == 0) {
                line_one.setVisibility(INVISIBLE);
            }
            tv_time.setText(recommendationList.get(i).getShowTime());
            tv_recommend_info.setText(recommendationList.get(i).getTitle());
            ll_recommend.addView(view);
            final int finalI = i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(recommendationList.get(finalI).getUrl())) {
                        Bundle b = new Bundle();
                        b.putString("url", recommendationList.get(finalI).getUrl());
                        b.putString("title", recommendationList.get(finalI).getTitle());
                        b.putString("bannerTitle", recommendationList.get(finalI).getTitle());
                        b.putString("info", "");
                        AndroidUtil.startBrowser(context, b, false);
                        tradeListener.recommendCallback();
                    }

                }
            });
        }
    }

    public void showTradeEventView(Date date) {
        tv_time.setText(DateUtil.dateToString("MM月dd日", date));
        tv_event.setVisibility(View.VISIBLE);
    }

    public void hideTradeEventView() {
        tv_event.setVisibility(View.INVISIBLE);
    }

    public interface TradeListener {
        void tradeCallback();

        void recommendCallback();
    }
}
