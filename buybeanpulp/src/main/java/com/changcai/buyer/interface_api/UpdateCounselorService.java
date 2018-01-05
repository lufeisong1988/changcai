package com.changcai.buyer.interface_api;

import com.changcai.buyer.common.Urls;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by lufeisong on 2017/12/27.
 */

public interface UpdateCounselorService {
    @FormUrlEncoded
    @POST(Urls.UPDATE_COUNSELOR)
    Observable<BaseApiModel<String>> getUserLevel(@FieldMap Map<String,String> requestMap);
}
