package com.changcai.buyer.business_logic.about_buy_beans.reset_paypassword;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.DetailsIdent;
import com.changcai.buyer.bean.UserInfo;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.http.VolleyUtil;
import com.changcai.buyer.interface_api.ApiServiceGenerator;
import com.changcai.buyer.interface_api.BaseApiModel;
import com.changcai.buyer.interface_api.GetIdentityDetailsService;
import com.changcai.buyer.interface_api.NetworkResultFunc1;
import com.changcai.buyer.interface_api.ResetPayPassService;
import com.changcai.buyer.interface_api.SendSmsResetPayPassService;
import com.changcai.buyer.util.LogUtil;
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
 * Created by liuxingwei on 2016/11/29.
 */

public class ResetPayPasswordPresenter implements ResetPayPasswordContract.Presenter {
    private String TAG = ResetPayPasswordPresenter.class.getSimpleName();
    protected UserInfo userInfo;
    ResetPayPasswordContract.View view;

    protected Subscription subscription;
    protected Subscription smsSubscription;
    private Subscription detailsSubscription;


    public ResetPayPasswordPresenter(ResetPayPasswordContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void sendDynamicCode(String tokenId) {
        Map<String, String> map = new HashMap<>();
        map.put("tokenId", tokenId);
        SendSmsResetPayPassService sendSmsResetPayPassService = ApiServiceGenerator.createService(SendSmsResetPayPassService.class);
        smsSubscription = sendSmsResetPayPassService
                .sendSmsCode(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseApiModel<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showErrorDialog(e.getMessage());
                    }


                    @Override
                    public void onNext(BaseApiModel<String> stringBaseApiModel) {
                        if (stringBaseApiModel.getErrorCode().equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {
                            if (view.isActive())
                                view.showToast(view.getActivityContext().getString(R.string.send_sms_success));
                            view.startTimerTask();
                        } else {
                            if (view.isActive())
                                view.showErrorDialog(stringBaseApiModel.getErrorDesc());
                            view.closeTimer();
                        }

                    }
                });
        RxUtil.addSubscription(smsSubscription);

    }

    @Override
    public void commitUpdate(String tokenId, String payPassword, String code, String identityNo) {

        Map<String, String> map = new HashMap<>();
        map.put("tokenId", tokenId);
        map.put("payPassword", payPassword);
        map.put("identityNo", identityNo);
        map.put("code", code);
        ResetPayPassService resetPayPassService = ApiServiceGenerator.createService(ResetPayPassService.class);
        subscription = resetPayPassService
                .resetPayPassword(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseApiModel<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showErrorDialog(e.getMessage());
                    }

                    @Override
                    public void onNext(BaseApiModel<String> stringBaseApiModel) {
                        if (stringBaseApiModel.getErrorCode().equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {
                            RefreshOrderEvent.publish(true);
                            if (view.isActive())

                                view.showSuccessDialog();
                        } else {
                            if (view.isActive())

                                view.showErrorDialog(stringBaseApiModel.getErrorDesc());
                        }
                    }
                });
        RxUtil.addSubscription(subscription);
    }

    @Override
    public void loadIdentityDetails(String tokenId) {
        Map<String, String> fieldMap = new HashMap<>();
        fieldMap.put("tokenId", tokenId);
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
//                        SPUtil.saveObjectToShare(Constants.KEY_IDENT_DETAILS, detailsIdent);
                        VolleyUtil.getInstance().cancelProgressDialog();
                        view.setEnterType(detailsIdent);

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        VolleyUtil.getInstance().cancelProgressDialog();
                        LogUtil.d(TAG, throwable.toString());
                        view.showErrorDialog(throwable.getLocalizedMessage());
                    }
                });
    }


    @Override
    public void start() {
        userInfo = SPUtil.getObjectFromShare(Constants.KEY_USER_INFO);
    }

    @Override
    public void detach() {
        view = null;
        RxUtil.remove(smsSubscription);
        RxUtil.remove(subscription);
    }
}
