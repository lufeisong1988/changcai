package com.changcai.buyer.interface_api;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by lufeisong on 2017/9/1.
 */

public interface TestService {
    @FormUrlEncoded
    @POST("getTopFive")
    Observable<BaseApiModel<Object>> signContract(@FieldMap Map<String,String> requestMap);
}
