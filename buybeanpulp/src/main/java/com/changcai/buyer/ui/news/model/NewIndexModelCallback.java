package com.changcai.buyer.ui.news.model;

import com.changcai.buyer.ui.news.bean.AdBean;
import com.changcai.buyer.ui.news.bean.GetRecommendBean;
import com.changcai.buyer.ui.news.bean.NewsBean;
import com.changcai.buyer.ui.news.bean.NoticeListBean;
import com.changcai.buyer.ui.news.bean.PriceBean;
import com.changcai.buyer.ui.news.bean.TradeEventBean;

/**
 * Created by lufeisong on 2017/11/17.
 */

public interface NewIndexModelCallback {
    void updateAd(AdBean adBean);
    void updateNotice(NoticeListBean noticeListBean);
    void updateTradeEvent(TradeEventBean tradeEventBean);
    void updateRecommed(GetRecommendBean getRecommendBean);
    void updatePrice(PriceBean priceBean);
    void updateNews(NewsBean newsBean);
}
