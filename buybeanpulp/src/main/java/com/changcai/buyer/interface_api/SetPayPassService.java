package com.changcai.buyer.interface_api;

import com.changcai.buyer.common.Urls;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by liuxingwei on 2016/11/29.
 */

public interface SetPayPassService {

    @FormUrlEncoded
    @POST(Urls.SET_PASSWORD)
    Observable<BaseApiModel<String>> setPayWord(@FieldMap Map<String,String> requestMap);
}
