package com.changcai.buyer.ui.news;

import com.changcai.buyer.bean.NoticeBean;
import com.changcai.buyer.bean.GetQuotePriceBean;
import com.changcai.buyer.bean.RecommendListBean;
import com.changcai.buyer.ui.news.bean.NewsEntity;
import com.changcai.buyer.ui.news.bean.RecommendNewsEntity;

import java.util.Date;
import java.util.List;

/**
 * Created by lufeisong on 2017/11/17.
 */

public interface NewIndexViewModel {
    void updateAdSucceed(List<RecommendNewsEntity> adLists);
    void updateAdFail();
    void updateAdError();

    void updateNoticeSucceed(List<NoticeBean.NoticeListBean> noticeList);
    void updateNoticeFail();
    void updateNoticeError();

    void updateTradeEventSucceed(Date date);
    void updateTradeEventFail(Date date);
    void updateTradeEventError();

    void updateRecommendSucceed(List<RecommendListBean.RecommendationListBean> recommendationList);
    void updateRecommendFail();
    void updateRecommendError();

    void updatePriceSucceed(List<GetQuotePriceBean.QuotePriceBean.ResultBean> resultBeen);
    void updatePriceFail();
    void updatePriceError();

    void updateArticleListSucceed(List<NewsEntity> articles,List<String> articleGroups);
    void updateArticleListFail();
    void updateArticleListError();

    void showLoading();
    void dismissLoading();

}
