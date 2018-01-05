package com.changcai.buyer.business_logic.about_buy_beans.sign_contract;

import android.content.Context;

import com.changcai.buyer.BasePresenter;
import com.changcai.buyer.BaseView;
import com.changcai.buyer.bean.OrderEnum;
import com.changcai.buyer.ui.order.bean.OrderInfo;

/**
 * Created by liuxingwei on 2017/1/9.
 */

public interface SignContract {

    interface View extends BaseView<SignContract.Presenter> {
        boolean isActive();

        Context getActivityContext();

        void showErrorDialog(String title, String errorMessage, String leftButtonText, String rightButtonText, int action);

        void showSuccessDialog(String message);

        void showSuccessDialog(String message, int action);

        void showContractInfo(OrderInfo orderInfo);

        void gotoFullPay();

        void setSignEnAble(boolean isAble);

        void showProgressDialog();

        void dismissProgressDialog();

        void gotoGoodsPay(String DeliveryInfoId);

        void gotoDownPay();

        void showErrorDialog(String message, final int action);


    }

    interface Presenter extends BasePresenter {

        void setOrderId(String orderId);

        void setOrderPrice(String orderPrice);

        void getOrderInfo();

        void signContract();

        void previewContract(OrderInfo orderInfo);
    }
}
