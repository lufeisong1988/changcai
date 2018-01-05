package com.changcai.buyer.ui.strategy;

import com.changcai.buyer.bean.Articles;

import java.util.List;

/**
 * Created by lufeisong on 2017/9/10.
 */

public interface PromptGoodsViewModel {
    void refreshSucceed(List<Articles> articlesList);

    void loadMoreSucceed(List<Articles> articlesList);

    void endData();

    void showLoading();

    void dismissLoading();

    void netErrorRefresh();
    void netErrorLoadMore();

    void setHeaderText(String headerText);
}
