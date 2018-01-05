package com.changcai.buyer.business_logic.about_buy_beans.direct_pay;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.changcai.buyer.BaseFragment;
import com.changcai.buyer.R;
import com.changcai.buyer.business_logic.about_buy_beans.full_pay.FullPayActivity;
import com.changcai.buyer.business_logic.about_buy_beans.pay_result.PayResultActivity;
import com.changcai.buyer.business_logic.about_buy_beans.sign_contract.SignContractActivity;
import com.changcai.buyer.ui.order.OrderDetailActivity;
import com.changcai.buyer.ui.quote.OrderActivity;
import com.changcai.buyer.ui.quote.QuoteDetailActivity;
import com.changcai.buyer.util.AppManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by liuxingwei on 2017/1/11.
 */

public class DirectPayFragment extends BaseFragment implements DirectPayContract.View {
    DirectPayContract.Presenter presenter;
    @BindView(R.id.iv_recharge_status)
    ImageView ivRechargeStatus;
    @BindView(R.id.tv_recharge_status)
    TextView tvRechargeStatus;
    @BindView(R.id.tv_recharge_number)
    TextView tvRechargeNumber;
    @BindView(R.id.tv_waiting)
    TextView tvWaiting;
    @BindView(R.id.tv_check_my_property)
    TextView tvCheckMyProperty;
    @BindView(R.id.tv_check_my_order)
    TextView tvCheckMyOrder;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_bank_card_info, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvCheckMyProperty.setText(R.string.order_detail_btn_text);
        tvCheckMyOrder.setText(R.string.look_other_price_info);
        tvRechargeStatus.setText(R.string.direct_pay_success);
        tvCheckMyOrder.setVisibility(View.VISIBLE);
        tvRechargeNumber.setVisibility(View.GONE);
        tvWaiting.setVisibility(View.VISIBLE);
        tvWaiting.setText(getString(R.string.waiting_seller_confirm));
        ivRechargeStatus.setImageResource(R.drawable.icon_success);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detach();
        unbinder.unbind();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }


    @Override
    public void setPresenter(DirectPayContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showErrorDialog(String message) {

    }

    @OnClick({R.id.iv_recharge_status, R.id.tv_recharge_status, R.id.tv_recharge_number, R.id.tv_waiting, R.id.tv_check_my_property, R.id.tv_check_my_order})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_recharge_status:
                break;
            case R.id.tv_recharge_status:
                break;
            case R.id.tv_recharge_number:
                break;
            case R.id.tv_waiting:
                break;
            case R.id.tv_check_my_property:
                AppManager.getAppManager().finishActivity(QuoteDetailActivity.class, OrderActivity.class, SignContractActivity.class, FullPayActivity.class, PayResultActivity.class);
                gotoActivity(OrderDetailActivity.class, getArguments());
                getActivity().finish();
                break;
            case R.id.tv_check_my_order:
                AppManager.getAppManager().finishActivity(QuoteDetailActivity.class, OrderActivity.class, SignContractActivity.class, FullPayActivity.class, PayResultActivity.class);
                getActivity().finish();
                break;
        }
    }
}
