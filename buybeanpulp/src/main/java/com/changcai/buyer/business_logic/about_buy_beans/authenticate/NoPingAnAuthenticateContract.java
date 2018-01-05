package com.changcai.buyer.business_logic.about_buy_beans.authenticate;

import android.content.Context;

import com.changcai.buyer.BasePresenter;
import com.changcai.buyer.BaseView;

/**
 * Created by liuxingwei on 2017/6/16.
 */

public interface NoPingAnAuthenticateContract {


    interface View extends BaseView<Presenter> {
        boolean isActive();

        Context getActivityContext();

        void setViewByType(String type);

        void setAuthenticateType(boolean authenticateType);
    }

    interface Presenter extends BasePresenter {


    }

}
