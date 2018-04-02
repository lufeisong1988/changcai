package com.changcai.buyer.ui.strategy;

import com.changcai.buyer.BasePresenter;
import com.changcai.buyer.BaseView;

/**
 * Created by liuxingwei on 2017/8/1.
 */

public interface PromptGoodsContract {

    interface View extends BaseView<PromptGoodsContract.Presenter> {

        void showLoading();

        void dismissLoading();


        void initStrategyTarget(String[] targetId,String[] dataUrlParam,String[] name,String[] code);

        void initStrategyTargetFail();

        void initStrategyTargetEmpty();

        void initStrategyTargetData(String[] date,String[] price,String[] point,int minY,int maxY,String currentDate);

        void initStrategyTargetDataFail();

        void initStrategyTargetDataEmpty();



        void initPaper(String content);

        void initPaperEmpty();

        void initPaperFail();
    }

    interface Presenter extends BasePresenter {
        void getStrategyTarget(String date,String folderId);

        void getStrategyTargetData(String targetId,String folderId,String code,String dataUrlParam,String data);

        void getSpotPaper(String folderId, String data);
    }
}
