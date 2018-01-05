package com.changcai.buyer.business_logic.about_buy_beans.ping_an_bank_card_no;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.EbaoBalanceInfo;
import com.changcai.buyer.interface_api.ApiServiceGenerator;
import com.changcai.buyer.interface_api.GetAccountBalance;
import com.changcai.buyer.interface_api.NetworkResultFunc1;
import com.changcai.buyer.util.RxUtil;
import com.changcai.buyer.util.UserDataUtil;

import java.util.HashMap;
import java.util.Map;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by liuxingwei on 2017/1/8.
 */

public class PingAnEasyPayRechargePresenter implements PingAnEasyPayRechargeContract.Presenter {

    PingAnEasyPayRechargeContract.View view;
    private Subscription accountSubscription;

    public PingAnEasyPayRechargePresenter(PingAnEasyPayRechargeContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void detach() {
        RxUtil.remove(accountSubscription);
    }

    @Override
    public void getAccountBalance() {
        GetAccountBalance getAccountBalance = ApiServiceGenerator.createService(GetAccountBalance.class);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("tokenId", UserDataUtil.getTokenId());
        RxUtil.remove(accountSubscription);
        accountSubscription = getAccountBalance
                .getAccountInfo(parameters)
                .map(new NetworkResultFunc1<EbaoBalanceInfo>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<EbaoBalanceInfo>() {
                    @Override
                    public void call(EbaoBalanceInfo ebaoBalanceInfo) {
                        if (view.isActive())
                            view.setBankInfo(ebaoBalanceInfo);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (view.isActive()) {
                            view.showErrorDialog(view.getActivityContext().getString(R.string.get_balance_failed));
                        }
                    }
                });

    }
}
