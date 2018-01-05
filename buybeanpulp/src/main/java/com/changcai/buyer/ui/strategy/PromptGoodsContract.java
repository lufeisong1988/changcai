package com.changcai.buyer.ui.strategy;

import android.content.Context;
import android.view.View;

import com.changcai.buyer.BasePresenter;
import com.changcai.buyer.BaseView;
import com.changcai.buyer.bean.Articles;
import com.changcai.buyer.bean.SpotFolderListBean;

import java.util.List;

/**
 * Created by liuxingwei on 2017/8/1.
 */

public interface PromptGoodsContract {

    interface View extends BaseView<PromptGoodsContract.Presenter> {
        boolean isActive();

        void initIndicator(final List<SpotFolderListBean> folderListBeen);

        void addDataNormal(List<Articles> articlesList);

        void addDataWithOnRefresh(List<Articles> articlesList);

        void addDataLoadMore(List<Articles> articlesList);

        Context getViewContext();

        void setHeaderText(String headerText);

        void setCustomFontTextViewDeclareVisible();

        void showLoadProgress();

        void dismissLoadProgress();
    }

    interface Presenter extends BasePresenter {
        void getSpotStrategy(int pageIndex, String folderId);

        void getSpotStrategyOnRefresh(String folderId);
    }
}
