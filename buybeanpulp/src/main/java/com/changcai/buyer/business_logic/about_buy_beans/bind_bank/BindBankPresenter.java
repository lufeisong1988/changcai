package com.changcai.buyer.business_logic.about_buy_beans.bind_bank;

/**
 * Created by liuxingwei on 2017/3/23.
 */

public class BindBankPresenter implements BindBankContract.Presenter {

    BindBankContract.View view;

    public BindBankPresenter(BindBankContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void loadLink() {
        view.loadWebUrl("bindingBank.html");
    }

    @Override
    public void start() {
        view.webViewSetting();
    }
    @Override
    public void detach() {
        view = null;
    }
}
