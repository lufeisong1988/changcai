package com.changcai.buyer.business_logic.about_buy_beans.deposit_and_pay;

import android.app.Activity;
import android.view.View;

import com.changcai.buyer.BaseCompatActivity;
import com.changcai.buyer.R;
import com.changcai.buyer.util.ActivityInjectUtils;

/**
 * Created by liuxingwei on 2017/3/20.
 */
public class DepositAndPayActivity extends BaseCompatActivity {

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void injectFragmentView() {

        DepositAndPayFragment depositAndPayFragment = (DepositAndPayFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (null == depositAndPayFragment) {
            depositAndPayFragment = new DepositAndPayFragment();
            ActivityInjectUtils.addFragmentToActivity(getSupportFragmentManager(), depositAndPayFragment, R.id.contentFrame);
        }
        DepositAndPayPresenter depositAndPayPresenter = new DepositAndPayPresenter(depositAndPayFragment);

        findViewById(R.id.view_bottom_line).setVisibility(View.VISIBLE);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected int getTitleTextColor() {
        return getResources().getColor(R.color.membership_color);
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
        return R.string.pay_explain;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_authenticate;
    }
}
