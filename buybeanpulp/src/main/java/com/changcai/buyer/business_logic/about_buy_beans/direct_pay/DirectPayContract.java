package com.changcai.buyer.business_logic.about_buy_beans.direct_pay;

import android.content.Context;

import com.changcai.buyer.BasePresenter;
import com.changcai.buyer.BaseView;

/**
 * Created by liuxingwei on 2017/1/11.
 */

public interface DirectPayContract {

    interface View extends BaseView<Presenter> {


        boolean isActive();

        Context getActivityContext();

    }

    interface Presenter extends BasePresenter {


    }
}
