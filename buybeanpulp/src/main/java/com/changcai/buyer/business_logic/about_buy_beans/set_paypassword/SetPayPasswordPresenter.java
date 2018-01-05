package com.changcai.buyer.business_logic.about_buy_beans.set_paypassword;

import android.os.Bundle;
import android.text.TextUtils;

import com.changcai.buyer.R;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.interface_api.ApiServiceGenerator;
import com.changcai.buyer.interface_api.NetworkResultFunc1;
import com.changcai.buyer.interface_api.ResetPayPassService;
import com.changcai.buyer.interface_api.SetPayPassService;
import com.changcai.buyer.rx.RefreshOrderEvent;
import com.changcai.buyer.util.RxUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by liuxingwei on 2016/11/29.
 */

public class SetPayPasswordPresenter implements SetPayPasswordContract.Presenter {

    SetPayPasswordContract.View view;

    protected static Subscription subscription;

    public SetPayPasswordPresenter(SetPayPasswordContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }


    private void setPayPassword(String payPassword) {
        Map<String, String> map = new HashMap<>();
        map.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        map.put("payPassword", payPassword);
        SetPayPassService setPayPassService = ApiServiceGenerator.createService(SetPayPassService.class);
        subscription = setPayPassService
                .setPayWord(map)
                .subscribeOn(Schedulers.io())
                .map(new NetworkResultFunc1<String>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        view.showSuccessDialog();
                        RefreshOrderEvent.publish(true);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        view.showErrorDialog(throwable.getMessage());
                    }
                });
        RxUtil.addSubscription(subscription);
    }

    private void resetPayPassword(String payPassword, Bundle bundle) {
        Map<String, String> map = new HashMap<>();
        map.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        map.put("payPassword", payPassword);
        map.put("code", bundle.getString("dynamicCode", ""));
        map.put("identityNo", bundle.getString("companyCode", ""));
        ResetPayPassService setPayPassService = ApiServiceGenerator.createService(ResetPayPassService.class);
        subscription = setPayPassService
                .resetPayPassword(map)
                .subscribeOn(Schedulers.io())
                .map(new NetworkResultFunc1<String>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        view.showSuccessDialog();
                        RefreshOrderEvent.publish(true);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        view.showErrorDialog(throwable.getMessage());
                    }
                });
        RxUtil.addSubscription(subscription);
    }


    @Override
    public void start() {

    }

    @Override
    public void detach() {
        view = null;
        RxUtil.remove(subscription);
    }

    @Override
    public void checkout(String password, String confirmPassword, Bundle bundle) {
        if (TextUtils.isEmpty(password)) {
            view.showErrorDialog(view.getActivityContext().getString(R.string.password_empty_limit));
            return;
        }
        if (!StringUtil.checkPassword(password)) {
            view.showErrorDialog(view.getActivityContext().getString(R.string.password_limit));
            return;
        }

        if (!password.equalsIgnoreCase(confirmPassword)) {
            view.showErrorDialog(view.getActivityContext().getString(R.string.password_equals_limit));
            return;
        }

        //bundle.getBoolean("isReset")==false
        if (bundle !=null && bundle.containsKey("isReset") && !bundle.getBoolean("isReset")) {
            resetPayPassword(password, bundle);
        } else {
            setPayPassword(password);
        }
    }
}
