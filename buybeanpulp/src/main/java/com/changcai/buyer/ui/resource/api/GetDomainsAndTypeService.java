package com.changcai.buyer.ui.resource.api;

import com.changcai.buyer.common.Urls;
import com.changcai.buyer.interface_api.BaseApiModel;
import com.changcai.buyer.ui.resource.bean.DomainsAndTypesBean;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by lufeisong on 2017/8/30.
 * 获取定价方式与区域
 */

public interface GetDomainsAndTypeService {
    /**
     * {"errorCode":"0","errorDesc":"","resultObject":{"domains":[{"name":"华南","id":2},{"name":"华东","id":1},{"name":"华中","id":3},{"name":"华北","id":5},{"name":"东北","id":6},{"name":"西南","id":4},{"name":"川渝","id":7}],"productType":[{"ALL":"全部","SPOT":"一口价","BASIS":"基差"}]}}
     * @param requestMap
     * @return
     */
    @FormUrlEncoded
    @POST(Urls.GET_DOMAINSANDTYPE)
    Observable<BaseApiModel<DomainsAndTypesBean>> getDomainsAndType(@FieldMap Map<String,String> requestMap);
}
