package com.changcai.buyer.interface_api;

import com.changcai.buyer.bean.DetailsIdent;
import com.changcai.buyer.common.Urls;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by liuxingwei on 2016/12/1.
 */

public interface GetIdentityDetailsService {
    @FormUrlEncoded
    @POST(Urls.IDENTITY_DETAILD)
    Observable<BaseApiModel<DetailsIdent>> getIdenttityDetails(@FieldMap Map<String,String> requestMap);
}
