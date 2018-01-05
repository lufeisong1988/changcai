package com.changcai.buyer.interface_api;

import com.changcai.buyer.bean.FiltrateDetailModel;
import com.changcai.buyer.bean.FiltrateModel;
import com.changcai.buyer.common.Urls;
import com.changcai.buyer.ui.order.bean.PreviewContract;

import java.util.List;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by liuxingwei on 2017/2/13.
 */

public interface GetMoreFiltrateService {

    @FormUrlEncoded
    @POST(Urls.MORE_FILTRATE)
    Observable<BaseApiModel<List<FiltrateDetailModel>>> moreFiltrate(@FieldMap Map<String,String> requestMap);
}
