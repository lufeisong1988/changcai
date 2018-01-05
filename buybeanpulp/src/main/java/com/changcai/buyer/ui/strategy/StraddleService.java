package com.changcai.buyer.ui.strategy;

import com.changcai.buyer.bean.StraddleModel;
import com.changcai.buyer.bean.StrategyInitModel;
import com.changcai.buyer.common.Urls;
import com.changcai.buyer.interface_api.BaseApiModel;

import java.util.List;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by liuxingwei on 2017/8/1.
 */

public interface StraddleService {

    /**
     * 策略初始化
     *
     * @param requestMap
     * @return
     */

    @FormUrlEncoded
    @POST(Urls.GETARBITRAGESTRATEGY)
    Observable<BaseApiModel<List<StraddleModel>>> getArbitrageStrategy(@FieldMap Map<String, String> requestMap);
}
