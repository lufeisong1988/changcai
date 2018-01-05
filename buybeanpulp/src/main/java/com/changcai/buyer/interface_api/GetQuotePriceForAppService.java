package com.changcai.buyer.interface_api;

import com.changcai.buyer.bean.GetQuotePriceBean;
import com.changcai.buyer.common.Urls;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by lufeisong on 2017/11/17.
 */

public interface GetQuotePriceForAppService {
    @FormUrlEncoded
    @POST(Urls.GET_QUOTE_PRICE_FOR_APP)
    Observable<BaseApiModel<GetQuotePriceBean>> getOrderInfo(@FieldMap Map<String,String> requestMap);

}
