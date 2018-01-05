package com.changcai.buyer.business_logic.about_buy_beans.set_paypassword;

import android.os.Build;
import android.view.View;

import com.changcai.buyer.CompatTouchBackActivity;
import com.changcai.buyer.R;
import com.changcai.buyer.util.ActivityInjectUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by liuxingwei on 2016/11/24.
 */

public class SetPayPasswordActivity extends CompatTouchBackActivity{

    SetPayPasswordPresenter setPayPasswordPresenter;


    @Override
    protected void injectFragmentView() {
        SetPayPasswordFragment setPayPasswordFragment = (SetPayPasswordFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (null == setPayPasswordFragment) {
            setPayPasswordFragment = SetPayPasswordFragment.getInstance();
            setPayPasswordFragment.setArguments(getIntent().getExtras());
            ActivityInjectUtils.addFragmentToActivity(getSupportFragmentManager(), setPayPasswordFragment, R.id.contentFrame);
        }
        setPayPasswordPresenter = new SetPayPasswordPresenter(setPayPasswordFragment);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
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
        return R.string.set_pay_password;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_authenticate;
    }
}
