package com.changcai.buyer.business_logic.about_buy_beans.full_pay.full_pay_result;

import android.view.View;

import com.changcai.buyer.BaseCompatActivity;
import com.changcai.buyer.R;
import com.changcai.buyer.business_logic.about_buy_beans.full_pay.FullPayActivity;
import com.changcai.buyer.business_logic.about_buy_beans.sign_contract.SignContractActivity;
import com.changcai.buyer.ui.base.BaseActivity;
import com.changcai.buyer.ui.base.BaseFragmentActivity;
import com.changcai.buyer.ui.quote.OrderActivity;
import com.changcai.buyer.ui.quote.QuoteDetailActivity;
import com.changcai.buyer.util.ActivityInjectUtils;
import com.changcai.buyer.util.AppManager;

/**
 * Created by liuxingwei on 2017/6/1.
 */

public class FullPayResultActivity extends BaseCompatActivity{
    FullPayResultPresenter payResultPresenter;

    @Override
    public void onBackPressed() {
        AppManager.getAppManager().finishActivity(QuoteDetailActivity.class, OrderActivity.class, SignContractActivity.class, FullPayActivity.class,FullPayResultActivity.class);
    }

    @Override
    protected void injectFragmentView() {
        FullPayResultFragment  fullPayResultFragment = (FullPayResultFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fullPayResultFragment == null) {
            fullPayResultFragment = new FullPayResultFragment();
            fullPayResultFragment.setArguments(getIntent().getExtras());
            ActivityInjectUtils.addFragmentToActivity(getSupportFragmentManager(), fullPayResultFragment, R.id.contentFrame);
        }
        payResultPresenter = new FullPayResultPresenter(fullPayResultFragment);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected int getTitleTextColor() {
        return getResources().getColor(R.color.black);
    }

    @Override
    protected int getNavigationVisible() {
        return View.VISIBLE;
    }

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
        return R.string.empty;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_authenticate;
    }
}
