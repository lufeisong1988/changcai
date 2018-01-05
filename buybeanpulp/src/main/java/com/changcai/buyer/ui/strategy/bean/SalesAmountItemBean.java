package com.changcai.buyer.ui.strategy.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.util.List;

/**
 * Created by lufeisong on 2017/9/3.
 */

public class SalesAmountItemBean implements IKeepFromProguard {

    /**
     * totalSales : 980,000
     * salesDetail : [{"volume":20001355,"name":"客户1","id":1,"receivable":5000339},{"volume":20001355,"name":"客户4","id":2,"receivable":5000339},{"volume":20000000,"name":"客户3","id":3,"receivable":5000000},{"volume":20000000,"name":"客户2","id":4,"receivable":5000000},{"volume":20000000,"name":"客户2","id":5,"receivable":5000000}]
     */

    private String totalSales;
    private List<SalesDetailBean> salesDetail;

    public String getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(String totalSales) {
        this.totalSales = totalSales;
    }

    public List<SalesDetailBean> getSalesDetail() {
        return salesDetail;
    }

    public void setSalesDetail(List<SalesDetailBean> salesDetail) {
        this.salesDetail = salesDetail;
    }

    public static class SalesDetailBean implements IKeepFromProguard{
        /**
         * volume : 20001355
         * name : 客户1
         * id : 1
         * receivable : 5000339
         */

        private int volume;
        private String name;
        private int id;
        private int receivable;

        public int getVolume() {
            return volume;
        }

        public void setVolume(int volume) {
            this.volume = volume;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getReceivable() {
            return receivable;
        }

        public void setReceivable(int receivable) {
            this.receivable = receivable;
        }
    }
}
