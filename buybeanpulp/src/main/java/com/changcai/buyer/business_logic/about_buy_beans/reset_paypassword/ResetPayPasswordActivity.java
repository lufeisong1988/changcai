package com.changcai.buyer.business_logic.about_buy_beans.reset_paypassword;

import android.os.Build;
import android.view.View;

import com.changcai.buyer.CompatTouchBackActivity;
import com.changcai.buyer.R;
import com.changcai.buyer.util.ActivityInjectUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by liuxingwei on 2016/11/29.
 */

public class ResetPayPasswordActivity extends CompatTouchBackActivity {

    ResetPayPasswordPresenter resetPayPasswordPresenter;

    @Override
    protected void injectFragmentView() {
        ResetPayPasswordFragment resetPayPasswordFragment = (ResetPayPasswordFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (null == resetPayPasswordFragment) {
            resetPayPasswordFragment = ResetPayPasswordFragment.getInstance();
            resetPayPasswordFragment.setArguments(getIntent().getExtras());
            ActivityInjectUtils.addFragmentToActivity(getSupportFragmentManager(), resetPayPasswordFragment, R.id.contentFrame);
        }
        resetPayPasswordPresenter = new ResetPayPasswordPresenter(resetPayPasswordFragment);


        if (getIntent().getExtras().containsKey("isReset")) {
            if (getIntent().getExtras().getBoolean("isReset")) {
                titleText.setText(R.string.reset_password);
            } else {
                titleText.setText(R.string.retrieve_password);
            }
        }
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
        return R.drawable.icon_nav_back;
    }

    @Override
    protected int getTitleText() {
        return R.string.reset_password;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_authenticate;
    }
}
