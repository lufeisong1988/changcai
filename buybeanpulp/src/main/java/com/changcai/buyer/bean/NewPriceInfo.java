package com.changcai.buyer.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liuxingwei on 2017/2/14.
 */
public class NewPriceInfo implements Serializable,IKeepFromProguard{
    private  String isShowYellowTip;

    private List<PriceInfo> productList;


    public String getIsShowYellowTip() {
        return isShowYellowTip;
    }

    public void setIsShowYellowTip(String isShowYellowTip) {
        this.isShowYellowTip = isShowYellowTip;
    }

    public List<PriceInfo> getProductList() {
        return productList;
    }

    public void setProductList(List<PriceInfo> productList) {
        this.productList = productList;
    }
}
