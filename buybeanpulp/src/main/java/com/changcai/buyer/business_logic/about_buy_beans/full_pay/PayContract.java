package com.changcai.buyer.business_logic.about_buy_beans.full_pay;

import android.content.Context;

import com.changcai.buyer.BasePresenter;
import com.changcai.buyer.BaseView;
import com.changcai.buyer.interface_api.BaseApiModel;
import com.changcai.buyer.ui.order.bean.DeliveryInfo;
import com.changcai.buyer.ui.order.bean.OrderInfo;

/**
 * Created by liuxingwei on 2017/3/31.
 */

public interface PayContract {

    interface View extends BaseView<PayContract.Presenter> {
        void showFullPayInfo(OrderInfo orderInfo);

        void showDeliveryInfo(DeliveryInfo deliveryInfo);

        void showErrorDialog(String title, String errorMessage, String leftButtonText, String rightButtonText, int action);


        void showBalance(String money);

        void showProgressDialog();

        void dismissProgressDialog();
        Context getMContext();

        void gotoResult(BaseApiModel baseApiModel);
    }

    interface Presenter extends BasePresenter {
        void getOrderInfo();

        void setOrderId(String orderId);

        String getOrderId();


        /**
         * 提货单ID
         * @param deliveryId
         */
        void setDeliveryId(String deliveryId);

        String getDeliveryId();
        /**
         * PAY_EBAO_INSUFFICIENT_ERROR：账户余额不足，请充值
         * FULL_PAY_WAIT_CONFIRM_ERROR ：全额支付待确认
         * RECHARGE_OK_PAY_NG_ERROR：入金成功，支付失败
         * RECHARGE_NG_ERROR：入金失败
         * TOTAL_BALANCE_INSUFFICIENT_ERROR：【订单金额时-（平台账户余额+易宝账户）】
         * FULL_PAY_CONNECT_ERROR：全额支付网络异常
         *
         * @param password
         * @param payType      payType	String	是
         *                     空（默认支付方式）：平台账户余额 》订单金额时，从平台账户扣钱支付
         *                     平台账户余额<订单金额时，返回失败
         *                     direct（快捷支付方式）：平台账户余额 》订单金额时，从平台账户扣钱支付
         *                     平台账户余额+易宝账户》订单金额时，从易宝账户充值到平台后直接支付
         *                     平台账户余额+易宝账户<订单金额时，返回失败，并且返回不足金额
         */
        void frontMoneyPay(String password, String payType);


        void getDelivery();

        void  getPingAnBalance();


        /**
         * 支付提货单
         */
        void payOrdDelivery(String password,String type);
    }

}
