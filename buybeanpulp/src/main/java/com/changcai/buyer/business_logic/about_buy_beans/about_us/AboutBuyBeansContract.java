package com.changcai.buyer.business_logic.about_buy_beans.about_us;

import com.changcai.buyer.BasePresenter;
import com.changcai.buyer.BaseView;

/**
 * Created by liuxingwei on 2016/11/23.
 */

public interface AboutBuyBeansContract {

    interface View extends BaseView<Presenter> {
        void showCheckResultToast(boolean isHaveNewVersion);
        boolean isActive();
        void showNewUpdateVersion(String content);
    }

    interface Presenter extends BasePresenter {

        void checkNewVersion();
    }
}
