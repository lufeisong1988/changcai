package com.changcai.buyer.ui.strategy.present;

import com.changcai.buyer.bean.PromptGoodsModel;
import com.changcai.buyer.bean.SpotFolderListBean;
import com.changcai.buyer.bean.StrategyInitModel;
import com.changcai.buyer.interface_api.ApiServiceGenerator;
import com.changcai.buyer.interface_api.NetworkResultFunc1;
import com.changcai.buyer.interface_api.ThrowableFiltrateFunc;
import com.changcai.buyer.ui.strategy.PromptGoodsViewModel;
import com.changcai.buyer.ui.strategy.SpotStrategy;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.util.RxUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lufeisong on 2017/9/10.
 */

public class PromptGoodsPresent implements PromptGoodsPresentInterface{
    private SpotStrategy strategyInitService;
    private StrategyInitModel strategyInitModel;
    private PromptGoodsViewModel promptGoodsViewModel;
    private int pageIndex = 0; //当前的页数
    private int folderPosition = 0;//标签栏位置

    Subscription subscription;

    private List<SpotFolderListBean> mSpotFolderListBeanList;//返回的数据
    public PromptGoodsPresent(StrategyInitModel strategyInitModel,PromptGoodsViewModel promptGoodsViewModel) {
        this.strategyInitModel = strategyInitModel;
        this.promptGoodsViewModel = promptGoodsViewModel;
        strategyInitService  = ApiServiceGenerator.createService(SpotStrategy.class);
    }

    @Override
    public void refreshSpotStrategy(int folderPosition) {
        pageIndex = 0;
        this.folderPosition = folderPosition;
        if(promptGoodsViewModel != null){
            promptGoodsViewModel.showLoading();
        }
        getSpotStrategy();

    }

    @Override
    public void loadMoreSpotStrategy() {
        pageIndex++;
        if(promptGoodsViewModel != null){
            promptGoodsViewModel.showLoading();
        }
        getSpotStrategy();
    }

    @Override
    public void getSpotStrategy() {
        Map<String, String> params = new HashMap<>(2);
        params.put("currentPage", String.valueOf(pageIndex));
        params.put("folderId", strategyInitModel.getSpotFolderList().get(folderPosition).getFolderId());
        subscription = strategyInitService
                .getArbitrageStrategy(params)
                .map(new NetworkResultFunc1<PromptGoodsModel>())
                .onErrorResumeNext(new ThrowableFiltrateFunc<PromptGoodsModel>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PromptGoodsModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.d("TAG","e = " +e.toString());
                        if(pageIndex == 0){
                            if(promptGoodsViewModel != null){
                                promptGoodsViewModel.dismissLoading();
                                promptGoodsViewModel.netErrorRefresh();
                            }
                        }else{//加载更多失败，需要退一位
                            pageIndex--;
                            if(promptGoodsViewModel != null){
                                promptGoodsViewModel.dismissLoading();
                                promptGoodsViewModel.netErrorLoadMore();
                            }
                        }

                    }

                    @Override
                    public void onNext(PromptGoodsModel strategyInitModel) {
                        if(promptGoodsViewModel != null){
                            promptGoodsViewModel.dismissLoading();
                            promptGoodsViewModel.setHeaderText(strategyInitModel.getLastUpdateTime());
                        }
                        if(pageIndex == 0){//初始化数据
                            if(promptGoodsViewModel != null){
                                promptGoodsViewModel.refreshSucceed(strategyInitModel.getArticles());
                            }
                        }else{//加载更多数据
                            if(promptGoodsViewModel != null){
                                promptGoodsViewModel.loadMoreSucceed(strategyInitModel.getArticles());
                            }
                        }
                        if(strategyInitModel.getArticles().size() < 10){
                            if(promptGoodsViewModel != null){
                                promptGoodsViewModel.endData();
                            }
                        }
                    }
                });
    }

    @Override
    public void onDestory() {
        RxUtil.remove(subscription);
        promptGoodsViewModel = null;

    }

}
interface PromptGoodsPresentInterface{
    void refreshSpotStrategy(int folderPosition);
    void loadMoreSpotStrategy();
    void getSpotStrategy();
    void onDestory();
}
