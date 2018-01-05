package com.changcai.buyer.business_logic.about_buy_beans.recharge_result;

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
import com.changcai.buyer.interface_api.BaseApiModel;
import com.changcai.buyer.ui.order.OrderListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by liuxingwei on 2017/1/8.
 */

public class RechargeResultFragment extends BaseFragment implements RechargeResultContract.View {

    RechargeResultContract.Presenter presenter;

    Unbinder unbinder;
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
    BaseApiModel baseApiModel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_bank_card_info, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        baseApiModel = (BaseApiModel) getArguments().getSerializable("recharge_result");
        if (baseApiModel.getErrorCode().equalsIgnoreCase("0")){
            rechargeSuccess();
        }else{
            rechargeConfirm();
        }
    }


    private void rechargeSuccess() {
//        ivRechargeStatus.setImageLevel(100);
        ivRechargeStatus.setImageResource(R.drawable.icon_success);
        tvRechargeStatus.setText(R.string.recharge_success);
        tvRechargeNumber.setText(baseApiModel.getResultObject().toString());
        tvCheckMyProperty.setVisibility(View.VISIBLE);
        tvCheckMyOrder.setVisibility(View.VISIBLE);
        tvWaiting.setVisibility(View.GONE);
    }

    private void rechargeConfirm() {
//        ivRechargeStatus.setImageLevel(50);
        ivRechargeStatus.setImageResource(R.drawable.icon_wait);
        tvRechargeStatus.setText(R.string.recharge_ing);
        tvRechargeNumber.setText(baseApiModel.getResultObject().toString());
        tvCheckMyProperty.setVisibility(View.VISIBLE);
        tvWaiting.setVisibility(View.VISIBLE);
        tvCheckMyOrder.setVisibility(View.GONE);
        tvWaiting.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detach();
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
    public void setPresenter(RechargeResultContract.Presenter presenter) {
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
                getActivity().finish();
                break;
            case R.id.tv_check_my_order:
                Bundle bundle = new Bundle();
                bundle.putString("orderViewStatus", "");
                gotoActivity(OrderListActivity.class, bundle);
                break;
        }
    }
}
