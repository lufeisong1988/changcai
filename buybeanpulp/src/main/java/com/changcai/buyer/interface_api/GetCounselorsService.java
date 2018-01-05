package com.changcai.buyer.interface_api;

import com.changcai.buyer.bean.GetCounselorsModel;
import com.changcai.buyer.common.Urls;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by lufeisong on 2017/12/21.
 */

public interface GetCounselorsService {
    @FormUrlEncoded
    @POST(Urls.GET_COUNSELORS)
    Observable<BaseApiModel<GetCounselorsModel>> getCounselors(@FieldMap Map<String,String> requestMap);
}
