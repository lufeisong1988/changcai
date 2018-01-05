package com.changcai.buyer.business_logic.about_buy_beans.guidance;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.changcai.buyer.R;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.ui.base.BaseActivity;
import com.changcai.buyer.ui.main.MainActivity;
import com.changcai.buyer.util.ImageUtil;
import com.changcai.buyer.util.SPUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by liuxingwei on 2016/12/15.
 */

public class GuidanceActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.vp_guide)
    ViewPager vpGuide;
    //    @BindView(R.id.btn_start)
//    Button btnStart;
//    @BindView(R.id.viewGroup)
//    LinearLayout viewGroup;
    private int lastPosition;

    private Context context;
    private Integer[] mDrawableResources = {
            R.drawable.loading1,
            R.drawable.loading2,
            R.drawable.loading3,
    };
    private ViewPageAdapter viewPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_guidance);
        SPUtil.saveboolean(Constants.KEY_NOT_FIRST_OPEN_APPLICATION, true);
        ButterKnife.bind(this);
        context = this;
        initViewPagerAdapter();
        vpGuide.addOnPageChangeListener(this);
//        btnStart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                gotoActivity(MainActivity.class, true);
//            }
//        });
    }


    private void initViewPagerAdapter() {

//        Observable.from(mDrawableResources)
//                .subscribe(new Action1<Integer>() {
//                    @Override
//                    public void call(Integer integer) {
//                        ImageView imageView = new ImageView(context);
//                        imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.start_action_dots));
//                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                        layoutParams.setMargins(0, 0, 28, 0);
//                        imageView.setLayoutParams(layoutParams);
//                        viewGroup.addView(imageView);
//                    }
//                });
//
//        viewGroup.getChildAt(0).setSelected(true);

        viewPageAdapter = new ViewPageAdapter(context, mDrawableResources);
        vpGuide.setAdapter(viewPageAdapter);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
// 页面被选中
        // 设置当前页面选中
//        viewGroup.getChildAt(position).setSelected(true);
//        设置前一页不选中
//        viewGroup.getChildAt(lastPosition).setSelected(false);

                // 替换位置
                lastPosition = position;

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    class ViewPageAdapter extends PagerAdapter {

        private Context context;
        private Integer[] mDrawableResources;

        public ViewPageAdapter(Context context, Integer[] mDrawableResources) {
            this.context = context;
            this.mDrawableResources = mDrawableResources;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            imageView.setImageDrawable(ContextCompat.getDrawable(context, mDrawableResources[position]));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setAdjustViewBounds(true);
            container.addView(imageView);
            if (position == mDrawableResources.length - 1) {
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoActivity(MainActivity.class, true);
                    }
                });
            }
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }


        @Override
        public int getCount() {
            return mDrawableResources.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
