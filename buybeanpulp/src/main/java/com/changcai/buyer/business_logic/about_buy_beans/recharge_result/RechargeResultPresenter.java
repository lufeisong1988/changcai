package com.changcai.buyer.business_logic.about_buy_beans.recharge_result;

/**
 * Created by liuxingwei on 2017/1/8.
 */

public class RechargeResultPresenter implements RechargeResultContract.Presenter{

    RechargeResultContract.View view;


    public RechargeResultPresenter(RechargeResultContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void detach() {

    }
}
