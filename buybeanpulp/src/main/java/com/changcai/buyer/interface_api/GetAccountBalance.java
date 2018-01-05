package com.changcai.buyer.interface_api;

import com.changcai.buyer.bean.EbaoBalanceInfo;
import com.changcai.buyer.bean.PlatformAmount;
import com.changcai.buyer.bean.TradeTime;
import com.changcai.buyer.common.Urls;

import java.util.List;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by liuxingwei on 2017/1/8.
 */

public interface GetAccountBalance {
    @FormUrlEncoded
    @POST(Urls.GET_EBAO_BALANCE)
    Observable<BaseApiModel<EbaoBalanceInfo>> getAccountInfo(@FieldMap Map<String,String> requestMap);


    @FormUrlEncoded
    @POST(Urls.USER_ACCOUNT)
    Observable<BaseApiModel<List<PlatformAmount>>> getUserPlatformTotalAmount(@FieldMap Map<String,String> requestMap);

}
