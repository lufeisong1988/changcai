package com.changcai.buyer.business_logic.about_buy_beans.full_pay;

import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;

import com.changcai.buyer.BaseCompatActivity;
import com.changcai.buyer.R;
import com.changcai.buyer.business_logic.about_buy_beans.sign_contract.SignContractActivity;
import com.changcai.buyer.util.ActivityInjectUtils;
import com.changcai.buyer.util.AppManager;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.view.LoadingProgressDialog;

/**
 * Created by liuxingwei on 2017/3/31.
 */

public class PayActivity extends BaseCompatActivity {

    private String payType;
    private PayPresenter downPayPresenter;

    @Override
    public void onBackPressed() {
        AppManager.getAppManager().finishActivity(PayActivity.class, SignContractActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d("PayActivity", "this is finishing");
    }

    @Override
    protected void injectFragmentView() {

        if (!TextUtils.isEmpty(payType)) {
//            if (payType.contentEquals("_down_pay")) {

            PayFragment payFragment = (PayFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
            if (payFragment == null) {
                payFragment = new PayFragment();
                payFragment.setArguments(getIntent().getExtras());
                ActivityInjectUtils.addFragmentToActivity(getSupportFragmentManager(), payFragment, R.id.contentFrame);
            }
            downPayPresenter = new PayPresenter(payFragment);

//            } else if (payType.contentEquals("_goods_pay")) {


//            }
        }

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

    @SuppressWarnings("deprecation")
    @Override
    protected int getTitleText() {
        if (getIntent() == null) return R.string.full_pay;
        payType = getIntent().getExtras().getString("payType");
        if (!TextUtils.isEmpty(payType)) {
            if (payType.contentEquals("_down_pay"))
                return R.string.down_pay_money;
            else if (payType.contentEquals("_goods_pay")) {
                return R.string.delivery_pay6;
            }
        }
        return R.string.full_pay;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_authenticate;
    }
}
