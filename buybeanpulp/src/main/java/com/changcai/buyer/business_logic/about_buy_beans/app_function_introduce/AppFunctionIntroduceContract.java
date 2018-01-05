package com.changcai.buyer.business_logic.about_buy_beans.app_function_introduce;

import com.changcai.buyer.BasePresenter;
import com.changcai.buyer.BaseView;

/**
 * Created by liuxingwei on 2016/11/23.
 */

public interface AppFunctionIntroduceContract {

    interface View extends BaseView<Presenter> {
        void webViewSetting();
        void loadWebUrl();
        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void loadLink();
    }

}
