package com.changcai.buyer.interface_api;

import com.changcai.buyer.common.Urls;
import com.changcai.buyer.ui.order.bean.OrderInfo;
import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by liuxingwei on 2017/1/11.
 */

public interface SignContractService {

    @FormUrlEncoded
    @POST(Urls.SIGN_CONTRACT)
    Observable<BaseApiModel<Object>> signContract(@FieldMap Map<String,String> requestMap);
}
