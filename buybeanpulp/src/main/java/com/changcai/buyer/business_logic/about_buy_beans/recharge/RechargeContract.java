package com.changcai.buyer.business_logic.about_buy_beans.recharge;

import android.content.Context;

import com.changcai.buyer.BasePresenter;
import com.changcai.buyer.BaseView;
import com.changcai.buyer.bean.EbaoBalanceInfo;
import com.changcai.buyer.business_logic.about_buy_beans.person_introduce.PersonIntroduceContract;
import com.changcai.buyer.interface_api.BaseApiModel;

/**
 * Created by liuxingwei on 2017/1/5.
 */

public interface RechargeContract {

    interface View extends BaseView<RechargeContract.Presenter> {


        boolean isActive();

        Context getActivityContext();

        void showErrorDialog(String title, String errorMessage, String leftButtonText, String rightButtonText, int action);

        void showSuccessDialog(int message);

        void setEbaoBalance(EbaoBalanceInfo balanceMoney);

        void goToResult(BaseApiModel baseApiModel);

        void showProgressDialog();

        void dismissProgressDialog();

    }

    interface Presenter extends BasePresenter {

        void getAccountBalance();

        void rechargePay(String password,String tranAmount);
    }
}
