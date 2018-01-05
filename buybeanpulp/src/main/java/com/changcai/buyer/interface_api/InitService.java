package com.changcai.buyer.interface_api;

import com.changcai.buyer.bean.InitModel;
import com.changcai.buyer.common.Urls;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by liuxingwei on 2016/12/2.
 */

public interface InitService {

    @FormUrlEncoded
    @POST(Urls.INIT)
    Observable<BaseApiModel<InitModel>> appInit(@FieldMap Map<String,String> requestMap);
}
