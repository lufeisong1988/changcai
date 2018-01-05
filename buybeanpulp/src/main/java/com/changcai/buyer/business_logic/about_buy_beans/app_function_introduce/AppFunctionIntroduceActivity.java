package com.changcai.buyer.business_logic.about_buy_beans.app_function_introduce;

import android.os.Build;
import android.view.View;

import com.changcai.buyer.CompatTouchBackActivity;
import com.changcai.buyer.R;
import com.changcai.buyer.util.ActivityInjectUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by liuxingwei on 2016/11/23.
 */

public class AppFunctionIntroduceActivity extends CompatTouchBackActivity {

    AppFunctionIntroducePresenter appFunctionIntroducePresenter;


    @Override
    protected void injectFragmentView() {
        AppFunctionIntroduceFragment appFunctionIntroduceFragment = (AppFunctionIntroduceFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (null == appFunctionIntroduceFragment) {
            appFunctionIntroduceFragment = new AppFunctionIntroduceFragment();
            ActivityInjectUtils.addFragmentToActivity(getSupportFragmentManager(),appFunctionIntroduceFragment,R.id.contentFrame);
        }
        appFunctionIntroducePresenter = new AppFunctionIntroducePresenter(appFunctionIntroduceFragment);

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
        return R.string.function_introduce;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_authenticate;
    }
}
