package com.changcai.buyer.interface_api;

import com.changcai.buyer.bean.UserInfo;
import com.changcai.buyer.common.Urls;

import java.util.Map;
import java.util.Set;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by liuxingwei on 2016/12/21.
 */

public interface MyPushTagsService {
    @FormUrlEncoded
    @POST(Urls.GETMYPUSHTAGS)
    Observable<BaseApiModel<Set<String>>> getMyPushTags(@FieldMap Map<String,String> requestMap);
}
