package com.changcai.buyer.ui.news.model;

import android.content.Context;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.NoticeBean;
import com.changcai.buyer.bean.GetQuotePriceBean;
import com.changcai.buyer.bean.RecommendListBean;
import com.changcai.buyer.bean.TradeTime;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.common.Urls;
import com.changcai.buyer.http.HttpListener;
import com.changcai.buyer.http.VolleyUtil;
import com.changcai.buyer.interface_api.ApiCodeErrorException;
import com.changcai.buyer.interface_api.ApiServiceGenerator;
import com.changcai.buyer.interface_api.GetNoticeService;
import com.changcai.buyer.interface_api.GetQuotePriceForAppService;
import com.changcai.buyer.interface_api.GetRecommendService;
import com.changcai.buyer.interface_api.NetworkResultFunc1;
import com.changcai.buyer.interface_api.TradeTimeService;
import com.changcai.buyer.ui.news.bean.AdBean;
import com.changcai.buyer.ui.news.bean.GetRecommendBean;
import com.changcai.buyer.ui.news.bean.NewsBean;
import com.changcai.buyer.ui.news.bean.NewsEntity;
import com.changcai.buyer.ui.news.bean.NoticeListBean;
import com.changcai.buyer.ui.news.bean.PriceBean;
import com.changcai.buyer.ui.news.bean.RecommendNewsEntity;
import com.changcai.buyer.ui.news.bean.TradeEventBean;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.ServerErrorCodeDispatch;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by lufeisong on 2017/11/17.
 */

public class NewIndexModel implements NewIndexModelInterface {
    private Context context;
    private NewIndexModelCallback callback;

    private Date date ;



    public NewIndexModel(Context context,NewIndexModelCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    public void getAdModel() {
        final AdBean adBean = new AdBean();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("tabId", "");
        VolleyUtil.getInstance().httpPost(context, Urls.GET_INFO_RECOMMEND, params, new HttpListener() {
            @Override
            public void onResponseJson(JsonObject response) {
                super.onResponseJson(response);
                String errorCode = response.get(Constants.RESPONSE_CODE).getAsString();
                if (errorCode.equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {
                    Gson gson = new Gson();
                    List<RecommendNewsEntity> recommendNewsList = gson.fromJson(response.get(Constants.RESPONSE_CONTENT),
                            new TypeToken<List<RecommendNewsEntity>>() {
                            }.getType());
                    adBean.setAdLists(recommendNewsList);
                    adBean.setResponseState(Constants.RESPONSE_SUCCEED);
                } else {
                    ServerErrorCodeDispatch.getInstance().showNetErrorDialog(context, context.getString(R.string.net_work_exception), errorCode);
                    adBean.setResponseState(Constants.RESPONSE_FAIL);
                }
                callback.updateAd(adBean);
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                ServerErrorCodeDispatch.getInstance().showNetErrorDialog(context, context.getString(R.string.network_unavailable));
                adBean.setResponseState(Constants.RESPONSE_ERROR);
                callback.updateAd(adBean);
            }
        }, false);
    }

    @Override
    public void getNoticeModel() {
        final NoticeListBean noticeListBean = new NoticeListBean();
        GetNoticeService getNotice = ApiServiceGenerator.createService(GetNoticeService.class);
        Map<String, String> baseMap = new HashMap<>();
        getNotice.getOrderInfo(baseMap)
                .subscribeOn(Schedulers.io())
                .map(new NetworkResultFunc1<NoticeBean>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<NoticeBean>() {
                    @Override
                    public void call(NoticeBean noticeBean) {
                        noticeListBean.setResponseState(Constants.RESPONSE_SUCCEED);
                        noticeListBean.setNoticeList(noticeBean.getNoticeList());
                        callback.updateNotice(noticeListBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (throwable instanceof ApiCodeErrorException) {
                            noticeListBean.setResponseState(Constants.RESPONSE_ERROR);
                        }else{
                            noticeListBean.setResponseState(Constants.RESPONSE_FAIL);
                        }
                        callback.updateNotice(noticeListBean);
                    }
                });
    }

    @Override
    public void getTradeEventModel() {
        final TradeEventBean tradeEventBean = new TradeEventBean();
        TradeTimeService tradeTimeService = ApiServiceGenerator.createService(TradeTimeService.class);
        Map<String, String> baseMap = new HashMap<>();
        tradeTimeService
                .getTradeTime(baseMap)
                .subscribeOn(Schedulers.io())
                .map(new NetworkResultFunc1<TradeTime>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<TradeTime>() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void call(TradeTime tradeTime) {
                        date = new Date(Long.parseLong(tradeTime.getCurrentTime()));
                        tradeEventBean.setDate(date);
                        tradeEventBean.setResponseState(Constants.RESPONSE_SUCCEED);
                        callback.updateTradeEvent(tradeEventBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        date = new Date();
                        tradeEventBean.setDate(date);
                        if (throwable instanceof ApiCodeErrorException) {
                            tradeEventBean.setResponseState(Constants.RESPONSE_ERROR);
                        }else{
                            tradeEventBean.setResponseState(Constants.RESPONSE_FAIL);
                        }
                        callback.updateTradeEvent(tradeEventBean);
                    }
                });
    }

    @Override
    public void getRecommendModel() {
        final GetRecommendBean getRecommendBean = new GetRecommendBean();
        GetRecommendService getRecommendService = ApiServiceGenerator.createService(GetRecommendService.class);
        Map<String, String> baseMap = new HashMap<>();
        getRecommendService.getOrderInfo(baseMap)
                .subscribeOn(Schedulers.io())
                .map(new NetworkResultFunc1<RecommendListBean>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RecommendListBean>() {
                    @Override
                    public void call(RecommendListBean recommendListBean) {
                        getRecommendBean.setResponseState(Constants.RESPONSE_SUCCEED);
                        getRecommendBean.setRecommendationList(recommendListBean.getRecommendationList());
                        callback.updateRecommed(getRecommendBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (throwable instanceof ApiCodeErrorException) {
                            getRecommendBean.setResponseState(Constants.RESPONSE_ERROR);
                        }else{
                            getRecommendBean.setResponseState(Constants.RESPONSE_FAIL);
                        }
                        callback.updateRecommed(getRecommendBean);
                    }
                });
    }

    @Override
    public void getPriceModel() {
        final PriceBean priceBean = new PriceBean();
        GetQuotePriceForAppService getQuotePriceForAppService = ApiServiceGenerator.createService(GetQuotePriceForAppService.class);
        Map<String, String> baseMap = new HashMap<>();
        getQuotePriceForAppService.getOrderInfo(baseMap)
                .subscribeOn(Schedulers.io())
                .map(new NetworkResultFunc1<GetQuotePriceBean>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<GetQuotePriceBean>() {
                    @Override
                    public void call(GetQuotePriceBean getQuotePriceBean) {
                        priceBean.setResponseState(Constants.RESPONSE_SUCCEED);
                        priceBean.setResultBeen(getQuotePriceBean.getQuotePrice().getResult());
                        callback.updatePrice(priceBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (throwable instanceof ApiCodeErrorException) {
                            priceBean.setResponseState(Constants.RESPONSE_ERROR);
                        }else{
                            priceBean.setResponseState(Constants.RESPONSE_FAIL);
                        }
                        callback.updatePrice(priceBean);
                    }
                });
    }

    @Override
    public void getArticleListModel(final String folderId, final int index) {
        final NewsBean newsBean = new NewsBean();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("folderId", folderId);
        params.put("currentPage", "0");
        params.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        VolleyUtil.getInstance().httpPost(context, Urls.GET_ARTICLE_LIST, params, new HttpListener() {
            @Override
            public void onResponseJson(JsonObject response) {
                super.onResponseJson(response);
                String errorCode = response.get(Constants.RESPONSE_CODE).getAsString();
                if (errorCode.equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {
                    Gson gson = new Gson();
                    List<NewsEntity> newsList = new ArrayList<NewsEntity>();
                    if (response.get(Constants.RESPONSE_CONTENT).isJsonArray()) {
                        List<NewsEntity> tempList = gson.fromJson(response.get(Constants.RESPONSE_CONTENT),
                                new TypeToken<List<NewsEntity>>() {
                                }.getType());
                        if (tempList != null && tempList.size() != 0) {
                            newsList.addAll(OrderAndGroupsData(tempList));
                        }
                    }
                    newsBean.setResponseState(Constants.RESPONSE_SUCCEED);
                    newsBean.setNewsEntities(newsList);

                } else {
                    newsBean.setResponseState(Constants.RESPONSE_FAIL);
                }
                newsBean.setIndex(index);
                newsBean.setFolderId(folderId);
                callback.updateNews(newsBean);
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                newsBean.setResponseState(Constants.RESPONSE_ERROR);
                newsBean.setFolderId(folderId);
                newsBean.setIndex(index);
                callback.updateNews(newsBean);
            }
        }, false);

    }



    /**
     * 遍历 最新3条服务器过来的数据
     *
     * @param sectionNewsEntity
     * @return
     */
    private List<NewsEntity> OrderAndGroupsData(List<NewsEntity> sectionNewsEntity) {
        //截取 前三的数据
        if (sectionNewsEntity.size() > 3) {
            return sectionNewsEntity.subList(0, 3);
        } else {
            return sectionNewsEntity;
        }
    }

}

interface NewIndexModelInterface {
    void getAdModel();

    void getNoticeModel();

    void getTradeEventModel();

    void getRecommendModel();

    void getPriceModel();

    void getArticleListModel(String folderId,int index);

}