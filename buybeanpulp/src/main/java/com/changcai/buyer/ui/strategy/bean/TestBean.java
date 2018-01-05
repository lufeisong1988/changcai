package com.changcai.buyer.ui.strategy.bean;

/**
 * Created by lufeisong on 2017/9/7.
 */

import com.changcai.buyer.IKeepFromProguard;

import java.util.List;
import java.util.Map;

/**
 * Created by lufeisong on 2017/9/2.
 */

public class TestBean implements IKeepFromProguard {


    /**
     * dateStr : {"2017年":["03月","04月","05月","06月","07月","08月","09月","10月","11月","12月"]}
     * salesByMonth : {"y":["0","0","0","116","434","246","3","0","0","0"],"x":["2017-03","2017-04","2017-05","2017-06","2017-07","2017-08","2017-09","2017-10","2017-11","2017-12"]}
     * nowDate : 2017-09
     */

    private DateStrBean dateStr;
    private SalesByMonthBean salesByMonth;
    private String nowDate;

    public DateStrBean getDateStr() {
        return dateStr;
    }

    public void setDateStr(DateStrBean dateStr) {
        this.dateStr = dateStr;
    }

    public SalesByMonthBean getSalesByMonth() {
        return salesByMonth;
    }

    public void setSalesByMonth(SalesByMonthBean salesByMonth) {
        this.salesByMonth = salesByMonth;
    }

    public String getNowDate() {
        return nowDate;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public static class DateStrBean implements IKeepFromProguard{
        private List<Map<String,String[]>> dateStr;

        public List<Map<String, String[]>> getDateStr() {
            return dateStr;
        }

        public void setDateStr(List<Map<String, String[]>> dateStr) {
            this.dateStr = dateStr;
        }
    }

    public static class SalesByMonthBean implements IKeepFromProguard{
        private List<String> y;
        private List<String> x;

        public List<String> getY() {
            return y;
        }

        public void setY(List<String> y) {
            this.y = y;
        }

        public List<String> getX() {
            return x;
        }

        public void setX(List<String> x) {
            this.x = x;
        }
    }
}
