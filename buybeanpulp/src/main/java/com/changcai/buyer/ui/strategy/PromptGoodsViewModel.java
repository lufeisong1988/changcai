package com.changcai.buyer.ui.strategy;

/**
 * Created by lufeisong on 2017/9/10.
 */

public interface PromptGoodsViewModel {

    void showLoading();

    void dismissLoading();

    void unAuth();

    void initStrategyTarget();

    void initStrategyTargetFail();

    void initStrategyTargetData();

    void initStrategyTargetDataFail();

    void initStrategyTargetContent();

    void initStrategyTargetContentFail();


}
