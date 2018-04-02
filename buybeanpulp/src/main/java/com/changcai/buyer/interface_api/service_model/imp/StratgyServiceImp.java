package com.changcai.buyer.interface_api.service_model.imp;

import com.changcai.buyer.bean.StrategyInitModel;
import com.changcai.buyer.bean.StrategyPaperModel;
import com.changcai.buyer.bean.StrategyTargetDataModel;
import com.changcai.buyer.bean.StrategyTargetModel;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.interface_api.ApiServiceGenerator;
import com.changcai.buyer.interface_api.BaseApiModel;
import com.changcai.buyer.interface_api.GetStrategyTargetDatasService;
import com.changcai.buyer.interface_api.GetStrategyTargetPaperService;
import com.changcai.buyer.interface_api.GetStrategyTargetService;
import com.changcai.buyer.interface_api.NetworkResultFunc1;
import com.changcai.buyer.interface_api.ThrowableFiltrateFunc;
import com.changcai.buyer.interface_api.service_model.StratgyServiceInterface;
import com.changcai.buyer.interface_api.service_model.base.ServiceRequestCallback;
import com.changcai.buyer.ui.strategy.StrategyInitService;
import com.changcai.buyer.util.UserDataUtil;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by lufeisong on 2018/3/19.
 */

public class StratgyServiceImp implements StratgyServiceInterface {
    /**
     * 初始化策略
     *
     * @param callback
     */
    @Override
    public void strategyInit(final ServiceRequestCallback<StrategyInitModel> callback) {
        StrategyInitService strategyInitService = ApiServiceGenerator.createService(StrategyInitService.class);
        strategyInitService
                .createOrder(new HashMap<String, String>())
                .map(new NetworkResultFunc1<StrategyInitModel>())
                .onErrorResumeNext(new ThrowableFiltrateFunc<StrategyInitModel>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<StrategyInitModel>() {
                    @Override
                    public void call(StrategyInitModel strategyInitModel) {
                        callback.onSucceed(strategyInitModel);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        callback.onError();
                    }
                });
    }

    /**
     * 获取策略指标
     *
     * @param date     选中的日期:初始化不传递，系统自动获取文章最新日期 yyyy-MM-dd
     * @param folderId 目录id(套利图景没有folderId，那么不传递此字段)
     * @param callbak
     */
    @Override
    public void getStrategyTarget(String date, String folderId, final ServiceRequestCallback<StrategyTargetModel> callbak) {
        Map<String, String> params = new HashMap<>(3);
        params.put("date", date);
        params.put("folderId", folderId);
        params.put("tokenId", UserDataUtil.getTokenId());
        GetStrategyTargetService service = ApiServiceGenerator.createService(GetStrategyTargetService.class);
        service.getStrateyTarget(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseApiModel<StrategyTargetModel>>() {
                    @Override
                    public void call(BaseApiModel<StrategyTargetModel> strategyTargetModelBaseApiModel) {
                        if (strategyTargetModelBaseApiModel.getErrorCode().equals(Constants.REQUEST_SUCCESS_S)) {
                            callbak.onSucceed(strategyTargetModelBaseApiModel.getResultObject());
                        } else {
                            callbak.onFail(strategyTargetModelBaseApiModel.getErrorDesc());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        callbak.onError();
                    }
                });
    }

    /**
     * 获取策略数据
     *
     * @param targetId     指标id
     * @param folderId
     * @param code         指标码
     * @param dataUrlParam 数据源部分url
     * @param date
     * @param callback
     */
    @Override
    public void getStrategyTargetDatas(String targetId, String folderId, String code, String dataUrlParam, String date, final ServiceRequestCallback<StrategyTargetDataModel> callback) {
        Map<String, String> params = new HashMap<>(6);
        params.put("targetId", targetId);
        params.put("folderId", folderId);
        params.put("code", code);
        params.put("dataUrlParam", dataUrlParam);
        params.put("date", date);
        params.put("tokenId", UserDataUtil.getTokenId());
        GetStrategyTargetDatasService service = ApiServiceGenerator.createService(GetStrategyTargetDatasService.class);
        service.getStrateyTargetData(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseApiModel<StrategyTargetDataModel>>() {
                    @Override
                    public void call(BaseApiModel<StrategyTargetDataModel> strategyTargetDataModelBaseApiModel) {
                        if (strategyTargetDataModelBaseApiModel.getErrorCode().equals(Constants.REQUEST_SUCCESS_S)) {
                            callback.onSucceed(strategyTargetDataModelBaseApiModel.getResultObject());
                        } else {
                            callback.onFail(strategyTargetDataModelBaseApiModel.getErrorDesc());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        callback.onError();
                    }
                });
    }

    /**
     * 获取策略文章
     *
     * @param folderId
     * @param data
     * @param callback
     */
    @Override
    public void getStrategyPaper(String folderId, String data, final ServiceRequestCallback<StrategyPaperModel> callback) {
        Map<String, String> params = new HashMap<>(3);
        params.put("folderId", folderId);
        params.put("date", data);
        params.put("tokenId", UserDataUtil.getTokenId());
        GetStrategyTargetPaperService service = ApiServiceGenerator.createService(GetStrategyTargetPaperService.class);
        service.getStrategyTargetContent(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseApiModel<StrategyPaperModel>>() {
                    @Override
                    public void call(BaseApiModel<StrategyPaperModel> strategyPaperModelBaseApiModel) {
                        if (strategyPaperModelBaseApiModel.getErrorCode().equals(Constants.REQUEST_SUCCESS_S)) {
                            callback.onSucceed(strategyPaperModelBaseApiModel.getResultObject());
                        } else {
                            callback.onFail(strategyPaperModelBaseApiModel.getErrorDesc());
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
