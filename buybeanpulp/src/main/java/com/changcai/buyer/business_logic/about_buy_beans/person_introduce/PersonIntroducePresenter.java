package com.changcai.buyer.business_logic.about_buy_beans.person_introduce;


import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.UserInfo;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.common.Urls;
import com.changcai.buyer.http.HttpListener;
import com.changcai.buyer.http.VolleyUtil;
import com.changcai.buyer.rx.RefreshOrderEvent;
import com.changcai.buyer.util.SPUtil;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuxingwei on 2016/11/22.
 */

public class PersonIntroducePresenter implements PersonIntroduceContract.Presenter {


    PersonIntroduceContract.View view;

    private boolean isFirstInputInformation;
    private UserInfo userInfo;

    public PersonIntroducePresenter(PersonIntroduceContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void commitIntroduce(@Nullable String sellerIntroduce, @Nullable String buyerIntroduce) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        parameters.put("sellerInformation", TextUtils.isEmpty(sellerIntroduce) ? "" : sellerIntroduce);
        parameters.put("buyerInformation", TextUtils.isEmpty(buyerIntroduce) ? "" : buyerIntroduce);

        VolleyUtil.getInstance().httpPost(view.getActivityContext(), Urls.POST_COMMIT_INTRODUCE, parameters, new HttpListener() {
            @Override
            public void onResponseJson(JsonObject response) {
                super.onResponseJson(response);
                String errorCode = response.get(Constants.RESPONSE_CODE).getAsString();
                if (errorCode.contentEquals(Constants.REQUEST_SUCCESS_S)) {
                    if (isFirstInputInformation) {
                        view.showSuccessDialogWithTitle(R.string.all_step_done, R.string.certify_done);
                    } else {
                        view.showSuccessDialog(R.string.modify_success);
                    }
                    RefreshOrderEvent.publish(true);
                } else {
                    view.showErrorDialog("错误");
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                view.showErrorDialog(error);
            }
        }, true);
    }

    @Override
    public void start() {
        userInfo = SPUtil.getObjectFromShare(Constants.KEY_USER_INFO);
        if (null != userInfo) {
            view.showBuyerView(userInfo.getBuyerInformation());
            if (TextUtils.isEmpty(userInfo.getBuyerInformation()) && TextUtils.isEmpty(userInfo.getSellerInformation())) {
                isFirstInputInformation = true;
            }
        }

    }

    @Override
    public void detach() {
        view = null;
    }

    @Override
    public void checkInputIsLegal(String buyerIntroduce) {

        if (TextUtils.isEmpty(buyerIntroduce)) {

            view.showErrorDialog(view.getActivityContext().getString(R.string.please_input_buyer_introduce));

        } else if (buyerIntroduce.length() < 10) {
            view.showErrorDialog(view.getActivityContext().getString(R.string.string_limit_for_buyer));
        } else {
            commitIntroduce(null, buyerIntroduce);
        }


    }
}
