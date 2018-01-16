package com.changcai.buyer.im.main.model.imp;

import com.changcai.buyer.bean.GetCounselorsModel;
import com.changcai.buyer.bean.GetImTeamsBean;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.im.main.model.NotifactionListModelInterface;
import com.changcai.buyer.interface_api.ApiServiceGenerator;
import com.changcai.buyer.interface_api.BaseApiModel;
import com.changcai.buyer.interface_api.GetCounselorsService;
import com.changcai.buyer.interface_api.GetImTeamsService;
import com.changcai.buyer.util.SPUtil;
import com.netease.nim.uikit.common.util.log.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by lufeisong on 2017/12/22.
 */

public class NotifactionListModelImp implements NotifactionListModelInterface {
    private NotifactionListModelCallback callback;

    public NotifactionListModelImp(NotifactionListModelCallback callback) {
        this.callback = callback;
    }

    @Override
    public void getCounselorsModel() {
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
                            callback.getCounselorsModelSucceed(onlineInfoBean);
                        }else{
                            callback.getCounselorsModelFail(getCounselorsModelBaseApiModel.getErrorDesc());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtil.d("getCounselors","error:" + throwable.toString());
                            callback.getCounselorsModelError();
                    }
                });

    }

    @Override
    public void getImTeams() {
        Map<String, String> map = SPUtil.getObjectFromShare(Constants.REQUEST_BASE_PARAMETERS);
        map.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        GetImTeamsService service = ApiServiceGenerator.createService(GetImTeamsService.class);
        service.getImTeams(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseApiModel<GetImTeamsBean>>() {
                    @Override
                    public void call(BaseApiModel<GetImTeamsBean> getImTeamsBeanBaseApiModel) {
                        if(getImTeamsBeanBaseApiModel.getErrorCode().equals("0")){
                            callback.getImTeamsSucceed(getImTeamsBeanBaseApiModel.getResultObject());
                        }else{
                            callback.getImTeamsFail(getImTeamsBeanBaseApiModel.getErrorDesc());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        callback.getImTeamsError();
                    }
                });
    }

    public interface NotifactionListModelCallback{
        void getCounselorsModelSucceed(List<GetCounselorsModel.InfoBean> info);
        void getCounselorsModelFail(String failStr);
        void getCounselorsModelError();

        void getImTeamsSucceed(GetImTeamsBean getImTeamsBeen);
        void getImTeamsFail(String failStr);
        void getImTeamsError();
    }
}

