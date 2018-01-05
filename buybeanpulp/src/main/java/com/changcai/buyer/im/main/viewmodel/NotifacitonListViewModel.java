package com.changcai.buyer.im.main.viewmodel;

import com.changcai.buyer.bean.GetCounselorsModel;

import java.util.List;

/**
 * Created by lufeisong on 2017/12/22.
 */

public interface NotifacitonListViewModel {
    void showLoading();

    void dismissLoading();

    void getCounselorsModelSucceed(List<GetCounselorsModel.InfoBean> info);

    void getCounselorsModelFail(String failStr);

    void getCounselorsModelError();

    void unLogin();

    void toNOTIFACTION();

    void hideNOTIFACTION();

    void showNOTIFACTION();

    void loginNimFail(String failStr);

    void updateConsultantStatus(boolean showDot, String message, long time);

    void updateAllStatus(boolean showDot, String message, long time);


}
