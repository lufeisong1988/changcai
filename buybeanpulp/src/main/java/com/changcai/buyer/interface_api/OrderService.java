package com.changcai.buyer.interface_api;

import com.changcai.buyer.common.Urls;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by liuxingwei on 2017/1/10.
 * 订单接口
 */

public interface OrderService {

    /**
     * 验证下单
     * @param requestMap
     * @return
     */
    @FormUrlEncoded
    @POST(Urls.VALIDCREATEORDER)
    Observable<BaseApiModel<String>> validCreateOrder(@FieldMap Map<String,String> requestMap);


    @FormUrlEncoded
    @POST(Urls.CREATEORDER)
    Observable<BaseApiModel<Object>> createOrder(@FieldMap Map<String,String> requestMap);


}
