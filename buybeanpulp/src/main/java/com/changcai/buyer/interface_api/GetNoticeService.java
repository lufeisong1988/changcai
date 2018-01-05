package com.changcai.buyer.interface_api;

import com.changcai.buyer.bean.NoticeBean;
import com.changcai.buyer.common.Urls;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by lufeisong on 2017/11/17.
 */

public interface GetNoticeService {
    @FormUrlEncoded
    @POST(Urls.GET_NOTICE)
    Observable<BaseApiModel<NoticeBean>> getOrderInfo(@FieldMap Map<String,String> requestMap);
}
