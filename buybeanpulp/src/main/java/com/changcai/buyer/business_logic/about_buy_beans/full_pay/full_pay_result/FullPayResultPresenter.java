package com.changcai.buyer.business_logic.about_buy_beans.full_pay.full_pay_result;

import com.changcai.buyer.common.Constants;
import com.changcai.buyer.interface_api.ApiServiceGenerator;
import com.changcai.buyer.interface_api.DirectPayService;
import com.changcai.buyer.interface_api.NetworkResultFunc1;
import com.changcai.buyer.util.RxUtil;
import com.changcai.buyer.util.SPUtil;

import java.util.HashMap;
import java.util.Map;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by liuxingwei on 2017/6/1.
 */

public class FullPayResultPresenter implements FullPayResultContract.Presenter {

    FullPayResultContract.View view;
    private Subscription directPayToSellerSubscription;

    public FullPayResultPresenter(FullPayResultContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void directPayToSeller(String orderId) {

        DirectPayService directPayService = ApiServiceGenerator.createService(DirectPayService.class);
        Map<String,String> parameters = new HashMap<>();
        parameters.put("orderId",orderId);
        parameters.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        RxUtil.remove(directPayToSellerSubscription);
        directPayToSellerSubscription = directPayService
                .directPay(parameters)
                .map(new NetworkResultFunc1<String>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        if (view !=null)
                            view.showImmediatePaySuccess();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (view!=null)
                            view.showErrorDialog(throwable.getMessage());
                    }
                });

    }

    @Override
    public void detach() {
        view =null;
    }
}
