package com.changcai.buyer.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lufeisong on 2018/3/6.
 */

public class StrategyTargetDataModel implements Serializable,IKeepFromProguard {

    private List<ViewBean> view;

    public List<ViewBean> getView() {
        return view;
    }

    public void setView(List<ViewBean> view) {
        this.view = view;
    }

    public static class ViewBean implements Serializable,IKeepFromProguard {
        /**
         * date : 2017-11-10
         * price : 2230
         * point : not_exist
         */

        private String date;
        private String price;
        private String point;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPoint() {
            return point;
        }

        public void setPoint(String point) {
            this.point = point;
        }
    }
}
