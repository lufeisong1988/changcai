package com.changcai.buyer.interface_api;

import com.changcai.buyer.common.Urls;
import com.changcai.buyer.ui.order.bean.PreviewContract;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by liuxingwei on 2017/1/13.
 */

public interface PreviewContractService {

    @FormUrlEncoded
    @POST(Urls.PREVIEW_SIGN_CONTRACT)
    Observable<BaseApiModel<PreviewContract>> signContract(@FieldMap Map<String,String> requestMap);
}
