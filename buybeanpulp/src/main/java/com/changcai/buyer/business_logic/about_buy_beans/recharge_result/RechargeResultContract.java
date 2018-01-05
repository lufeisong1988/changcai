package com.changcai.buyer.business_logic.about_buy_beans.recharge_result;

import android.content.Context;

import com.changcai.buyer.BasePresenter;
import com.changcai.buyer.BaseView;

/**
 * Created by liuxingwei on 2017/1/8.
 */

public interface RechargeResultContract {

    interface View extends BaseView<RechargeResultContract.Presenter> {


        boolean isActive();

        Context getActivityContext();




    }

    interface Presenter extends BasePresenter {

    }
}
