package com.changcai.buyer.business_logic.about_buy_beans.deposit_and_pay;

/**
 * Created by liuxingwei on 2017/3/20.
 */
public class DepositAndPayPresenter implements DepositAndPayContract.Presenter{


    DepositAndPayContract.View view;


    public DepositAndPayPresenter(DepositAndPayContract.View view) {
        this.view = view;
    }

    @Override
    public void start() {

    }

    @Override
    public void detach() {

    }
}
