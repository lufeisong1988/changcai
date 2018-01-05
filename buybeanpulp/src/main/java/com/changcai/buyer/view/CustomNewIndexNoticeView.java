package com.changcai.buyer.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.NoticeBean;
import com.changcai.buyer.util.AndroidUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

/**
 * Created by lufeisong on 2017/11/14.
 */

public class CustomNewIndexNoticeView extends LinearLayout {
    private Context context;
    private VerticalViewPager vp_custom_notice;
    private NoticeAdapter adapter;
    private boolean turning = true;
    private AdSwitchTask adSwitchTask;
    private long autoTurningTime = 4000;
    private final int MULTIPLE_COUNT = 300;
    private NoticeListener noticeListener;


    private List<View> views = new ArrayList<>();
    public CustomNewIndexNoticeView(Context context) {
        super(context);
        init(context);
    }

    public CustomNewIndexNoticeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomNewIndexNoticeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    void init(final Context context){
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.view_custom_new_index_notice,this,true);
        vp_custom_notice = (VerticalViewPager) view.findViewById(R.id.vp_custom_notice);
        vp_custom_notice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        vp_custom_notice.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                ToastUtil.showLong(context,"position = " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    public void setListener(NoticeListener noticeListener){
        this.noticeListener = noticeListener;
    }
    public void update(final List<NoticeBean.NoticeListBean> noticeList){
        if(adSwitchTask != null){
            adSwitchTask.runAble = false;
        }
//        if(noticeList == null || noticeList.size() == 0){
//            return;
//        }
        adapter = new NoticeAdapter(noticeList);
        vp_custom_notice.setAdapter(adapter);
        CustomSpeedScroller scroller = new CustomSpeedScroller(context);
        scroller.setScrollDuration(2000);
        scroller.initViewPagerScroll(vp_custom_notice);//这个是设置切换过渡时间为2秒

        adSwitchTask = new AdSwitchTask(this);

        if(noticeList.size() > 1){
            postDelayed(adSwitchTask, autoTurningTime);
        }
    }

    class NoticeAdapter extends PagerAdapter{
        List<NoticeBean.NoticeListBean> noticeList = new ArrayList<>();

        public NoticeAdapter(List<NoticeBean.NoticeListBean> noticeList) {
            this.noticeList = noticeList;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            View noticeView = LayoutInflater.from(context).inflate(R.layout.view_custom_new_index_notice_item,null,false);
            TextView tv1 = (TextView) noticeView.findViewById(R.id.tv_notice);

            tv1.setGravity(Gravity.CENTER_VERTICAL);
            if(noticeList.size() >  0) {
                final int index = position % noticeList.size();
                tv1.setText(noticeList.get(index).getTitle());

                noticeView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!TextUtils.isEmpty(noticeList.get(index).getUrl())){
                            Bundle b = new Bundle();
                            b.putString("url", noticeList.get(index).getUrl());
                            b.putString("title", noticeList.get(index).getTitle());
                            b.putString("bannerTitle",noticeList.get(index).getTitle());
                            b.putString("info","");
                            AndroidUtil.startBrowser(context, b, false);
                            noticeListener.noticeCallback();
                        }
                    }
                });
            }

            container.addView(noticeView);
            return noticeView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
    static class AdSwitchTask implements Runnable {
        public  boolean runAble = true;
        private final WeakReference<CustomNewIndexNoticeView> reference;

        AdSwitchTask(CustomNewIndexNoticeView customNewIndexNoticeView) {
            this.reference = new WeakReference<CustomNewIndexNoticeView>(customNewIndexNoticeView);
        }

        @Override
        public void run() {
            CustomNewIndexNoticeView customNewIndexNoticeView = reference.get();

            if (customNewIndexNoticeView != null && runAble) {
                if (customNewIndexNoticeView.vp_custom_notice != null && customNewIndexNoticeView.turning) {
                    if (customNewIndexNoticeView.getVisibility() == VISIBLE) {
                        int page = customNewIndexNoticeView.vp_custom_notice.getCurrentItem();
//                        if(page == customNewIndexNoticeView.views.size() - 1){
//                            page = 0;
//                        }else{
//                            page = page + 1;
//                        }
                        customNewIndexNoticeView.vp_custom_notice.setCurrentItem(page + 1);
//                        customNewIndexNoticeView.vp_custom_notice.setCurrentItem(page,true);
                    }
                    customNewIndexNoticeView.postDelayed(customNewIndexNoticeView.adSwitchTask, customNewIndexNoticeView.autoTurningTime);
                }
            }
        }
    }
    public interface NoticeListener{
        void noticeCallback();
    }
}
