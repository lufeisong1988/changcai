package com.changcai.buyer.ui.consultant;

/**
 * Created by lufeisong on 2017/12/27.
 */

public interface ConsultantSettingViewModel {
    void showLoading();

    void dismissLoading();

    void getUserLevelSucceed();

    void getUserLevelFail(String failStr);

    void getUserLevelError();


    void updateConsultantSucceed();

    void updateConsultantFail(String failStr);

    void updateConsultantError();

}
