package com.changcai.buyer.business_logic.about_buy_beans.pay_result;

import android.content.Context;

import com.changcai.buyer.BasePresenter;
import com.changcai.buyer.BaseView;

/**
 * Created by liuxingwei on 2017/1/11.
 */

public interface PayResultContract {

    interface View extends BaseView<Presenter> {
        boolean isActive();

        Context getActivityContext();

        void showErrorDialog(String title, String errorMessage, String leftButtonText, String rightButtonText, int action);

        void showSuccessDialog(int message);

        void showPayResultFail();

        void showPayResultSuccess();

        void showPayResultConfirm();

        void goToDirectPayResult();

        void showRechargeOkPayFail();

    }

    interface Presenter extends BasePresenter {


        void directPayToSeller(String orderId);
    }
}
