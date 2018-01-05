package com.changcai.buyer.ui.consultant.present.imp;

import com.changcai.buyer.CommonApplication;
import com.changcai.buyer.bean.GetUserLevelBean;
import com.changcai.buyer.bean.UserInfo;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.ui.consultant.ConsultantSettingViewModel;
import com.changcai.buyer.ui.consultant.model.ConsultantSettingModel;
import com.changcai.buyer.ui.consultant.model.imp.ConsultantSettingModelImp;
import com.changcai.buyer.ui.consultant.present.ConsultantSettingPresent;
import com.changcai.buyer.util.SPUtil;

/**
 * Created by lufeisong on 2017/12/27.
 */

public class ConsultantSettingPresentImp implements ConsultantSettingPresent,ConsultantSettingModelImp.GetUserLevelCallback,ConsultantSettingModelImp.UpdateConselorCallback{
    private ConsultantSettingModel model;
    private ConsultantSettingViewModel view;
    private boolean isLoading = false;
    public ConsultantSettingPresentImp(ConsultantSettingViewModel view) {
        this.view = view;
        model = new ConsultantSettingModelImp(this,this);
    }

    @Override
    public void getUserLevel(boolean isLoading) {
        this.isLoading = isLoading;
        if(isLoading && view != null){
            view.showLoading();
        }
        model.getUserLevel();
    }

    @Override
    public void updateCounselor(final String serviceLevel, final String counselorStatus, final String serviceStatus) {
        if(view != null){
            view.showLoading();
        }
        model.updateCounselor(serviceLevel,counselorStatus,serviceStatus);
    }

    @Override
    public void onDestory() {
        view = null;
    }

    //获取会员等级信息
    @Override
    public void getUserLevelSucceed(GetUserLevelBean bean) {
        if(view != null){
            if(isLoading){
                view.dismissLoading();
            }
            CommonApplication.getInstance().setUserLevelBean(bean);
            view.getUserLevelSucceed();
        }
    }

    @Override
    public void getUserLevelFail(String failStr) {
        if(view != null){
            if(isLoading){
                view.dismissLoading();
            }
            view.getUserLevelFail(failStr);
        }
    }

    @Override
    public void getUserLevelError() {
        if(view != null){
            if(isLoading){
                view.dismissLoading();
            }
            view.getUserLevelError();
        }
    }

    //更新会员等级
    @Override
    public void updateConselorSucceed(String serviceLevel, String counselorStatus, String serviceStatus) {
        UserInfo userInfo = SPUtil.getObjectFromShare(Constants.KEY_USER_INFO);
        if(userInfo != null){
            userInfo.setServiceLevel(serviceLevel);
            userInfo.setCounselorStatus(counselorStatus);
            userInfo.setServiceStatus(serviceStatus);
            SPUtil.saveObjectToShare(Constants.KEY_USER_INFO, userInfo);
        }
        model.updateUserInfo();
        if(view != null){
            view.dismissLoading();
            view.updateConsultantSucceed();
        }
    }

    @Override
    public void updateConselorFail(String failStr) {
        if(view != null){
            view.dismissLoading();
            view.updateConsultantFail(failStr);
        }
    }

    @Override
    public void updateConselorError() {
        if(view != null){
            view.dismissLoading();
            view.updateConsultantError();
        }
    }
}
