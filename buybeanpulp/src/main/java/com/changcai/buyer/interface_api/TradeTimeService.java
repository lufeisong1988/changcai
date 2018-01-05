package com.changcai.buyer.interface_api;

import com.changcai.buyer.bean.TradeTime;
import com.changcai.buyer.common.Urls;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by liuxingwei on 2016/12/23.
 */
public interface TradeTimeService {

    @FormUrlEncoded
    @POST(Urls.GET_SUMMARY)
    Observable<BaseApiModel<TradeTime>> getTradeTime(@FieldMap Map<String,String> requestMap);
}
