package com.changcai.buyer.business_logic.about_buy_beans.authenticate;

import android.text.TextUtils;

import com.changcai.buyer.bean.UserInfo;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.interface_api.ApiCodeErrorException;
import com.changcai.buyer.interface_api.ApiServiceGenerator;
import com.changcai.buyer.interface_api.NetworkResultFunc1;
import com.changcai.buyer.interface_api.RefreshUserInfoService;
import com.changcai.buyer.interface_api.ThrowableFiltrateFunc;
import com.changcai.buyer.ui.login.LoginActivity;
import com.changcai.buyer.util.RxUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.ServerErrorCodeDispatch;
import com.changcai.buyer.util.UserDataUtil;

import java.util.HashMap;
import java.util.Map;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by liuxingwei on 2017/6/16.
 */

public class NoPingAnAuthenticatePresenter implements NoPingAnAuthenticateContract.Presenter {

    NoPingAnAuthenticateContract.View view;

    private Subscription subscription;

    public NoPingAnAuthenticatePresenter(NoPingAnAuthenticateContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        loadUserInfo();
    }

    @Override
    public void detach() {
        RxUtil.remove(subscription);
    }


    private void loadUserInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        RefreshUserInfoService refreshUserInfoService = ApiServiceGenerator.createService(RefreshUserInfoService.class);
        subscription = refreshUserInfoService
                .refreshUserInfo(map)
                .map(new NetworkResultFunc1<UserInfo>())
                .onErrorResumeNext(new ThrowableFiltrateFunc<UserInfo>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UserInfo>() {
                    @Override
                    public void call(UserInfo userInfo) {
                        view.setViewByType(userInfo.getEnterStatus());
                        view.setAuthenticateType(userInfo.getEnterType().equalsIgnoreCase("PERSONAL"));
                        SPUtil.saveObjectToShare(view.getActivityContext(), Constants.KEY_USER_INFO, userInfo);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (throwable instanceof ApiCodeErrorException) {
                            ServerErrorCodeDispatch.getInstance().checkErrorCode(view.getActivityContext(), ((ApiCodeErrorException) throwable).getState(), throwable.getMessage());
                        } else {
                            ServerErrorCodeDispatch.getInstance().checkErrorCode(view.getActivityContext(), "", throwable.getMessage());
                        }
                    }
                });
        RxUtil.addSubscription(subscription);
    }

}
