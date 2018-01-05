package com.changcai.buyer.business_logic.about_buy_beans.point_price;

import android.view.View;

import com.changcai.buyer.BaseCompatActivity;
import com.changcai.buyer.R;
import com.changcai.buyer.util.ActivityInjectUtils;

/**
 * Created by liuxingwei on 2017/4/6.
 */

public class PointPriceListActivity extends BaseCompatActivity {


    @Override
    protected void injectFragmentView() {
        PointPriceListFragment pointPriceListFragment = (PointPriceListFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (pointPriceListFragment == null){
            pointPriceListFragment = new PointPriceListFragment();
            pointPriceListFragment.setArguments(getIntent().getExtras());
            ActivityInjectUtils.addFragmentToActivity(getSupportFragmentManager(),pointPriceListFragment,R.id.contentFrame);
        }
        PointPricePresenter pointPricePresenter = new PointPricePresenter(pointPriceListFragment);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected int getTitleTextColor() {
        return getResources().getColor(R.color.white);
    }

    @Override
    protected int getNavigationVisible() {
        return View.VISIBLE;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected int getToolBarBackgroundColor() {
        return getResources().getColor(R.color.global_blue);
    }

    @Override
    protected int getNavigationIcon() {
        return R.drawable.icon_nav_back;
    }

    @Override
    protected int getTitleText() {
        return R.string.all_point_price;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_authenticate;
    }
}
