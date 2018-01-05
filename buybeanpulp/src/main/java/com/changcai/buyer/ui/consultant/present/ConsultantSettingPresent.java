package com.changcai.buyer.ui.consultant.present;

/**
 * Created by lufeisong on 2017/12/27.
 */

public interface ConsultantSettingPresent {
    void getUserLevel(boolean isLoading);
    void updateCounselor(String serviceStatus,String counselorStatus,String serviceLevel);
    void onDestory();
}
