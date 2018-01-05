package com.changcai.buyer.interface_api;

import com.changcai.buyer.common.Urls;
import com.changcai.buyer.ui.order.bean.DeliveryInfo;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by liuxingwei on 2017/4/7.
 */

public interface GetDeliveryInfo {

    @FormUrlEncoded
    @POST(Urls.GET_DELIVERY_INFO)
    Observable<BaseApiModel<DeliveryInfo>> getDelivery(@FieldMap Map<String,String> requestMap);
}
