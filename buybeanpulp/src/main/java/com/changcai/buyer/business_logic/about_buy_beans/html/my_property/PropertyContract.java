package com.changcai.buyer.business_logic.about_buy_beans.html.my_property;

import com.changcai.buyer.BasePresenter;
import com.changcai.buyer.BaseView;

/**
 * Created by liuxingwei on 2017/1/5.
 */

public interface PropertyContract {

    interface View extends BaseView<PropertyContract.Presenter> {
        void webViewSetting();
        void loadWebUrl();
        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void loadLink();
    }
}
