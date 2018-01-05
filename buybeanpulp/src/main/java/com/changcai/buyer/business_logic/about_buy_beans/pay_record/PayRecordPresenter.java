package com.changcai.buyer.business_logic.about_buy_beans.pay_record;

/**
 * Created by liuxingwei on 2017/4/6.
 */

public class PayRecordPresenter implements PayRecordContract.Presenter {

    PayRecordContract.View view;

    public PayRecordPresenter(PayRecordContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void detach() {
        view = null;
    }
}
