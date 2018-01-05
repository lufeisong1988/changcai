package com.changcai.buyer.business_logic.about_buy_beans.authenticate;

import android.view.View;

import com.changcai.buyer.BaseCompatActivity;
import com.changcai.buyer.R;
import com.changcai.buyer.util.ActivityInjectUtils;

/**
 * Created by liuxingwei on 2016/11/17.
 */

public class AuthenticateActivity extends BaseCompatActivity {


    NoPingAnAuthenticatePresenter authenticatePresenter;

    @Override
    protected void injectFragmentView() {

        NoPingAnAuthenticateFragment authenticateFragment = (NoPingAnAuthenticateFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (authenticateFragment == null) {
            authenticateFragment = new NoPingAnAuthenticateFragment();
            authenticateFragment.setArguments(getIntent().getExtras());
            ActivityInjectUtils.addFragmentToActivity(getSupportFragmentManager(), authenticateFragment, R.id.contentFrame);
        }
        authenticatePresenter = new NoPingAnAuthenticatePresenter(authenticateFragment);

    }

    @Override
    public void onBackPressed() {
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
        return R.drawable.navigation_bar_back_blue;
    }

    @Override
    protected int getTitleText() {
        return R.string.authenticate_certify_text;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_authenticate;
    }
}
