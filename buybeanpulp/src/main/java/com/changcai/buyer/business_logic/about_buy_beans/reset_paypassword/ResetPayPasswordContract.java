package com.changcai.buyer.business_logic.about_buy_beans.reset_paypassword;

import android.content.Context;

import com.changcai.buyer.BasePresenter;
import com.changcai.buyer.BaseView;
import com.changcai.buyer.bean.DetailsIdent;

/**
 * Created by liuxingwei on 2016/11/29.
 */

public interface ResetPayPasswordContract {

    interface View extends BaseView<ResetPayPasswordContract.Presenter>{
        void showSuccessDialog();
        void showToast(String message);
        Context getActivityContext();
        boolean isActive();
        void closeTimer();
        void startTimerTask();
        void reSetTime();
        void setEnterType(DetailsIdent detailsIdent);

    }

    interface Presenter extends BasePresenter{
        void sendDynamicCode(String number);
        void commitUpdate(String tokenId,String payPassword, String code ,String identityNo);
        void loadIdentityDetails(String tokenId);
    }
}
