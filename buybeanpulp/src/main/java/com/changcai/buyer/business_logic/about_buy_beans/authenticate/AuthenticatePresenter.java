package com.changcai.buyer.business_logic.about_buy_beans.authenticate;

import android.support.annotation.NonNull;
import android.util.Log;

import com.changcai.buyer.bean.DetailsIdent;
import com.changcai.buyer.bean.UserInfo;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.interface_api.ApiServiceGenerator;
import com.changcai.buyer.interface_api.BaseApiModel;
import com.changcai.buyer.interface_api.GetIdentityDetailsService;
import com.changcai.buyer.interface_api.NetworkResultFunc1;
import com.changcai.buyer.interface_api.RefreshUserInfoService;
import com.changcai.buyer.util.RxUtil;
import com.changcai.buyer.util.SPUtil;
import com.juggist.commonlibrary.rx.RefreshOrderEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by liuxingwei on 2016/11/17.
 */

public class AuthenticatePresenter implements AuthenticateContract.Presenter {

    private String TAG = AuthenticatePresenter.class.getSimpleName();
    /*
     *main view
     */
    AuthenticateContract.View view;
    private Subscription subscription;
    private Subscription refreshOrderSubscription;
    private Subscription detailsSubscription;

    public AuthenticatePresenter(@NonNull AuthenticateContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    /**
     * start 方法可能会调用很多次，所以先remove
     */
    private void registerObservable() {
        RxUtil.remove(refreshOrderSubscription);
        refreshOrderSubscription = RefreshOrderEvent.getObservable().subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                refreshUserInfo();
            }
        });
        RxUtil.addSubscription(refreshOrderSubscription);
    }

    @Override
    public void start() {
        registerObservable();
    }

    @Override
    public void detach() {
        RxUtil.remove(refreshOrderSubscription);
        RxUtil.remove(subscription);
        RxUtil.remove(detailsSubscription);
        view = null;
        Log.d(TAG, "view == null");
    }

    @Override
    public void refreshUserInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        RefreshUserInfoService refreshUserInfoService = ApiServiceGenerator.createService(RefreshUserInfoService.class);
        subscription = refreshUserInfoService
                .refreshUserInfo(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseApiModel<UserInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (view != null && view.isActive())
                            view.showErrorDialog(e.getMessage());
                    }

                    @Override
                    public void onNext(BaseApiModel<UserInfo> userInfoBaseApiModel) {
                        if (userInfoBaseApiModel.getErrorCode() .equalsIgnoreCase( Constants.REQUEST_SUCCESS_S)) {
                            SPUtil.saveObjectToShare(Constants.KEY_USER_INFO, userInfoBaseApiModel.getResultObject());
                            if (view != null && view.isActive())
                                view.refreshUI();
                        }
                    }
                });
        RxUtil.addSubscription(subscription);
    }

    @Override
    public void loadIdentityDetails() {
        Map<String, String> fieldMap = new HashMap<>();
        fieldMap.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        GetIdentityDetailsService getIdentityDetailsService = ApiServiceGenerator.createService(GetIdentityDetailsService.class);
        RxUtil.remove(detailsSubscription);
        detailsSubscription = getIdentityDetailsService.getIdenttityDetails(fieldMap)
                .subscribeOn(Schedulers.io())
                .delay(500, TimeUnit.MILLISECONDS)
                .map(new NetworkResultFunc1<DetailsIdent>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DetailsIdent>() {
                    @Override
                    public void call(DetailsIdent detailsIdent) {
                        SPUtil.saveObjectToShare(Constants.KEY_IDENT_DETAILS,detailsIdent);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        view.showErrorDialog(throwable.getMessage());
                    }
                });
    }
}
