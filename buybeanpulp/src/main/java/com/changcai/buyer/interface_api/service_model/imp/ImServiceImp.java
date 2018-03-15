package com.changcai.buyer.interface_api.service_model.imp;

import com.changcai.buyer.bean.GetCounselorsModel;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.interface_api.ApiServiceGenerator;
import com.changcai.buyer.interface_api.BaseApiModel;
import com.changcai.buyer.interface_api.GetCounselorsService;
import com.changcai.buyer.interface_api.service_model.ImServiceInterface;
import com.changcai.buyer.interface_api.service_model.base.ServiceRequestCallback;
import com.changcai.buyer.util.SPUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by lufeisong on 2018/3/12.
 */

public class ImServiceImp implements ImServiceInterface{
    /**
     * 获取顾问列表
     */
    @Override
    public void getCounselorsList(final ServiceRequestCallback callback) {
        Map<String, String> map = SPUtil.getObjectFromShare(Constants.REQUEST_BASE_PARAMETERS);
        map.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        GetCounselorsService getCounselorsService = ApiServiceGenerator.createService(GetCounselorsService.class);
        getCounselorsService.getCounselors(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseApiModel<GetCounselorsModel>>() {
                    @Override
                    public void call(BaseApiModel<GetCounselorsModel> getCounselorsModelBaseApiModel) {
                        if(getCounselorsModelBaseApiModel.getErrorCode().equals("0")){
                            List<GetCounselorsModel.InfoBean> info = getCounselorsModelBaseApiModel.getResultObject().getInfo();
                            List<GetCounselorsModel.InfoBean> onlineInfoBean = new ArrayList<GetCounselorsModel.InfoBean>();
                            for(int i = 0;i < info.size();i++){
                                if(info.get(i).getServiceStatus().equals("NORMAL")){
                                    onlineInfoBean.add(info.get(i));
                                }
                            }
                            callback.onSucceed(onlineInfoBean);
                        }else{
                            callback.onFail(getCounselorsModelBaseApiModel.getErrorDesc());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        callback.onError();
                    }
                });
    }
}
