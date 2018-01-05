package com.changcai.buyer.ui.strategy.api;

import com.changcai.buyer.common.Urls;
import com.changcai.buyer.interface_api.BaseApiModel;
import com.changcai.buyer.ui.strategy.bean.IsExistBean;
import com.changcai.buyer.ui.strategy.bean.SalesAmountBean;
import com.changcai.buyer.ui.strategy.bean.SalesAmountItemBean;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by lufeisong on 2017/9/2.
 */

public interface StrategyService {
    @FormUrlEncoded
    @POST(Urls.GET_SALES_AMOUNT)
    Observable<BaseApiModel<SalesAmountBean>> getSalesAmount(@FieldMap Map<String,String> requestMap);

    @FormUrlEncoded
    @POST(Urls.GET_SALES_AMOUNT_ITEM)
    Observable<BaseApiModel<SalesAmountItemBean>> getSalesAmountItem(@FieldMap Map<String,String> requestMap);


    @FormUrlEncoded
    @POST(Urls.GET_IS_EXIST)
    Observable<BaseApiModel<IsExistBean>> getIsExist(@FieldMap Map<String,String> requestMap);

}
