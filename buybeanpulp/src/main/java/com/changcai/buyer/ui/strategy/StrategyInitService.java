package com.changcai.buyer.ui.strategy;

import com.changcai.buyer.bean.StategyInitModel;
import com.changcai.buyer.bean.StrategyInitModel;
import com.changcai.buyer.common.Urls;
import com.changcai.buyer.interface_api.BaseApiModel;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by liuxingwei on 2017/8/1.
 */

public interface StrategyInitService {

    /**
     * 策略初始化
     * @param requestMap
     * @return
     */

    @FormUrlEncoded
    @POST(Urls.STRATEGY_INIT)
    Observable<BaseApiModel<StrategyInitModel>> createOrder(@FieldMap Map<String,String> requestMap);
}
