package com.changcai.buyer.business_logic.about_buy_beans.ping_an_bank_card_no;

import android.content.Context;

import com.changcai.buyer.BasePresenter;
import com.changcai.buyer.BaseView;
import com.changcai.buyer.bean.EbaoBalanceInfo;

/**
 * Created by liuxingwei on 2017/1/8.
 */

public interface PingAnEasyPayRechargeContract {

    interface View extends BaseView<PingAnEasyPayRechargeContract.Presenter> {


        boolean isActive();

        Context getActivityContext();

        void setBankInfo(EbaoBalanceInfo ebaoBalanceInfo);

    }

    interface Presenter extends BasePresenter {


        void  getAccountBalance();

    }
}
