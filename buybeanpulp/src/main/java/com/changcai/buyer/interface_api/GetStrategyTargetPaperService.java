package com.changcai.buyer.interface_api;

import com.changcai.buyer.bean.StrategyPaperModel;
import com.changcai.buyer.common.Urls;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by lufeisong on 2018/3/7.
 */

public interface GetStrategyTargetPaperService {
    @FormUrlEncoded
    @POST(Urls.GETSTRATEGYTARGETCONTENT)
    Observable<BaseApiModel<StrategyPaperModel>> getStrategyTargetContent(@FieldMap Map<String,String> requestMap);
}
