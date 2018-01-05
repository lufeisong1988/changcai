package com.changcai.buyer.interface_api;

import com.changcai.buyer.bean.UserInfo;
import com.changcai.buyer.common.Urls;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by liuxingwei on 2016/11/30.
 */

public interface RefreshUserInfoService {

    @FormUrlEncoded
    @POST(Urls.REFRESH_USERINFO)
    Observable<BaseApiModel<UserInfo>> refreshUserInfo(@FieldMap Map<String,String> requestMap);
}
