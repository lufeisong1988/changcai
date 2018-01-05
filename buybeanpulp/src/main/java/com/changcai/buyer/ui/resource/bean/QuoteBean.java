package com.changcai.buyer.ui.resource.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lufeisong on 2017/8/31.
 */

public class QuoteBean implements IKeepFromProguard {

    /**
     * allQuote : {"result":[{"supplierName":"油厂","deliveryLocation":"厦门中盛","showPrice":"2760","price":"持平","factoryName":"厦门中盛","deliveryDate":"201708","quoteDate":1502692200000},{"supplierName":"油厂","deliveryLocation":"东莞植之元","showPrice":"2810","price":"","factoryName":"植之元","deliveryDate":"201708","quoteDate":1502690400000},{"supplierName":"油厂","deliveryLocation":"钦州中粮","showPrice":"2830","price":-20,"factoryName":"钦州中粮","deliveryDate":"201708","quoteDate":1502686800000},{"supplierName":"油厂","deliveryLocation":"泉州福海","showPrice":"2750","price":-40,"factoryName":"泉州福海","deliveryDate":"201708","quoteDate":1502685600000},{"supplierName":"油厂","deliveryLocation":"防城港九三","showPrice":"2800","price":"","factoryName":"惠禹","deliveryDate":"201708","quoteDate":1502677620000},{"supplierName":"油厂","deliveryLocation":"防城港九三","showPrice":"2900","price":"","factoryName":"惠禹","deliveryDate":"201708","quoteDate":1502677620000},{"supplierName":"油厂","deliveryLocation":"植之元","showPrice":"2840","price":-40,"factoryName":"植之元","deliveryDate":"201708","quoteDate":1502441400000},{"supplierName":"油厂","deliveryLocation":"福清康宏","showPrice":"2790","price":"","factoryName":"福清康宏","deliveryDate":"201708","quoteDate":1502441400000},{"supplierName":"油厂","deliveryLocation":"长乐元成","showPrice":"2800","price":"","factoryName":"长乐元成","deliveryDate":"201708","quoteDate":1502441400000},{"supplierName":"油厂","deliveryLocation":"东莞中粮","showPrice":"2830","price":"","factoryName":"东莞中粮","deliveryDate":"201708","quoteDate":1502441400000}],"total":225}
     */

    private AllQuoteBean allQuote;

    public AllQuoteBean getAllQuote() {
        return allQuote;
    }

    public void setAllQuote(AllQuoteBean allQuote) {
        this.allQuote = allQuote;
    }

    public static class AllQuoteBean implements IKeepFromProguard{
        /**
         * result : [{"supplierName":"油厂","deliveryLocation":"厦门中盛","showPrice":"2760","price":"持平","factoryName":"厦门中盛","deliveryDate":"201708","quoteDate":1502692200000},{"supplierName":"油厂","deliveryLocation":"东莞植之元","showPrice":"2810","price":"","factoryName":"植之元","deliveryDate":"201708","quoteDate":1502690400000},{"supplierName":"油厂","deliveryLocation":"钦州中粮","showPrice":"2830","price":-20,"factoryName":"钦州中粮","deliveryDate":"201708","quoteDate":1502686800000},{"supplierName":"油厂","deliveryLocation":"泉州福海","showPrice":"2750","price":-40,"factoryName":"泉州福海","deliveryDate":"201708","quoteDate":1502685600000},{"supplierName":"油厂","deliveryLocation":"防城港九三","showPrice":"2800","price":"","factoryName":"惠禹","deliveryDate":"201708","quoteDate":1502677620000},{"supplierName":"油厂","deliveryLocation":"防城港九三","showPrice":"2900","price":"","factoryName":"惠禹","deliveryDate":"201708","quoteDate":1502677620000},{"supplierName":"油厂","deliveryLocation":"植之元","showPrice":"2840","price":-40,"factoryName":"植之元","deliveryDate":"201708","quoteDate":1502441400000},{"supplierName":"油厂","deliveryLocation":"福清康宏","showPrice":"2790","price":"","factoryName":"福清康宏","deliveryDate":"201708","quoteDate":1502441400000},{"supplierName":"油厂","deliveryLocation":"长乐元成","showPrice":"2800","price":"","factoryName":"长乐元成","deliveryDate":"201708","quoteDate":1502441400000},{"supplierName":"油厂","deliveryLocation":"东莞中粮","showPrice":"2830","price":"","factoryName":"东莞中粮","deliveryDate":"201708","quoteDate":1502441400000}]
         * total : 225
         */

        private int total;
        private List<ResultBean> result = new ArrayList<>();

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<ResultBean> getResult() {
            return result;
        }

        public void setResult(List<ResultBean> result) {
            this.result = result;
        }

        public static class ResultBean implements IKeepFromProguard{
            /**
             * supplierName : 油厂
             * deliveryLocation : 厦门中盛
             * showPrice : 2760
             * price : 持平
             * factoryName : 厦门中盛
             * deliveryDate : 201708
             * quoteDate : 1502692200000
             */

            public static final int GROUP = 0;
            public static final int CHILD = 1;
            public int type = CHILD;
            public String headStr = "";
            public String domainsName = "";

            private String supplierName;
            private String deliveryLocation;
            private String showPrice;
            private String price;
            private String factoryName;
            private String deliveryDate;
            private long quoteDate;
            private long sourceId;
            private long source;//0:是资源报价，1:豆粕商城数据

            public String getSupplierName() {
                return supplierName;
            }

            public void setSupplierName(String supplierName) {
                this.supplierName = supplierName;
            }

            public String getDeliveryLocation() {
                return deliveryLocation;
            }

            public void setDeliveryLocation(String deliveryLocation) {
                this.deliveryLocation = deliveryLocation;
            }

            public String getShowPrice() {
                return showPrice;
            }

            public void setShowPrice(String showPrice) {
                this.showPrice = showPrice;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getFactoryName() {
                return factoryName;
            }

            public void setFactoryName(String factoryName) {
                this.factoryName = factoryName;
            }

            public String getDeliveryDate() {
                return deliveryDate;
            }

            public void setDeliveryDate(String deliveryDate) {
                this.deliveryDate = deliveryDate;
            }

            public long getQuoteDate() {
                return quoteDate;
            }

            public void setQuoteDate(long quoteDate) {
                this.quoteDate = quoteDate;
            }

            public long getSourceId() {
                return sourceId;
            }

            public void setSourceId(long sourceId) {
                this.sourceId = sourceId;
            }

            public long getSource() {
                return source;
            }

            public void setSource(long source) {
                this.source = source;
            }

            @Override
            public String toString() {
                return "ResultBean{" +
                        "type=" + type +
                        ", headStr='" + headStr + '\'' +
                        ", domainsName='" + domainsName + '\'' +
                        ", supplierName='" + supplierName + '\'' +
                        ", deliveryLocation='" + deliveryLocation + '\'' +
                        ", showPrice='" + showPrice + '\'' +
                        ", price='" + price + '\'' +
                        ", factoryName='" + factoryName + '\'' +
                        ", deliveryDate='" + deliveryDate + '\'' +
                        ", quoteDate=" + quoteDate +
                        ", sourceId=" + sourceId +
                        ", source=" + source +
                        '}';
            }
        }
    }
}
