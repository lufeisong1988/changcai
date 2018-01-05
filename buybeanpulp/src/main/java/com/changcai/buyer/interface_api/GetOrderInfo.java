package com.changcai.buyer.interface_api;

import com.changcai.buyer.common.Urls;
import com.changcai.buyer.ui.order.bean.DeliveryInfo;
import com.changcai.buyer.ui.order.bean.OrderInfo;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by liuxingwei on 2017/1/11.
 * 5.3   获取订单详情
 */

public interface GetOrderInfo {

    @FormUrlEncoded
    @POST(Urls.GET_ORDER_INFO_REST)
    Observable<BaseApiModel<OrderInfo>> getOrderInfo(@FieldMap Map<String,String> requestMap);





}
