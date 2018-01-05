package com.changcai.buyer.business_logic.about_buy_beans.pay_record;

import android.opengl.Visibility;
import android.view.View;

import com.changcai.buyer.BaseCompatActivity;
import com.changcai.buyer.R;
import com.changcai.buyer.R2;
import com.changcai.buyer.util.ActivityInjectUtils;

/**
 * Created by liuxingwei on 2017/4/6.
 */

public class PayRecordListActivity extends BaseCompatActivity {

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void injectFragmentView() {
        PayRecordFragmentList payRecordFragmentList = (PayRecordFragmentList) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (payRecordFragmentList == null){
            payRecordFragmentList = new PayRecordFragmentList();
            payRecordFragmentList.setArguments(getIntent().getExtras());
            ActivityInjectUtils.addFragmentToActivity(getSupportFragmentManager(),payRecordFragmentList,R.id.contentFrame);
        }
        PayRecordPresenter pointPricePresenter = new PayRecordPresenter(payRecordFragmentList);
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
        return R.string.pay_record;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_authenticate;
    }
}
