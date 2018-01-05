package com.changcai.buyer;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lufeisong on 2017/11/16.
 */

public class TestActivity extends Activity{
    private ViewPager vp_price;
    private List<View> priceViews = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        });
//        vp_price = (ViewPager)findViewById(R.id.vp_price);
//        for(int i = 0;i < 3;i++){
//            View view = LayoutInflater.from(this).inflate(R.layout.view_new_index_price_item,null);
//            LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll_price);
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            if(i == 0 ){
//                lp.setMargins(getResources().getDimensionPixelSize(R.dimen.dim40),getResources().getDimensionPixelSize(R.dimen.dim15),getResources().getDimensionPixelSize(R.dimen.dim20),getResources().getDimensionPixelSize(R.dimen.dim15));
//            }else if(i == 2){
//                lp.setMargins(getResources().getDimensionPixelSize(R.dimen.dim20),getResources().getDimensionPixelSize(R.dimen.dim15),getResources().getDimensionPixelSize(R.dimen.dim40),getResources().getDimensionPixelSize(R.dimen.dim15));
//            } else{
//                lp.setMargins(getResources().getDimensionPixelSize(R.dimen.dim20),getResources().getDimensionPixelSize(R.dimen.dim15),getResources().getDimensionPixelSize(R.dimen.dim20),getResources().getDimensionPixelSize(R.dimen.dim15));
//            }
//            ll.setLayoutParams(lp);
//            priceViews.add(view);
//        }
//        vp_price.setAdapter(new PriceAdapter());
    }
    class PriceAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(priceViews.get(position));
            return priceViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(priceViews.get(position));
        }

        @Override
        public float getPageWidth(int position) {
            return (float) 0.75;
        }
    }
}
