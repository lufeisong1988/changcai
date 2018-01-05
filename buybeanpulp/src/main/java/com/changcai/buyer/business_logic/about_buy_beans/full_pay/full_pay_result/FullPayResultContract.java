package com.changcai.buyer.business_logic.about_buy_beans.full_pay.full_pay_result;

import android.content.Context;

import com.changcai.buyer.BasePresenter;
import com.changcai.buyer.BaseView;

/**
 * Created by liuxingwei on 2017/6/1.
 */

public interface FullPayResultContract {

    interface View extends BaseView<Presenter> {


        boolean isActive();

        Context getActivityContext();
        void showImmediatePaySuccess();

    }

    interface Presenter extends BasePresenter {
         void directPayToSeller(String orderId) ;

    }

}
