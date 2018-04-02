package com.changcai.buyer.interface_api;

import com.changcai.buyer.bean.StrategyTargetModel;
import com.changcai.buyer.common.Urls;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by lufeisong on 2018/3/6.
 */

public interface GetStrategyTargetService {
    @FormUrlEncoded
    @POST(Urls.GETSTRATEGYTARGET)
    Observable<BaseApiModel<StrategyTargetModel>> getStrateyTarget(@FieldMap Map<String,String> requestMap);
}
