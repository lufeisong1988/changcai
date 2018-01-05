package com.changcai.buyer.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lufeisong on 2017/11/17.
 */

public class RecommendListBean implements Serializable, IKeepFromProguard {

    private List<RecommendationListBean> recommendationList;

    public List<RecommendationListBean> getRecommendationList() {
        return recommendationList;
    }

    public void setRecommendationList(List<RecommendationListBean> recommendationList) {
        this.recommendationList = recommendationList;
    }

    public static class RecommendationListBean implements Serializable, IKeepFromProguard {
        /**
         * showTime : 2017-11-17
         * channel : APP
         * id : 2
         * sort : 2
         * title : 推荐22
         * url :
         * status : display
         */

        private String showTime;
        private String channel;
        private int id;
        private int sort;
        private String title;
        private String url;
        private String status;

        public String getShowTime() {
            return showTime;
        }

        public void setShowTime(String showTime) {
            this.showTime = showTime;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
