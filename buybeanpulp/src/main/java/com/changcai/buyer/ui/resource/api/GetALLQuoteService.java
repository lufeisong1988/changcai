package com.changcai.buyer.ui.resource.api;

import com.changcai.buyer.common.Urls;
import com.changcai.buyer.interface_api.BaseApiModel;
import com.changcai.buyer.ui.resource.bean.QuoteBean;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by lufeisong on 2017/8/31.
 */

public interface GetALLQuoteService {
    @FormUrlEncoded
    @POST(Urls.GET_ALLQUOTE)
    Observable<BaseApiModel<QuoteBean>> getDomainsAndType(@FieldMap Map<String,String> requestMap);
}
