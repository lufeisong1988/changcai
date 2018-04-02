package com.changcai.buyer.ui.strategy.model;

import com.changcai.buyer.bean.StrategyPaperModel;
import com.changcai.buyer.bean.StrategyTargetDataModel;
import com.changcai.buyer.bean.StrategyTargetModel;
import com.changcai.buyer.interface_api.ApiServiceGenerator;
import com.changcai.buyer.interface_api.BaseApiModel;
import com.changcai.buyer.interface_api.GetStrategyTargetPaperService;
import com.changcai.buyer.interface_api.GetStrategyTargetDatasService;
import com.changcai.buyer.interface_api.GetStrategyTargetService;
import com.changcai.buyer.ui.strategy.present.GetPromptGoodsPresentCallback;

import java.util.HashMap;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by lufeisong on 2018/3/7.
 */

public class PromptGoodsModel implements PromptGoodsModelInterface{
    private GetPromptGoodsPresentCallback callback;

    public PromptGoodsModel(GetPromptGoodsPresentCallback callback) {
        this.callback = callback;
    }

    @Override
    public void getStrategyTarget(String date, String folderId) {
        Map<String, String> params = new HashMap<>(2);
        params.put("date", date);
        params.put("folderId", folderId);
        GetStrategyTargetService service = ApiServiceGenerator.createService(GetStrategyTargetService.class);
        service.getStrateyTarget(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseApiModel<StrategyTargetModel>>() {
                    @Override
                    public void call(BaseApiModel<StrategyTargetModel> strategyTargetModelBaseApiModel) {
                        callback.onGetStrategyTargetNext(strategyTargetModelBaseApiModel.getResultObject());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        callback.onGetStrategyTargetError(throwable);
                    }
                });
    }

    @Override
    public void getStrategyTargetData(String targetId, String folderId, String code, String dataUrlParam, String date) {
        Map<String, String> params = new HashMap<>(2);
        params.put("targetId", targetId);
        params.put("folderId", folderId);
        params.put("code", code);
        params.put("dataUrlParam", dataUrlParam);
        params.put("date", date);
        GetStrategyTargetDatasService service = ApiServiceGenerator.createService(GetStrategyTargetDatasService.class);
        service.getStrateyTargetData(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseApiModel<StrategyTargetDataModel>>() {
                    @Override
                    public void call(BaseApiModel<StrategyTargetDataModel> strategyTargetDataModelBaseApiModel) {
                            callback.onGetStrategyTargetDataNext(strategyTargetDataModelBaseApiModel.getResultObject());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        callback.onGetStrategyTargetDataError(throwable);
                    }
                });
    }

    @Override
    public void getStrategyTargetContent(String folderId, String date) {
        Map<String, String> params = new HashMap<>(2);
        params.put("folderId", folderId);
        params.put("date", date);
        GetStrategyTargetPaperService service = ApiServiceGenerator.createService(GetStrategyTargetPaperService.class);
        service.getStrategyTargetContent(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseApiModel<StrategyPaperModel>>() {
                    @Override
                    public void call(BaseApiModel<StrategyPaperModel> strategyTargetContentModelBaseApiModel) {
                            callback.onGetStrategyTargetContentNext(strategyTargetContentModelBaseApiModel.getResultObject());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        callback.onGetStrategyTargetContentError(throwable);
                    }
                });
    }
}
interface PromptGoodsModelInterface{
    void getStrategyTarget(String date,String folderId);

    void getStrategyTargetData(String targetId,String folderId,String code,String dataUrlParam,String date);

    void getStrategyTargetContent(String folderId,String date);
}
