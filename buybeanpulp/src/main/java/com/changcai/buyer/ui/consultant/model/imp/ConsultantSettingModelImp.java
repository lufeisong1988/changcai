package com.changcai.buyer.ui.consultant.model.imp;

import com.changcai.buyer.bean.GetUserLevelBean;
import com.changcai.buyer.bean.UserInfo;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.interface_api.ApiServiceGenerator;
import com.changcai.buyer.interface_api.BaseApiModel;
import com.changcai.buyer.interface_api.GetUserLevelService;
import com.changcai.buyer.interface_api.RefreshUserInfoService;
import com.changcai.buyer.interface_api.UpdateCounselorService;
import com.changcai.buyer.ui.consultant.model.ConsultantSettingModel;
import com.changcai.buyer.util.SPUtil;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by lufeisong on 2017/12/27.
 */

public class ConsultantSettingModelImp implements ConsultantSettingModel {
    private GetUserLevelCallback callback;
    private UpdateConselorCallback updateCallback;
    public interface GetUserLevelCallback{
        void getUserLevelSucceed(GetUserLevelBean bean);
        void getUserLevelFail(String failStr);
        void getUserLevelError();
    }
    public interface UpdateConselorCallback{
        void updateConselorSucceed(String serviceLevel, String counselorStatus, String serviceStatus);
        void updateConselorFail(String failStr);
        void updateConselorError();
    }
    public ConsultantSettingModelImp(GetUserLevelCallback callback,UpdateConselorCallback updateCallback) {
        this.callback = callback;
        this.updateCallback = updateCallback;
    }

    @Override
    public void getUserLevel() {
        Map<String, String> map = SPUtil.getObjectFromShare(Constants.REQUEST_BASE_PARAMETERS);
        map.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        GetUserLevelService service = ApiServiceGenerator.createService(GetUserLevelService.class);
        service.getUserLevel(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseApiModel<GetUserLevelBean>>() {
                    @Override
                    public void call(BaseApiModel<GetUserLevelBean> getUserLevelBeanBaseApiModel) {
                        if(getUserLevelBeanBaseApiModel.getErrorCode().equals("0")){
                            callback.getUserLevelSucceed(getUserLevelBeanBaseApiModel.getResultObject());
                        }else{
                            callback.getUserLevelFail(getUserLevelBeanBaseApiModel.getErrorDesc());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        callback.getUserLevelError();
                    }
                });
    }

    @Override
    public void updateCounselor(final String serviceLevel, final String counselorStatus, final String serviceStatus) {
        Map<String, String> map = SPUtil.getObjectFromShare(Constants.REQUEST_BASE_PARAMETERS);
        map.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        map.put("serviceLevel", serviceLevel);
        map.put("counselorStatus", counselorStatus);
        map.put("serviceStatus", serviceStatus);
        UpdateCounselorService service = ApiServiceGenerator.createService(UpdateCounselorService.class);
        service.getUserLevel(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseApiModel<String>>() {
                    @Override
                    public void call(BaseApiModel<String> stringBaseApiModel) {
                        if(stringBaseApiModel.getErrorCode().equals("0")){
                            updateCallback.updateConselorSucceed(serviceLevel,counselorStatus,serviceStatus);
                        }else{
                            updateCallback.updateConselorFail(stringBaseApiModel.getErrorDesc());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        updateCallback.updateConselorError();
                    }
                });
    }

    public void updateUserInfo(){
        Map<String, String> map = new HashMap<>();
        map.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        RefreshUserInfoService refreshUserInfoService = ApiServiceGenerator.createService(RefreshUserInfoService.class);
        refreshUserInfoService.refreshUserInfo(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseApiModel<UserInfo>>() {
                    @Override
                    public void call(BaseApiModel<UserInfo> userInfoBaseApiModel) {
                        SPUtil.saveObjectToShare(Constants.KEY_USER_INFO, userInfoBaseApiModel.getResultObject());
                    }
                });
    }
}
