package com.changcai.buyer.business_logic.about_buy_beans.assign_platform;

import android.support.annotation.NonNull;

import com.changcai.buyer.R;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.common.Urls;
import com.changcai.buyer.http.HttpListener;
import com.changcai.buyer.http.VolleyUtil;
import com.google.gson.JsonObject;
import com.juggist.commonlibrary.rx.RefreshOrderEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuxingwei on 2016/11/22.
 */

public class AssignPlatformPresenter implements AssignPlatformContract.Presenter {


    AssignPlatformContract.View view;

    public AssignPlatformPresenter(@NonNull AssignPlatformContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        view.webViewLoadUrl();
    }

    @Override
    public void detach() {
        view = null;
    }

    @Override
    public void commitAssignPlatformContract(String tokenId) {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("tokenId", tokenId);
        VolleyUtil.getInstance().httpPost(view.getActivityContext(), Urls.ASSIGN_PLATFORM_CONTRACT, parameters, new HttpListener() {

            @Override
            public void onResponseJson(JsonObject response) {
                super.onResponseJson(response);
                String errorCode = response.get(Constants.RESPONSE_CODE).getAsString();
                if (errorCode.equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {
                    RefreshOrderEvent.publish(true);
                    if (view.isActive())
                    view.showSuccessDialog(R.string.assign_platform_agreement_success);
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                if (view.isActive())
                view.showErrorDialog(error);
            }
        }, true);
    }
}
