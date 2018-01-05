package com.changcai.buyer.business_logic.about_buy_beans.deposit_and_pay;

import android.content.Context;

import com.changcai.buyer.BasePresenter;
import com.changcai.buyer.BaseView;

/**
 * Created by liuxingwei on 2017/3/20.
 */

public interface DepositAndPayContract {

    interface View extends BaseView<DepositAndPayContract.Presenter> {


        boolean isActive();

        Context getActivityContext();

    }

    interface Presenter extends BasePresenter {


    }
}
