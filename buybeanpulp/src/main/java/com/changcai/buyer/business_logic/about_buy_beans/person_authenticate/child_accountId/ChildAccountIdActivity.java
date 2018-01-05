package com.changcai.buyer.business_logic.about_buy_beans.person_authenticate.child_accountId;

import android.view.View;

import com.changcai.buyer.CompatTouchBackActivity;
import com.changcai.buyer.R;
import com.changcai.buyer.util.ActivityInjectUtils;

/**
 * Created by liuxingwei on 2016/11/24.
 */

public class ChildAccountIdActivity extends CompatTouchBackActivity {
    ChildAccountIdPresenter childAccountIdPresenter;

    @Override
    protected void injectFragmentView() {

        ChildAccountFragment childAccountFragment = (ChildAccountFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (null == childAccountFragment) {
            childAccountFragment = ChildAccountFragment.newInstance();
            ActivityInjectUtils.addFragmentToActivity(getSupportFragmentManager(), childAccountFragment, R.id.contentFrame);

        }
        childAccountIdPresenter = new ChildAccountIdPresenter(childAccountFragment);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
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
        return R.string.id_authenticate_text;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_authenticate;
    }
}
