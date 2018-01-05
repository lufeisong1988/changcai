package com.changcai.buyer.interface_api;

import com.changcai.buyer.common.Urls;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by liuxingwei on 2017/1/14.
 */

public interface DirectPayService {

    @FormUrlEncoded
    @POST(Urls.DIRECT_PAY)
    Observable<BaseApiModel<String>> directPay(@FieldMap Map<String,String> requestMap);
}
