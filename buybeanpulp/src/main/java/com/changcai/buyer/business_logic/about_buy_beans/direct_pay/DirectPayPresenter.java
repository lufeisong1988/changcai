package com.changcai.buyer.business_logic.about_buy_beans.direct_pay;

/**
 * Created by liuxingwei on 2017/1/11.
 */

public class DirectPayPresenter implements DirectPayContract.Presenter {


    DirectPayContract.View view;


    public DirectPayPresenter(DirectPayContract.View view) {
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
