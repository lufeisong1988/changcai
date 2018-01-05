package com.changcai.buyer.business_logic.about_buy_beans.pay_result;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.changcai.buyer.BaseCompatActivity;
import com.changcai.buyer.R;
import com.changcai.buyer.business_logic.about_buy_beans.full_pay.FullPayActivity;
import com.changcai.buyer.business_logic.about_buy_beans.full_pay.PayActivity;
import com.changcai.buyer.business_logic.about_buy_beans.sign_contract.SignContractActivity;
import com.changcai.buyer.ui.quote.OrderActivity;
import com.changcai.buyer.ui.quote.QuoteDetailActivity;
import com.changcai.buyer.util.ActivityInjectUtils;
import com.changcai.buyer.util.AppManager;
import com.changcai.buyer.util.LogUtil;

/**
 * Created by liuxingwei on 2017/1/11.
 */

public class PayResultActivity extends BaseCompatActivity{
    PayResultPresenter payResultPresenter;

    @Override
    public void onBackPressed() {
        AppManager.getAppManager().finishActivity(QuoteDetailActivity.class, OrderActivity.class, SignContractActivity.class, FullPayActivity.class, PayActivity.class);
        finish();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d("PayResultActivity","this is finishing");

    }

    @Override
    protected void injectFragmentView() {
        PayResultFragment payResultFragment = (PayResultFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (payResultFragment == null){
            payResultFragment = new PayResultFragment();
            payResultFragment.setArguments(getIntent().getExtras());
            ActivityInjectUtils.addFragmentToActivity(getSupportFragmentManager(),payResultFragment,R.id.contentFrame);
        }
        payResultPresenter = new PayResultPresenter(payResultFragment);

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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
        return R.string.pay_success;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_authenticate;
    }

    public  void setTitleText(String titleContent){
        titleText.setText(titleContent);
    }
}
