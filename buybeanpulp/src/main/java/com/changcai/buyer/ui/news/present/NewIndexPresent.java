package com.changcai.buyer.ui.news.present;

import android.content.Context;

import com.changcai.buyer.common.Constants;
import com.changcai.buyer.ui.news.NewIndexViewModel;
import com.changcai.buyer.ui.news.bean.AdBean;
import com.changcai.buyer.ui.news.bean.GetRecommendBean;
import com.changcai.buyer.ui.news.bean.NewsBean;
import com.changcai.buyer.ui.news.bean.NewsClassify;
import com.changcai.buyer.ui.news.bean.NewsEntity;
import com.changcai.buyer.ui.news.bean.NoticeListBean;
import com.changcai.buyer.ui.news.bean.PriceBean;
import com.changcai.buyer.ui.news.bean.TradeEventBean;
import com.changcai.buyer.ui.news.model.NewIndexModel;
import com.changcai.buyer.ui.news.model.NewIndexModelCallback;
import com.changcai.buyer.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lufeisong on 2017/11/17.
 */

public class NewIndexPresent implements NewIndexPresentInterface,NewIndexModelCallback {
    private Context context;
    private NewIndexViewModel view;
    private NewIndexModel model;
    private List<NewsClassify> newsClassifys = new ArrayList<>();

    private List<String> articleGroups = new ArrayList<>();
    private List<NewsEntity> articles = new ArrayList<>();

    private boolean refreshAble = false;
    public NewIndexPresent(Context context, NewIndexViewModel view, List<NewsClassify> newsClassifys) {
        this.context = context;
        this.view = view;
        this.newsClassifys = newsClassifys;
        model = new NewIndexModel(context,this);
    }

    @Override
    public void init() {
        refreshAble = false;
        getAdModel();
    }

    @Override
    public void refresh() {
        refreshAble = true;
        getAdModel();
    }

    @Override
    public void destory() {
        view = null;
    }

    //获取广告
    @Override
    public void getAdModel() {
        if(view == null){
            return;
        }
        if(!refreshAble){
            view.showLoading();
        }
        model.getAdModel();
    }
    @Override
    public void updateAd(AdBean adBean) {
        if(view == null){
            return;
        }
        if(adBean.getResponseState() == Constants.RESPONSE_SUCCEED){
            view.updateAdSucceed(adBean.getAdLists());
            getNoticeModel();
        }else if(adBean.getResponseState() == Constants.RESPONSE_FAIL){
            view.updateAdFail();
            if(!refreshAble){
                view.dismissLoading();
            }
        }else if(adBean.getResponseState() == Constants.RESPONSE_ERROR){
            view.updateAdError();
            if(!refreshAble){
                view.dismissLoading();
            }
        }
    }

    //获取公告
    @Override
    public void getNoticeModel() {
        if(view == null){
            return;
        }
        if(!refreshAble){
            view.showLoading();
        }
        model.getNoticeModel();
    }
    @Override
    public void updateNotice(NoticeListBean noticeListBean) {
        if(view == null){
            return;
        }

        if(noticeListBean.getResponseState() == Constants.RESPONSE_SUCCEED){
            view.updateNoticeSucceed(noticeListBean.getNoticeList());
            getTradeEventModel();
        }else if(noticeListBean.getResponseState() == Constants.RESPONSE_FAIL){
            view.updateNoticeFail();
            if(!refreshAble){
                view.dismissLoading();
            }
        }else if(noticeListBean.getResponseState() == Constants.RESPONSE_ERROR){
            view.updateNoticeError();
            if(!refreshAble){
                view.dismissLoading();
            }
        }
    }
    //获取行业日历
    @Override
    public void getTradeEventModel() {
        if(view == null){
            return;
        }
        if(!refreshAble){
            view.showLoading();
        }
        model.getTradeEventModel();
    }
    @Override
    public void updateTradeEvent(TradeEventBean tradeEventBean) {
        if(view == null){
            return;
        }

        if(tradeEventBean.getResponseState() == Constants.RESPONSE_SUCCEED){
            view.updateTradeEventSucceed(tradeEventBean.getDate());
            getRecommendModel();
        }else if(tradeEventBean.getResponseState() == Constants.RESPONSE_FAIL){
            view.updateTradeEventFail(tradeEventBean.getDate());
            if(!refreshAble){
                view.dismissLoading();
            }
        }else if(tradeEventBean.getResponseState() == Constants.RESPONSE_ERROR){
            view.updateTradeEventError();
            if(!refreshAble){
                view.dismissLoading();
            }
        }
    }

    //获取为你推荐
    @Override
    public void getRecommendModel() {
        if(view == null){
            return;
        }
        if(!refreshAble){
            view.showLoading();
        }
        model.getRecommendModel();
    }

    @Override
    public void updateRecommed(GetRecommendBean getRecommendBean) {
        if(view == null){
            return;
        }

        if(getRecommendBean.getResponseState() == Constants.RESPONSE_SUCCEED){
            view.updateRecommendSucceed(getRecommendBean.getRecommendationList());
            getPriceModel();
        }else if(getRecommendBean.getResponseState() == Constants.RESPONSE_FAIL){
            view.updateRecommendFail();
            if(!refreshAble){
                view.dismissLoading();
            }
        }else if(getRecommendBean.getResponseState() == Constants.RESPONSE_ERROR){
            view.updateRecommendError();
            if(!refreshAble){
                view.dismissLoading();
            }
        }
    }
    //获取最新报价
    @Override
    public void getPriceModel() {
        if(view == null){
            return;
        }
        if(!refreshAble){
            view.showLoading();
        }
        model.getPriceModel();
    }
    @Override
    public void updatePrice(PriceBean priceBean) {
        if(view == null){
            return;
        }

        if(priceBean.getResponseState() == Constants.RESPONSE_SUCCEED){
            view.updatePriceSucceed(priceBean.getResultBeen());
            getArticleListModel();
        }else if(priceBean.getResponseState() == Constants.RESPONSE_FAIL){
            view.updatePriceFail();
            if(!refreshAble){
                view.dismissLoading();
            }
        }else if(priceBean.getResponseState() == Constants.RESPONSE_ERROR){
            view.updatePriceError();
            if(!refreshAble){
                view.dismissLoading();
            }
        }
    }

    //获取新闻
    @Override
    public void getArticleListModel() {
        if(view == null){
            return;
        }
        if(!refreshAble){
            view.showLoading();
        }
        //从第二个数据源开始
        if(newsClassifys != null && newsClassifys.size() > 1){//剔除第一条数据
            //清空数据
            articles.clear();
            articleGroups.clear();
            getArticleList(newsClassifys.get(1).getFolderId(),1);
        }
    }
    @Override
    public void updateNews(NewsBean newsBean) {
        if(newsBean.getResponseState() == Constants.RESPONSE_SUCCEED){
            int index = newsBean.getIndex();
            int maxIndex = newsClassifys.size() - 1;
            LogUtil.d("TAG","index = " + index + "  maxIndex = " + maxIndex);
            if(newsBean.getNewsEntities() != null && newsBean.getNewsEntities().size() > 0){
                NewsEntity groupNewsEntity = new NewsEntity();
                groupNewsEntity.setFlag(newsClassifys.get(index).getName());
                articles.add(groupNewsEntity);
                articles.addAll(newsBean.getNewsEntities());
                articleGroups.add(newsClassifys.get(index).getName());
            }
            if(index < maxIndex){
                index++;
                getArticleList(newsClassifys.get(index).getFolderId(),index);
            }else if(index == maxIndex){
                if(view != null){
                    if(!refreshAble){
                        view.dismissLoading();
                    }
                    view.updateArticleListSucceed(articles,articleGroups);
                }
            }
        }else if(newsBean.getResponseState() == Constants.RESPONSE_FAIL){
            if(view != null){
                if(!refreshAble){
                    view.dismissLoading();
                }
                view.updateArticleListFail();
            }
        }else if(newsBean.getResponseState() == Constants.RESPONSE_ERROR){
            if(view != null){
                if(!refreshAble){
                    view.dismissLoading();
                }
                view.updateArticleListError();
            }
        }
    }
    private void getArticleList(String folderId,int index){
        model.getArticleListModel(folderId,index);
    }

}
