package com.changcai.buyer.ui.news.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;

public class RecommendNewsEntity implements Serializable, IKeepFromProguard {
    /**
     * 标题
     */
    private String name;
    /**
     * 新闻图片
     */
    private String picture;
    /**
     * 文章地址
     */
    private String url;

    /**
     *概要
     */
    private String summary;


    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}