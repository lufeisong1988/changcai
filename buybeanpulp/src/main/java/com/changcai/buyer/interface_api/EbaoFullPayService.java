package com.changcai.buyer.interface_api;

import com.changcai.buyer.common.Urls;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by liuxingwei on 2017/1/12.
 */

public interface EbaoFullPayService {

    @FormUrlEncoded
    @POST(Urls.VALIDATE_PAY_PASS)
    Observable<BaseApiModel<String>> validatePayPass(@FieldMap Map<String,String> requestMap);

    @FormUrlEncoded
    @POST(Urls.EBAO_FULL_PAY)
    Observable<BaseApiModel<String>> ebaoFullPay(@FieldMap Map<String,String> requestMap);


    @FormUrlEncoded
    @POST(Urls.PAY_DELIVERY)
    Observable<BaseApiModel<String>> payOrdDelivery(@FieldMap Map<String,String> requestMap);


    @FormUrlEncoded
    @POST(Urls.PAY_FRONT_MONEY)
    Observable<BaseApiModel<String>> payFrontMoney(@FieldMap Map<String,String> requestMap);




}
