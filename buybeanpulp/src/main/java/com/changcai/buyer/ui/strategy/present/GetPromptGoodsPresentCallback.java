package com.changcai.buyer.ui.strategy.present;

import com.changcai.buyer.bean.StrategyPaperModel;
import com.changcai.buyer.bean.StrategyTargetDataModel;
import com.changcai.buyer.bean.StrategyTargetModel;

/**
 * Created by lufeisong on 2017/9/10.
 */

public interface GetPromptGoodsPresentCallback {

    void onGetStrategyTargetNext(StrategyTargetModel strategyTargetModel);

    void onGetStrategyTargetError(Throwable e);

    void onGetStrategyTargetDataNext(StrategyTargetDataModel strategyTargetDataModel);

    void onGetStrategyTargetDataError(Throwable e);

    void onGetStrategyTargetContentNext(StrategyPaperModel strategyTargetContentModel);

    void onGetStrategyTargetContentError(Throwable e);

}
