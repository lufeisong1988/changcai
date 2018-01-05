package com.changcai.buyer.common;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.changcai.buyer.R;
import com.changcai.buyer.business_logic.about_buy_beans.recharge.RechargeActivity;
import com.changcai.buyer.interface_api.ApiCodeErrorException;
import com.changcai.buyer.interface_api.ApiServiceGenerator;
import com.changcai.buyer.interface_api.BaseApiModel;
import com.changcai.buyer.interface_api.GetIdTypeService;
import com.changcai.buyer.rx.RxBus;
import com.changcai.buyer.util.DesUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.ServerErrorCodeDispatch;
import com.changcai.buyer.util.UserDataUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liuxingwei on 2017/3/23.
 */

public class CommonJsInterface {


    private Context context;

    public CommonJsInterface(Context context) {
        this.context = context;
    }

    /**
     * Show a toast from the web page
     * 抽取公用为了兼容以前的版本把该接口放在此类中
     */
    @JavascriptInterface
    public void jsCallResult(String object) {
        GetIdTypeService getIdTypeService = ApiServiceGenerator.createService(GetIdTypeService.class);
        Map<String, String> parameter = new HashMap<>();
        parameter.put("tokenId", UserDataUtil.getTokenId());
        getIdTypeService.getType(parameter)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseApiModel<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof ApiCodeErrorException && ((ApiCodeErrorException) e).getState().contentEquals("101")) {
                            ServerErrorCodeDispatch.getInstance().checkErrorCode(context, "101", e.getMessage());
                            return;
                        }

                        Map<String, Throwable> map = new HashMap<>();
                        map.put("error", e);
                        RxBus.get().post("MyProperty", map);
                    }

                    @Override
                    public void onNext(BaseApiModel<String> stringBaseApiModel) {
                        Map<String, String> map = new HashMap<>();
                        map.put("canRecharge", stringBaseApiModel.getResultObject());
                        RxBus.get().post("MyProperty", map);
                    }
                });
    }

    /**
     * 通用逻辑获取tokenId
     *
     * @return
     */
    @JavascriptInterface
    public String jsCallOcGetUserAccountList() {
        Map<String, String> parameters = SPUtil.getObjectFromShare(Constants.REQUEST_BASE_PARAMETERS);
        parameters.put("tokenId", UserDataUtil.getTokenId());
        Gson gson = new Gson();
        String requestJSON = DesUtil.encrypt(gson.toJson(parameters), "abcdefgh");
        return requestJSON;
    }
}
