package com.changcai.buyer.interface_api;

import com.changcai.buyer.common.Urls;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by liuxingwei on 2016/11/28.
 */

public interface DoIdentityService {

    @FormUrlEncoded
    @POST(Urls.DO_IDENTITY)
    Observable<BaseApiModel<String>> doIdentity(@FieldMap Map<String,String> requestMap);
}
