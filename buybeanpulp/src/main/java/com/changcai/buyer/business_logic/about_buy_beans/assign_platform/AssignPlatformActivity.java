package com.changcai.buyer.business_logic.about_buy_beans.assign_platform;

import android.os.Build;
import android.view.View;

import com.changcai.buyer.BaseCompatActivity;
import com.changcai.buyer.R;
import com.changcai.buyer.util.ActivityInjectUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by liuxingwei on 2016/11/22.
 */

public class AssignPlatformActivity extends BaseCompatActivity {

    AssignPlatformPresenter assignPlatformPresenter;

    @Override
    protected void injectFragmentView() {

        AssignPlatformFragment assignPlatformFragment = (AssignPlatformFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (assignPlatformFragment == null) {
            assignPlatformFragment = AssignPlatformFragment.newInstance();
            ActivityInjectUtils.addFragmentToActivity(getSupportFragmentManager(), assignPlatformFragment, R.id.contentFrame);
        }
        assignPlatformFragment.setArguments(getIntent().getExtras());
        assignPlatformPresenter = new AssignPlatformPresenter(assignPlatformFragment);


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
        return R.string.check_assign_platform_agreement;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_authenticate;
    }
}
