package com.changcai.buyer.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liuxingwei on 2017/8/1.
 */

public class PromptGoodsModel implements Serializable,IKeepFromProguard{


    /**
     * articles : [{"articleId":"","title":"点睛","summary":"summary","content":"<p>点睛<\/p>","createTime":"","articleUrl":""}]
     * lastUpdateTime : 2017-07-31
     */

    private String lastUpdateTime;
    private List<Articles> articles;

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public List<Articles> getArticles() {
        return articles;
    }

    public void setArticles(List<Articles> articles) {
        this.articles = articles;
    }

}
