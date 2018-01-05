package com.changcai.buyer.business_logic.about_buy_beans.recharge_result;

import android.view.View;

import com.changcai.buyer.BaseCompatActivity;
import com.changcai.buyer.R;
import com.changcai.buyer.util.ActivityInjectUtils;

/**
 * Created by liuxingwei on 2017/1/8.
 */

public class RechargeResultActivity extends BaseCompatActivity {

    RechargeResultPresenter rechargeResultPresenter;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void injectFragmentView() {
        RechargeResultFragment rechargeResultFragment = (RechargeResultFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (rechargeResultFragment == null){
            rechargeResultFragment = new RechargeResultFragment();
            rechargeResultFragment.setArguments(getIntent().getExtras());
            ActivityInjectUtils.addFragmentToActivity(getSupportFragmentManager(),rechargeResultFragment,R.id.contentFrame);
        }
        rechargeResultPresenter = new RechargeResultPresenter(rechargeResultFragment);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected int getTitleTextColor() {
        return getResources().getColor(R.color.black);
    }

    @Override
    protected int getNavigationVisible() {
        return View.VISIBLE;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected int getToolBarBackgroundColor() {
        return getResources().getColor(R.color.white);
    }

    @Override
    protected int getNavigationIcon() {
        return R.drawable.icon_nav_back;
    }

    @Override
    protected int getTitleText() {
        return R.string.recharge_result;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_authenticate;
    }
}
