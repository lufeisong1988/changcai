package com.changcai.buyer.business_logic.about_buy_beans.about_us;

import android.os.Build;
import android.view.View;

import com.changcai.buyer.CompatTouchBackActivity;
import com.changcai.buyer.R;
import com.changcai.buyer.util.ActivityInjectUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by liuxingwei on 2016/11/23.
 */

public class AboutBuyBeansActivity extends CompatTouchBackActivity {

    AboutBuyBeansPresenter buyBeansPresenter;

    @Override
    protected void injectFragmentView() {

        AboutBuyBeansFragment aboutBuyBeansFragment = (AboutBuyBeansFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (null == aboutBuyBeansFragment){
            aboutBuyBeansFragment = new AboutBuyBeansFragment();
            ActivityInjectUtils.addFragmentToActivity(getSupportFragmentManager(),aboutBuyBeansFragment,R.id.contentFrame);
        }
        buyBeansPresenter = new AboutBuyBeansPresenter(aboutBuyBeansFragment);


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
        return R.string.about_app;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_authenticate;
    }
}
