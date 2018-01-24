package com.changcai.buyer.interface_api;

import com.changcai.buyer.common.Urls;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by lufeisong on 2018/1/24.
 */

public interface InitImMsgService {
    @FormUrlEncoded
    @POST(Urls.INIT_IM_MSG)
    Observable<BaseApiModel<String>> InitImMsg(@FieldMap Map<String,String> requestMap);
}
