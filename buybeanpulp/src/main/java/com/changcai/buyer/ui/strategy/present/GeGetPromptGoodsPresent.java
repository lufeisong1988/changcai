package com.changcai.buyer.ui.strategy.present;

import com.changcai.buyer.bean.StrategyPaperModel;
import com.changcai.buyer.bean.StrategyTargetDataModel;
import com.changcai.buyer.bean.StrategyTargetModel;
import com.changcai.buyer.ui.strategy.PromptGoodsViewModel;
import com.changcai.buyer.ui.strategy.model.PromptGoodsModel;

/**
 * Created by lufeisong on 2017/9/10.
 * 头寸管理
 * 1.现货策略
 */

public class GeGetPromptGoodsPresent implements GetPromptGoodsPresentInterface,GetPromptGoodsPresentCallback {
    private PromptGoodsModel mode;
    private PromptGoodsViewModel view;

    boolean auth;//是否认证成功
    boolean force;//是否强制

    public GeGetPromptGoodsPresent(PromptGoodsViewModel view) {
        auth = false;
        force = false;
        this.view = view;
        mode = new PromptGoodsModel(this);
    }


    @Override
    public void setAuth(boolean auth, boolean force) {
        this.auth = auth;
        this.force = force;
    }

    @Override
    public void getStrategyTarget(String date, String folderId) {
        checkAuth();
        showLoading();
        mode.getStrategyTarget(date,folderId);
    }

    @Override
    public void getStrategyTargetData(String targetId, String folderId, String code, String dataUrlParam, String date) {
        checkAuth();
        showLoading();
        mode.getStrategyTargetData(targetId,folderId,code,dataUrlParam,date);
    }

    @Override
    public void getStrategyTargetContent(String folderId, String date) {
        checkAuth();
        showLoading();
        mode.getStrategyTargetContent(folderId,date);
    }

    @Override
    public void onDestory() {
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

    /**
     * 检测是否认证通过
     */
    void checkAuth(){
        if(force && !auth){
            if(view != null){
                view.unAuth();
            }
            return;
        }
    }
    //请求数据model的回调
    @Override
    public void onGetStrategyTargetNext(StrategyTargetModel strategyTargetModel) {
        dismissLoading();
    }

    @Override
    public void onGetStrategyTargetError(Throwable e) {
        dismissLoading();
    }

    @Override
    public void onGetStrategyTargetDataNext(StrategyTargetDataModel strategyTargetDataModel) {
        dismissLoading();
    }

    @Override
    public void onGetStrategyTargetDataError(Throwable e) {
        dismissLoading();
    }

    @Override
    public void onGetStrategyTargetContentNext(StrategyPaperModel strategyTargetContentModel) {
        dismissLoading();
    }

    @Override
    public void onGetStrategyTargetContentError(Throwable e) {
        dismissLoading();
    }
}
interface GetPromptGoodsPresentInterface {
    void setAuth(boolean auth,boolean force);

    void getStrategyTarget(String date,String folderId);

    void getStrategyTargetData(String targetId,String folderId,String code,String dataUrlParam,String date);

    void getStrategyTargetContent(String folderId,String date);

    void onDestory();
}
