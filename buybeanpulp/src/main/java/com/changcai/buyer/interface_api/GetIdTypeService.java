package com.changcai.buyer.interface_api;

import com.changcai.buyer.common.Urls;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by liuxingwei on 2017/1/9.
 */

public interface GetIdTypeService {

    @FormUrlEncoded
    @POST(Urls.GET_ID_TYPE)
    Observable<BaseApiModel<String>> getType(@FieldMap Map<String,String> requestMap);
}
