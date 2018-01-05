package com.changcai.buyer.interface_api;

import com.changcai.buyer.common.Urls;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by liuxingwei on 2017/1/8.
 */

public interface EbaoRechargeService {

    @FormUrlEncoded
    @POST(Urls.EBAO_RECHARGE)
    Observable<BaseApiModel<String>> recharge(@FieldMap Map<String,String> requestMap);


    @FormUrlEncoded
    @POST(Urls.VALIDATE_PAY_PASS)
    Observable<BaseApiModel<String>> validatePayPass(@FieldMap Map<String,String> requestMap);
}
