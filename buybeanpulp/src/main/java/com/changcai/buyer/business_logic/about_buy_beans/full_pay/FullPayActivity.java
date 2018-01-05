package com.changcai.buyer.business_logic.about_buy_beans.full_pay;

import android.view.View;

import com.changcai.buyer.BaseCompatActivity;
import com.changcai.buyer.R;
import com.changcai.buyer.business_logic.about_buy_beans.sign_contract.SignContractActivity;
import com.changcai.buyer.ui.order.OrderDetailActivity;
import com.changcai.buyer.ui.quote.OrderActivity;
import com.changcai.buyer.ui.quote.QuoteDetailActivity;
import com.changcai.buyer.util.ActivityInjectUtils;
import com.changcai.buyer.util.AppManager;
import com.changcai.buyer.view.PayFragmentDialog;

/**
 * Created by liuxingwei on 2017/1/11.
 */

public class FullPayActivity extends BaseCompatActivity {

    @Override
    public void onBackPressed() {
        AppManager.getAppManager().finishActivity(QuoteDetailActivity.class, OrderActivity.class, SignContractActivity.class);
        finish();
    }

    FullPayPresenter fullPayPresenter;

    @Override
    protected void injectFragmentView() {
        FullPayFragment fullPayFragment = (FullPayFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fullPayFragment == null) {
            fullPayFragment = new FullPayFragment();
            fullPayFragment.setArguments(getIntent().getExtras());
            ActivityInjectUtils.addFragmentToActivity(getSupportFragmentManager(), fullPayFragment, R.id.contentFrame);
        }
        fullPayPresenter = new FullPayPresenter(fullPayFragment);

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.view_bottom_line).setVisibility(View.VISIBLE);

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
        return R.string.full_pay;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_authenticate;
    }


}
