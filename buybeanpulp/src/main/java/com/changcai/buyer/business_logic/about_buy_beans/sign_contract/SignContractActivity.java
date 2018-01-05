package com.changcai.buyer.business_logic.about_buy_beans.sign_contract;

import android.view.View;

import com.changcai.buyer.BaseCompatActivity;
import com.changcai.buyer.R;
import com.changcai.buyer.ui.quote.OrderActivity;
import com.changcai.buyer.ui.quote.QuoteDetailActivity;
import com.changcai.buyer.util.ActivityInjectUtils;
import com.changcai.buyer.util.AppManager;

/**
 * Created by liuxingwei on 2017/1/9.
 */

public class SignContractActivity extends BaseCompatActivity{


    @Override
    public void onBackPressed() {
        AppManager.getAppManager().finishActivity(QuoteDetailActivity.class, OrderActivity.class);
        finish();
    }

    SignContractPresenter signContractPresenter;
    @Override
    protected void injectFragmentView() {
        SignContractFragment signContractFragment = (SignContractFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (signContractFragment == null){
            signContractFragment = new SignContractFragment();
            signContractFragment.setArguments(getIntent().getExtras());
            ActivityInjectUtils.addFragmentToActivity(getSupportFragmentManager(),signContractFragment,R.id.contentFrame);
        }
        signContractPresenter = new SignContractPresenter(signContractFragment);

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setLineShow();
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
        return R.string.sign_contract;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_authenticate;
    }
}
