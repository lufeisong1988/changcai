package com.changcai.buyer.business_logic.about_buy_beans.person_authenticate.child_accountId;

import com.changcai.buyer.BasePresenter;
import com.changcai.buyer.BaseView;

/**
 * Created by liuxingwei on 2016/11/24.
 */

public interface ChildAccountContract {


    interface View extends BaseView<ChildAccountContract.Presenter> {
        void showAuthenticateInfo(String relation, String relationCompany, String buyerPermissions, String salesmanPermissions);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {
    }
}
