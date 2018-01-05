package com.changcai.buyer.business_logic.about_buy_beans.bind_bank;

import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;

import com.changcai.buyer.BaseCompatActivity;
import com.changcai.buyer.R;
import com.changcai.buyer.util.ActivityInjectUtils;

/**
 * Created by liuxingwei on 2017/3/23.
 */

public class BindBankActivity extends BaseCompatActivity {
    WebView webView;
    BindBankFragment bindBankFragment;

    @Override
    public void onBackPressed() {
        if (webView == null) {
            webView = bindBankFragment.webView;
        }
        if (webView.canGoBack()) {
            if (bindBankFragment.bindBankSuccess) {
                this.finish();
            } else {
                if (TextUtils.isEmpty(bindBankFragment.preViewUrl) || bindBankFragment.preViewUrl.equalsIgnoreCase("none")) {
                    finish();
                } else {
                    bindBankFragment.loadWebUrl(bindBankFragment.preViewUrl);

                }
            }
        } else {
            finish();
        }
    }

    @Override
    protected void injectFragmentView() {

        bindBankFragment = (BindBankFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (bindBankFragment == null) {
            bindBankFragment = new BindBankFragment();
            ActivityInjectUtils.addFragmentToActivity(getSupportFragmentManager(), bindBankFragment, R.id.contentFrame);
        }
        BindBankPresenter authenticatePresenter = new BindBankPresenter(bindBankFragment);

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
        return
                R.drawable.icon_nav_back;
    }

    @Override
    protected int getTitleText() {
        return R.string.bind_bank;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_authenticate;
    }
}
