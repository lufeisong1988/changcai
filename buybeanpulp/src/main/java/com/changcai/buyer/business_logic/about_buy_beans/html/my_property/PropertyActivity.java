package com.changcai.buyer.business_logic.about_buy_beans.html.my_property;

import android.view.View;
import android.webkit.WebView;

import com.changcai.buyer.BaseCompatActivity;
import com.changcai.buyer.R;
import com.changcai.buyer.util.ActivityInjectUtils;

/**
 * Created by liuxingwei on 2017/1/5.
 */

public class PropertyActivity extends BaseCompatActivity {
    PropertyFragment propertyFragment;
    WebView webView;

    @Override
    public void onBackPressed() {
        if (webView == null) {
            webView = propertyFragment.webView;
        }
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }

    PropertyPresenter propertyPresenter;

    @Override
    protected void injectFragmentView() {
        propertyFragment = (PropertyFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (null == propertyFragment) {
            propertyFragment = new PropertyFragment();
            ActivityInjectUtils.addFragmentToActivity(getSupportFragmentManager(), propertyFragment, R.id.contentFrame);
        }
        propertyPresenter = new PropertyPresenter(propertyFragment);
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
        return R.string.my_property_text;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_authenticate;
    }
}
