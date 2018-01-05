package com.changcai.buyer.ui.strategy.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lufeisong on 2017/9/2.
 */

public class SalesAmountBean implements IKeepFromProguard {


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
        List<MonthsBean> monthsBeenList;

        public List<MonthsBean> getMonthsBeenList() {
            return monthsBeenList;
        }

        public void setMonthsBeenList(List<MonthsBean> monthsBeenList) {
            this.monthsBeenList = monthsBeenList;
        }

        public static class MonthsBean{
            private String year;
            private ArrayList<String> months;

            public String getYear() {
                return year;
            }

            public void setYear(String year) {
                this.year = year;
            }

            public ArrayList<String> getMonths() {
                return months;
            }

            public void setMonths(ArrayList<String> months) {
                this.months = months;
            }
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
