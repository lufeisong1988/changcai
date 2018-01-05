package com.changcai.buyer.business_logic.about_buy_beans.set_paypassword;

import android.content.Context;
import android.os.Bundle;

import com.changcai.buyer.BasePresenter;
import com.changcai.buyer.BaseView;

/**
 * Created by liuxingwei on 2016/11/29.
 */

public interface SetPayPasswordContract {


    interface View extends BaseView<SetPayPasswordContract.Presenter> {

        Context getActivityContext();

        void showSuccessDialog();
    }

    interface Presenter extends BasePresenter {

        void checkout(String password, String confirmPassword, Bundle bundle);
    }
}
