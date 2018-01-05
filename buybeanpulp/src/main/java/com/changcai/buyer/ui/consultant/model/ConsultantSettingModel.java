package com.changcai.buyer.ui.consultant.model;

/**
 * Created by lufeisong on 2017/12/27.
 */

public interface ConsultantSettingModel {
    void getUserLevel();
    void updateCounselor(final String serviceLevel, final String counselorStatus, final String serviceStatus);
    void updateUserInfo();
}
