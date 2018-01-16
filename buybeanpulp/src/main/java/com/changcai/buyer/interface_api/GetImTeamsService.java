package com.changcai.buyer.interface_api;

import com.changcai.buyer.bean.GetImTeamsBean;
import com.changcai.buyer.common.Urls;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by lufeisong on 2018/1/16.
 */

public interface GetImTeamsService {
    @FormUrlEncoded
    @POST(Urls.GET_IM_TEAMS)
    Observable<BaseApiModel<GetImTeamsBean>> getImTeams(@FieldMap Map<String,String> requestMap);
}
