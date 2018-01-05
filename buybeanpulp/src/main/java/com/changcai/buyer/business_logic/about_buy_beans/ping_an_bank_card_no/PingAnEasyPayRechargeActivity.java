package com.changcai.buyer.business_logic.about_buy_beans.ping_an_bank_card_no;

import android.view.View;

import com.changcai.buyer.BaseCompatActivity;
import com.changcai.buyer.R;
import com.changcai.buyer.util.ActivityInjectUtils;
import com.changcai.buyer.util.AppManager;

/**
 * Created by liuxingwei on 2017/1/8.
 */

public class PingAnEasyPayRechargeActivity extends BaseCompatActivity {


    @Override
    public void onBackPressed() {
        AppManager.getAppManager().finishActivity(this);
        finish();
    }

    PingAnEasyPayRechargePresenter pingAnEasyPayRechargePresenter;
    @Override
    protected void injectFragmentView() {
        PingAnEasyPayRechargeFragment pingAnEasyPayRechargeFragment = (PingAnEasyPayRechargeFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (pingAnEasyPayRechargeFragment == null){
            pingAnEasyPayRechargeFragment = new PingAnEasyPayRechargeFragment();
            pingAnEasyPayRechargeFragment.setArguments(getIntent().getExtras());
            ActivityInjectUtils.addFragmentToActivity(getSupportFragmentManager(),pingAnEasyPayRechargeFragment,R.id.contentFrame);
        }
        pingAnEasyPayRechargePresenter = new PingAnEasyPayRechargePresenter(pingAnEasyPayRechargeFragment);
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
        return R.string.recharge_ping_an;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_authenticate;
    }
}
