package com.changcai.buyer.ui.news.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lufeisong on 2017/11/17.
 */

public class AdBean implements Serializable, IKeepFromProguard {
    private List<RecommendNewsEntity> adLists = new ArrayList<>();
    private int responseState ;

    public List<RecommendNewsEntity> getAdLists() {
        return adLists;
    }

    public void setAdLists(List<RecommendNewsEntity> adLists) {
        this.adLists = adLists;
    }

    public int getResponseState() {
        return responseState;
    }

    public void setResponseState(int responseState) {
        this.responseState = responseState;
    }
}
