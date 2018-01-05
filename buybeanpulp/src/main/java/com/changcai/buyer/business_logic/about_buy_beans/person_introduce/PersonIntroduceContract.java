package com.changcai.buyer.business_logic.about_buy_beans.person_introduce;

import android.content.Context;

import com.changcai.buyer.BasePresenter;
import com.changcai.buyer.BaseView;

/**
 * Created by liuxingwei on 2016/11/22.
 */

public interface PersonIntroduceContract {

    interface View extends BaseView<PersonIntroduceContract.Presenter> {


        boolean isActive();
        Context getActivityContext();
        void showErrorDialog(String errorMessage);
        void showSuccessDialog(int message);
        void showSuccessDialogWithTitle(int message,int title);
        void showBuyerView(String buyerInformation);
    }

    interface Presenter extends BasePresenter {
        void commitIntroduce(String sellerIntroduce,String buyerIntroduce);
       void checkInputIsLegal(String buyerIntroduce);

    }

}
