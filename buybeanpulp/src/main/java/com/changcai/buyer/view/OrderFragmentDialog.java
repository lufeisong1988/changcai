package com.changcai.buyer.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.SpaceStatistic;
import com.changcai.buyer.business_logic.about_buy_beans.full_pay.FullPayActivity;
import com.changcai.buyer.business_logic.about_buy_beans.sign_contract.SignContractActivity;
import com.changcai.buyer.ui.order.DeliveryDetailActivity;
import com.changcai.buyer.ui.order.OrderDetailActivity;
import com.ta.utdid2.android.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liuxingwei on 2017/3/8.
 */

public class OrderFragmentDialog extends BaseBottomDialog {
    @BindView(R.id.iv_order_status)
    ImageView ivOrderStatus;
    @BindView(R.id.iv_order_cancel)
    ImageView ivOrderCancel;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_tons)
    TextView tvTons;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_order_intro)
    TextView tvOrderIntro;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;

    private SpaceStatistic spaceStatistic;

    @Override
    public int getLayoutRes() {
        return R.layout.order_fragment_dialog;
    }

    @Override
    public void bindView(View v) {
        ButterKnife.bind(this, v);
        if (!getArguments().isEmpty()) {
            if (!StringUtils.isEmpty(getArguments().getString("orderType"))) {
                switch (getArguments().getString("orderType")) {
                    //签署合同
                    case "newBuyConfirming":
                        ivOrderStatus.setImageResource(R.drawable.window_contract_order);
                        tvConfirm.setText(R.string.go_sign_contract);
                        break;
                    //去支付
                    case "newBuyDepositing":
                        ivOrderStatus.setImageResource(R.drawable.qdtc_bzj);
                        tvConfirm.setText("去支付保证金");
                        break;
                    //定金
                    case "reserveDepositPayOrder":
                        ivOrderStatus.setImageResource(R.drawable.qdtc_dj);
                        tvConfirm.setText("待支付定金");
                        break;

                    case "allPayOrder":
                        ivOrderStatus.setImageResource(R.drawable.qdtc_qefk);
                        tvConfirm.setText("去全额付款");
                        break;
                    case "fastPayOrder":
                        ivOrderStatus.setImageResource(R.drawable.qdtc_zjf);
                        tvConfirm.setText("去转为直接付");

                        break;

                    case "deliveryOrder":
                        ivOrderStatus.setImageResource(R.drawable.qdtc_hk);
                        tvConfirm.setText("去支付货款");
                        break;
                    default:
                        ivOrderStatus.setImageResource(R.mipmap.no_network_2);
                        tvConfirm.setText(R.string.post);
                        break;
                }
            }

            spaceStatistic = (SpaceStatistic) getArguments().getSerializable("orderInfo");
            if (spaceStatistic.getDeliveryOrder() != null && Integer.parseInt(spaceStatistic.getDeliveryOrder()) > 0) {
                tvName.setText(TextUtils.isEmpty(spaceStatistic.getDeliveryInfo().getSellerEnterName()) ? "卖家：" : "卖家：".concat(spaceStatistic.getDeliveryInfo().getSellerEnterName()));
                tvPrice.setText(TextUtils.isEmpty(spaceStatistic.getDeliveryInfo().getPrice()) ? "单价：" : "单价：".concat(spaceStatistic.getDeliveryInfo().getPrice()).concat("元"));
                tvTons.setText(TextUtils.isEmpty(spaceStatistic.getDeliveryInfo().getQuantity()) ? "数量：" : "数量：".concat(spaceStatistic.getDeliveryInfo().getQuantity()).concat("吨"));
                tvOrderIntro.setText(TextUtils.isEmpty(spaceStatistic.getDeliveryInfo().getFactoryBrand()) ? "" : spaceStatistic.getDeliveryInfo().getFactoryBrand()
                        .concat(" / ")
                        .concat(TextUtils.isEmpty(spaceStatistic.getDeliveryInfo().getEggSpec()) ? "" : spaceStatistic.getDeliveryInfo().getEggSpec())
                        .concat(" / ")
                        .concat(TextUtils.isEmpty(spaceStatistic.getDeliveryInfo().getPackType()) ? "" : spaceStatistic.getDeliveryInfo().getPackType()));
            } else {
                tvName.setText(TextUtils.isEmpty(spaceStatistic.getOrderInfo().getSeller()) ? "卖家：" : "卖家：".concat(spaceStatistic.getOrderInfo().getSeller()));
                tvPrice.setText(TextUtils.isEmpty(spaceStatistic.getOrderInfo().getPrice()) ? "单价：" : "单价：".concat(spaceStatistic.getOrderInfo().getPrice()).concat("元"));
                tvTons.setText(TextUtils.isEmpty(spaceStatistic.getOrderInfo().getQuantity()) ? "数量：" : "数量：".concat(spaceStatistic.getOrderInfo().getQuantity()).concat("吨"));
                tvOrderIntro.setText(TextUtils.isEmpty(spaceStatistic.getOrderInfo().getFactoryBrand()) ? "" : spaceStatistic.getOrderInfo().getFactoryBrand()
                        .concat(" / ")
                        .concat(TextUtils.isEmpty(spaceStatistic.getOrderInfo().getEggSpec()) ? "" : spaceStatistic.getOrderInfo().getEggSpec())
                        .concat(" / ")
                        .concat(TextUtils.isEmpty(spaceStatistic.getOrderInfo().getPackType()) ? "" : spaceStatistic.getOrderInfo().getPackType()));
            }
        }
    }

    @Override
    public float getDimAmount() {
        return 0.4f;
    }


    @OnClick({R.id.tv_cancel, R.id.tv_confirm, R.id.iv_order_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_confirm:
                if (!StringUtils.isEmpty(getArguments().getString("orderType"))) {
                    Bundle bundle = new Bundle();
//                    if (spaceStatistic.getOrderInfo() == null) {
//                        dismiss();
//                        return;
//                    }
//                    if (TextUtils.isEmpty(spaceStatistic.getOrderInfo().getOrderId())) {
//                        dismiss();
//                        return;
//                    }

                    switch (getArguments().getString("orderType")) {
                        //签署合同
                        case "newBuyConfirming":
                            //全额付
                        case "allPayOrder":

                        case "fastPayOrder":
                        case "newBuyDepositing":
                        case "reserveDepositPayOrder":

                            bundle.putString("orderId", spaceStatistic.getOrderInfo().getOrderId());

//                            gotoActivity(SignContractActivity.class, bundle);
                            gotoActivity(OrderDetailActivity.class, bundle);
                            dismiss();
                            break;
                        //去支付

                        case "deliveryOrder":
                            bundle.putString("deliveryId", spaceStatistic.getDeliveryInfo().getDeliveryId());
                            gotoActivity(DeliveryDetailActivity.class, bundle);
                            dismiss();

                            break;
//                        //直接付  提货单  未支付
//                        case "buyDeliveryUnpay":
//                            gotoActivity(OrderDetailActivity.class, bundle);
//                            dismiss();
//
//                            break;

                        default:
                            dismiss();
                            break;
                    }
                }
                break;
            case R.id.tv_cancel:
            case R.id.iv_order_cancel:
                dismiss();
                break;
        }
    }


}
