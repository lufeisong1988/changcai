package com.changcai.buyer.business_logic.about_buy_beans.authenticate;

import android.content.Context;
import android.support.annotation.Keep;

import com.changcai.buyer.BasePresenter;
import com.changcai.buyer.BaseView;

/**
 * Created by liuxingwei on 2016/11/17.
 */

@Keep
public interface AuthenticateContract {
    interface View extends BaseView<Presenter> {


        void showStepFirstIconLevelListDrawable(int level);
        void showStepSecondIconLevelListDrawable(int level);
        void showStepThirdIconLevelListDrawable(int level);
        void showStepFourthIconLevelListDrawable(int level);
        void showStepFifthIconLevelListDrawable(int level);
        void showAuthenticateDialog();
        boolean isActive();
        void refreshUI();
        Context getActivityContext();
    }

    interface Presenter extends BasePresenter {

        void refreshUserInfo();

        void loadIdentityDetails();
    }

}
