package com.changcai.buyer.ui.news.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;

public class NewsEntity implements Serializable, IKeepFromProguard {
//    "articleId": "31",
//            "articleUrl": "http://3g.163.com/touch/article.html?docid=BODVA0SG00014AEE&version=A",
//            "createTime": "2016年05月31日",
//            "tag": "市场动态",
//            "timeDesc": "一周内",
//            "title": "快讯：物流板块涨幅领先 天顺股份涨停"
    private String flag;//首页判断是否是父还是子
    private String articleId;
    private String articleUrl;
    private String createTime;
    private String tag;
    private String title;
    private String timeDesc;
    private String picUrl;
    private String section;
    private String minGrade; //会员等级权限
    private String summary;
    private String minVipGrade;
    private String hasAuthority;
    private String minGradePic;//会员等级图片地址

    public String getHasAuthority() {
        return hasAuthority;
    }

    public void setHasAuthority(String hasAuthority) {
        this.hasAuthority = hasAuthority;
    }

    public String getMinVipGrade() {
        return minVipGrade;
    }

    public void setMinVipGrade(String minVipGrade) {
        this.minVipGrade = minVipGrade;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getMinGrade() {
        return minGrade;
    }

    public void setMinGrade(String minGrade) {
        this.minGrade = minGrade;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimeDesc() {
        return timeDesc;
    }

    public void setTimeDesc(String timeDesc) {
        this.timeDesc = timeDesc;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getMinGradePic() {
        return minGradePic;
    }

    public void setMinGradePic(String minGradePic) {
        this.minGradePic = minGradePic;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof NewsEntity) {
            return this.getSection().equals(((NewsEntity) o).getSection());
        }
        return false;
    }

    @Override
    public String toString() {
        return "NewsEntity{" +
                "flag='" + flag + '\'' +
                ", articleId='" + articleId + '\'' +
                ", articleUrl='" + articleUrl + '\'' +
                ", createTime='" + createTime + '\'' +
                ", tag='" + tag + '\'' +
                ", title='" + title + '\'' +
                ", timeDesc='" + timeDesc + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", section='" + section + '\'' +
                ", minGrade='" + minGrade + '\'' +
                ", summary='" + summary + '\'' +
                ", minVipGrade='" + minVipGrade + '\'' +
                ", hasAuthority='" + hasAuthority + '\'' +
                ", minGradePic='" + minGradePic + '\'' +
                '}';
    }
}