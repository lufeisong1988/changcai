package com.changcai.buyer.interface_api.service_model.base;

/**
 * Created by lufeisong on 2018/3/12.
 */

public interface ServiceRequestCallback<T> {
    void onSucceed(T t);
    void onFail(String error);
    void onError();


}
