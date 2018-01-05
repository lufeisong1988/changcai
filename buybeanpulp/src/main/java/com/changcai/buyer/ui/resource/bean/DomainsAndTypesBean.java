package com.changcai.buyer.ui.resource.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.util.List;

/**
 * Created by lufeisong on 2017/8/30.
 */

public class DomainsAndTypesBean implements IKeepFromProguard {


    private List<DomainsBean> domains;
    private List<ProductTypeBean> productType;

    public List<DomainsBean> getDomains() {
        return domains;
    }

    public void setDomains(List<DomainsBean> domains) {
        this.domains = domains;
    }

    public List<ProductTypeBean> getProductType() {
        return productType;
    }

    public void setProductType(List<ProductTypeBean> productType) {
        this.productType = productType;
    }

    public static class DomainsBean implements IKeepFromProguard{
        /**
         * name : 华南
         * id : 2
         */

        private String name;
        private int id;

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
    }

    public static class ProductTypeBean implements IKeepFromProguard{
        /**
         * id :
         * name : 全部
         */

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
