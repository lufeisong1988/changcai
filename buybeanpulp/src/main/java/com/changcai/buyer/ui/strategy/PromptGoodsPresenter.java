package com.changcai.buyer.ui.strategy;

import android.text.TextUtils;

import com.changcai.buyer.bean.StrategyPaperModel;
import com.changcai.buyer.bean.StrategyTargetDataModel;
import com.changcai.buyer.bean.StrategyTargetModel;
import com.changcai.buyer.interface_api.service_model.StratgyServiceInterface;
import com.changcai.buyer.interface_api.service_model.base.ServiceRequestCallback;
import com.changcai.buyer.interface_api.service_model.imp.StratgyServiceImp;

import java.util.List;

/**
 * Created by liuxingwei on 2017/8/1.
 */

public class PromptGoodsPresenter implements PromptGoodsContract.Presenter {

    PromptGoodsContract.View  view;

    StratgyServiceInterface service;


    public PromptGoodsPresenter(PromptGoodsContract.View view) {
        this.view = view;
        view.setPresenter(this);
        service = new StratgyServiceImp();
    }

    @Override
    public void start() {

    }

    @Override
    public void detach() {
        view = null;
    }


    void showLoading(){
        if(view != null){
            view.showLoading();
        }
    }
    void dismissLoading(){
        if(view != null){
            view.dismissLoading();
        }
    }

    @Override
    public void getStrategyTarget(String date, String folderId) {
        final String mFolerId = folderId;
        showLoading();
        service.getStrategyTarget(date, mFolerId, new ServiceRequestCallback<StrategyTargetModel>() {
            @Override
            public void onSucceed(StrategyTargetModel strategyTargetModel) {
                dismissLoading();
                if(strategyTargetModel != null && strategyTargetModel.getDate() != null && strategyTargetModel.getTargets() != null && strategyTargetModel.getTargets().size() > 0){
                    List<StrategyTargetModel.TargetsBean> targets = strategyTargetModel.getTargets();
                    String[] targetId = new String[targets.size()];
                    String[] dataUrlParam = new String[targets.size()];
                    String[] name = new String[targets.size()];
                    String[] code = new String[targets.size()];
                    for(int i = 0;i < targets.size();i++){
                        targetId[i] = targets.get(i).getTargetId();
                        dataUrlParam[i] = targets.get(i).getDataUrlParam();
                        name[i] = targets.get(i).getName();
                        code[i] = targets.get(i).getCode();
                    }
                    if(view != null){
                        view.initStrategyTarget(targetId,dataUrlParam,name,code);
                    }
                    StrategyTargetModel.TargetsBean targetsBean = strategyTargetModel.getTargets().get(0);
                    getStrategyTargetData(targetsBean.getTargetId(),mFolerId,targetsBean.getCode(),targetsBean.getDataUrlParam(),strategyTargetModel.getDate());

                }else{
                    if(view != null){
                        view.initStrategyTargetEmpty();
                    }
                }
            }

            @Override
            public void onFail(String error) {
                dismissLoading();
                if(view != null){
                    view.initStrategyTargetFail();
                }
            }

            @Override
            public void onError() {
                dismissLoading();
                if(view != null){
                    view.initStrategyTargetFail();
                }
            }
        });
    }

    @Override
    public void getStrategyTargetData(String targetId, String folderId, String code, String dataUrlParam, String date) {
        final String tmpDate = date;
        showLoading();
        service.getStrategyTargetDatas(targetId, folderId, code, dataUrlParam, date, new ServiceRequestCallback<StrategyTargetDataModel>() {
            @Override
            public void onSucceed(StrategyTargetDataModel strategyTargetDataModel) {
                dismissLoading();
                if(view != null){
                    if(strategyTargetDataModel != null && strategyTargetDataModel.getView() != null){
                        List<StrategyTargetDataModel.ViewBean> items = strategyTargetDataModel.getView();
                        String[] date = new String[items.size()];
                        String[] price = new String[items.size()];
                        String[] point = new String[items.size()];
                        float minY = 0;
                        float maxY = 0;
                        for(int i = 0; i < items.size();i++){
                            StrategyTargetDataModel.ViewBean item = items.get(i);
                            date[i] = item.getDate();
                            price[i] = item.getPrice();
                            point[i] = item.getPoint();
                            float priceF = Float.parseFloat(item.getPrice());
                            if(priceF < minY){
                                minY = priceF;
                            }
                            if(priceF > maxY){
                                maxY = priceF;
                            }
                        }
                        view.initStrategyTargetData(date,price,point,Math.round(minY),Math.round(maxY),tmpDate);
                    }else{
                        view.initStrategyTargetDataEmpty();
                    }

                }
            }

            @Override
            public void onFail(String error) {
                dismissLoading();
                if(view != null){
                    view.initStrategyTargetDataFail();
                }
            }

            @Override
            public void onError() {
                dismissLoading();
                if(view != null){
                    view.initStrategyTargetDataFail();
                }
            }
        });
    }

    @Override
    public void getSpotPaper(String folderId, String data) {
        showLoading();
        service.getStrategyPaper(folderId, data, new ServiceRequestCallback<StrategyPaperModel>() {
            @Override
            public void onSucceed(StrategyPaperModel strategyPaperModel) {
                dismissLoading();
                if(view != null){
                    if(strategyPaperModel.getArticle() != null && !TextUtils.isEmpty(strategyPaperModel.getArticle())){
                        view.initPaper(strategyPaperModel.getArticle());
                    }else{
                        view.initPaperEmpty();
                    }
                }
            }

            @Override
            public void onFail(String error) {
                dismissLoading();
                if(view != null){
                    view.initPaperFail();
                }
            }

            @Override
            public void onError() {
                dismissLoading();
                if(view != null){
                    view.initPaperFail();
                }
            }
        });
    }
}
