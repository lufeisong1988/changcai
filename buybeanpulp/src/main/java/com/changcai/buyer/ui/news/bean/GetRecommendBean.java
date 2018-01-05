package com.changcai.buyer.ui.news.bean;

import com.changcai.buyer.IKeepFromProguard;
import com.changcai.buyer.bean.RecommendListBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lufeisong on 2017/11/17.
 */

public class GetRecommendBean implements Serializable, IKeepFromProguard {
    private List<RecommendListBean.RecommendationListBean> recommendationList;
    private int responseState;

    public List<RecommendListBean.RecommendationListBean> getRecommendationList() {
        return recommendationList;
    }

    public void setRecommendationList(List<RecommendListBean.RecommendationListBean> recommendationList) {
        this.recommendationList = recommendationList;
    }

    public int getResponseState() {
        return responseState;
    }

    public void setResponseState(int responseState) {
        this.responseState = responseState;
    }
}
