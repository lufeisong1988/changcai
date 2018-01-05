package com.changcai.buyer.business_logic.about_buy_beans.assign_platform;

import android.content.Context;
import android.os.Bundle;

import com.changcai.buyer.BasePresenter;
import com.changcai.buyer.BaseView;
import com.changcai.buyer.bean.UserInfo;

/**
 * Created by liuxingwei on 2016/11/22.
 */

public interface AssignPlatformContract {

    interface View extends BaseView<Presenter> {

        void showSuccessDialog(int message);
        Context getActivityContext();
        boolean isActive();
        void webViewLoadUrl();
    }

    interface Presenter extends BasePresenter {


        void commitAssignPlatformContract(String tokenId);

    }
}
