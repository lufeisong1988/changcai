package com.changcai.buyer.business_logic.about_buy_beans.html.my_property;

/**
 * Created by liuxingwei on 2017/1/5.
 */

public class PropertyPresenter  implements PropertyContract.Presenter{

    PropertyContract.View view;

    public PropertyPresenter(PropertyContract.View view) {
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
