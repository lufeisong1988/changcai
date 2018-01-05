package com.changcai.buyer.business_logic.about_buy_beans.app_function_introduce;

/**
 * Created by liuxingwei on 2016/11/23.
 */

public class AppFunctionIntroducePresenter implements AppFunctionIntroduceContract.Presenter {


    AppFunctionIntroduceContract.View view;

    public AppFunctionIntroducePresenter(AppFunctionIntroduceContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void loadLink() {
        view.loadWebUrl();
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
