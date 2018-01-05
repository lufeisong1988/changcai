package com.changcai.buyer.ui.news.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lufeisong on 2017/11/17.
 */

public class NewsBean implements Serializable, IKeepFromProguard {
    private List<NewsEntity> newsEntities = new ArrayList<>();
    private int responseState;
    private String folderId;
    private int index;

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public List<NewsEntity> getNewsEntities() {
        return newsEntities;
    }

    public void setNewsEntities(List<NewsEntity> newsEntities) {
        this.newsEntities = newsEntities;
    }

    public int getResponseState() {
        return responseState;
    }

    public void setResponseState(int responseState) {
        this.responseState = responseState;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
