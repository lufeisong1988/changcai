package com.changcai.buyer.ui;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.changcai.buyer.R;
import com.changcai.buyer.ui.base.BaseActivity;
import com.changcai.buyer.ui.main.MainActivity;

import java.util.ArrayList;

/**
 * @author zhoujun
 * @version 1.1
 * @description 引导页
 * @date 2014年7月29日 上午11:43:51
 */
public class HelperActivity extends BaseActivity {
    private ViewPager vpPager;
    private ArrayList<View> alPages;
    // 包裹滑动图片LinearLayout
    private ViewGroup vgWelcomepage;
    // 包裹小圆点的LinearLayout
    private ViewGroup vgWelcomeflag;
    private boolean misScrolled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
    }

    // 指引页面数据适配器
    class GuidePageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return alPages.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(alPages.get(arg1));
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            try {
                ((ViewPager) arg0).addView(alPages.get(arg1));
            } catch (Exception e) {

            }
            return alPages.get(arg1 % alPages.size());
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

        @Override
        public void finishUpdate(View arg0) {
        }
    }


    class GuidePageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case ViewPager.SCROLL_STATE_DRAGGING:
                    misScrolled = false;
                    break;
                case ViewPager.SCROLL_STATE_SETTLING:
                    misScrolled = true;
                    break;
                case ViewPager.SCROLL_STATE_IDLE:
                    if (vpPager.getCurrentItem() == vpPager.getAdapter().getCount() - 1 && !misScrolled) {
                        jumpActivity();
                    }
                    misScrolled = true;
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            changeState(arg0);
        }
    }

    /**
     * 改变原点的状态
     *
     * @param position
     */
    private void changeState(int position) {
        int pos = position;
        int count = vgWelcomeflag.getChildCount();
//        for (int i = 0; i < count; i++) {
//            ImageView ivItem = (ImageView) vgWelcomeflag.getChildAt(i);
//            if (i == pos) {
//                ivItem.setImageResource(R.mipmap.point_select);
//            } else {
//                ivItem.setImageResource(R.mipmap.point_normal);
//            }
//        }
    }


    private void init() {
        LayoutInflater inflater = getLayoutInflater();
//        alPages = new ArrayList<View>();
//        alPages.add(inflater.inflate(R.layout.help_1, null));
//        alPages.add(inflater.inflate(R.layout.help_2, null));
//        alPages.add(inflater.inflate(R.layout.help_3, null));
//
//        vgWelcomepage = (ViewGroup) inflater.inflate(R.layout.activity_help, null);
//
//        vgWelcomeflag = (ViewGroup) vgWelcomepage.findViewById(R.id.viewGroup);
//        vpPager = (ViewPager) vgWelcomepage.findViewById(R.id.guidePages);
//        for (int i = 0; i < alPages.size(); i++) {
//            ImageView ivRound = new ImageView(this);
//            LinearLayout.LayoutParams pRound = new LinearLayout.LayoutParams(
//                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//            pRound.leftMargin = 10;
//            pRound.rightMargin = 10;
//            ivRound.setLayoutParams(pRound);
//            if (i == 0) {
//                ivRound.setImageResource(R.mipmap.point_select);
//            } else {
//                ivRound.setImageResource(R.mipmap.point_normal);
//            }
//
//            vgWelcomeflag.addView(ivRound, i);
//
//        }
//
//        setContentView(vgWelcomepage);
//
//        vpPager.setAdapter(new GuidePageAdapter());
//        vpPager.setOnPageChangeListener(new GuidePageChangeListener());
//        vpPager.setCurrentItem(0);

    }

    /**
     * 跳转界面
     */
    private void jumpActivity() {
//		if(isLogin){
//			gotoActivity(MainActivity.class,true);
//		}else{
//			gotoActivity(LoginActivity.class,true);
//		}
        gotoActivity(MainActivity.class, true);
    }
}
