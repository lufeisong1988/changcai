package com.changcai.buyer.business_logic.about_buy_beans.bind_bank;



import com.changcai.buyer.BasePresenter;
import com.changcai.buyer.BaseView;

/**
 * Created by liuxingwei on 2017/3/23.
 */

public interface BindBankContract {

    interface View extends BaseView<BindBankContract.Presenter> {
        void webViewSetting();
        void loadWebUrl(String fileName);

    }

    interface Presenter extends BasePresenter {


        void loadLink();
    }
}
