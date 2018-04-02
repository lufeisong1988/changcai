package com.changcai.buyer.bean;

import com.netease.nim.uikit.business.session.IKeepFromProguard;

import java.io.Serializable;

/**
 * Created by lufeisong on 2018/3/19.
 */

public class StrategyPaperModel implements Serializable,IKeepFromProguard {
    String article;

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }
}
