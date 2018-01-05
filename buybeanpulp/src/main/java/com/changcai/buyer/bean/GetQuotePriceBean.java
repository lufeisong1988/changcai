package com.changcai.buyer.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lufeisong on 2017/11/17.
 */


public class GetQuotePriceBean implements Serializable, IKeepFromProguard {
    /**
     * quotePrice : {"result":[{"supplierName":"油厂","proteinSpec":"43%","deliveryLocation":"防城港澳加","showPrice":"M1801+80","price":"持平","factoryName":"澳加","deliveryDate":"201710","updatePhone":"021-54180258-812","productName":"豆粕","quoteDate":1504590780000},{"supplierName":"油厂","proteinSpec":"43%","deliveryLocation":"防城港澳加","showPrice":"M1801+80","price":"持平","factoryName":"澳加","deliveryDate":"201711","updatePhone":"021-54180258-812","productName":"豆粕","quoteDate":1504590780000},{"supplierName":"油厂","proteinSpec":"43%","deliveryLocation":"防城港澳加","showPrice":"M1801+80","price":"持平","factoryName":"澳加","deliveryDate":"201712","updatePhone":"021-54180258-812","productName":"豆粕","quoteDate":1504590780000}],"total":1105}
     */

    private QuotePriceBean quotePrice;

    public QuotePriceBean getQuotePrice() {
        return quotePrice;
    }

    public void setQuotePrice(QuotePriceBean quotePrice) {
        this.quotePrice = quotePrice;
    }

    public static class QuotePriceBean implements Serializable, IKeepFromProguard{
        /**
         * result : [{"supplierName":"油厂","proteinSpec":"43%","deliveryLocation":"防城港澳加","showPrice":"M1801+80","price":"持平","factoryName":"澳加","deliveryDate":"201710","updatePhone":"021-54180258-812","productName":"豆粕","quoteDate":1504590780000},{"supplierName":"油厂","proteinSpec":"43%","deliveryLocation":"防城港澳加","showPrice":"M1801+80","price":"持平","factoryName":"澳加","deliveryDate":"201711","updatePhone":"021-54180258-812","productName":"豆粕","quoteDate":1504590780000},{"supplierName":"油厂","proteinSpec":"43%","deliveryLocation":"防城港澳加","showPrice":"M1801+80","price":"持平","factoryName":"澳加","deliveryDate":"201712","updatePhone":"021-54180258-812","productName":"豆粕","quoteDate":1504590780000}]
         * total : 1105
         */

        private int total;
        private List<ResultBean> result;

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

        public static class ResultBean implements Serializable, IKeepFromProguard{
            /**
             * supplierName : 油厂
             * proteinSpec : 43%
             * deliveryLocation : 防城港澳加
             * showPrice : M1801+80
             * price : 持平
             * factoryName : 澳加
             * deliveryDate : 201710
             * updatePhone : 021-54180258-812
             * productName : 豆粕
             * quoteDate : 1504590780000
             */

            private String supplierName;
            private String proteinSpec;
            private String deliveryLocation;
            private String showPrice;
            private String price;
            private String factoryName;
            private String deliveryDate;
            private String updatePhone;
            private String productName;
            private long quoteDate;

            public String getSupplierName() {
                return supplierName;
            }

            public void setSupplierName(String supplierName) {
                this.supplierName = supplierName;
            }

            public String getProteinSpec() {
                return proteinSpec;
            }

            public void setProteinSpec(String proteinSpec) {
                this.proteinSpec = proteinSpec;
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

            public String getUpdatePhone() {
                return updatePhone;
            }

            public void setUpdatePhone(String updatePhone) {
                this.updatePhone = updatePhone;
            }

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
            }

            public long getQuoteDate() {
                return quoteDate;
            }

            public void setQuoteDate(long quoteDate) {
                this.quoteDate = quoteDate;
            }
        }
    }

}

