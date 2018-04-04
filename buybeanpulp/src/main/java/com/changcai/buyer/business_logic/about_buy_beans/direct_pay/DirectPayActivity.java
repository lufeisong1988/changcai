package com.changcai.buyer.business_logic.about_buy_beans.direct_pay;

import android.app.Activity;
import android.view.View;

import com.changcai.buyer.BaseCompatActivity;
import com.changcai.buyer.R;
import com.changcai.buyer.business_logic.about_buy_beans.full_pay.FullPayActivity;
import com.changcai.buyer.business_logic.about_buy_beans.pay_result.PayResultActivity;
import com.changcai.buyer.business_logic.about_buy_beans.sign_contract.SignContractActivity;
import com.juggist.commonlibrary.rx.RxBus;
import com.changcai.buyer.ui.quote.OrderActivity;
import com.changcai.buyer.ui.quote.QuoteDetailActivity;
import com.changcai.buyer.util.ActivityInjectUtils;
import com.changcai.buyer.util.AppManager;

/**
 * Created by liuxingwei on 2017/1/11.
 */

public class DirectPayActivity extends BaseCompatActivity {

    DirectPayPresenter directPayPresenter;

    @Override
    public void onBackPressed() {
        AppManager.getAppManager().finishActivity(QuoteDetailActivity.class, OrderActivity.class, SignContractActivity.class, FullPayActivity.class, PayResultActivity.class);
        RxBus.get().post("OrderDetailPay",new Boolean(true));
        finish();
    }

    @Override
    protected void injectFragmentView() {

        DirectPayFragment directPayFragment = (DirectPayFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (directPayFragment == null){
            directPayFragment = new DirectPayFragment();
            directPayFragment.setArguments(getIntent().getExtras());
            ActivityInjectUtils.addFragmentToActivity(getSupportFragmentManager(),directPayFragment,R.id.contentFrame);
        }
        directPayPresenter = new DirectPayPresenter(directPayFragment);

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
        return R.string.direct_pay_result;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_authenticate;
    }
}
