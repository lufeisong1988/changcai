package com.changcai.buyer.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;

/**
 * Created by liuxingwei on 2017/3/9.
 */
public class OrderInfo2 implements Serializable,IKeepFromProguard{


    /**
     * seller :
     * quantity :
     * orderId :
     * price :
     * packType :
     * eggSpec :
     * operation : 去签署合同
     * factoryBrand :
     */

    private String sellerEnterName;
    private String quantity;
    private String orderId;
    private String price;
    private String packType;
    private String eggSpec;
    private String operation;
    private String factoryBrand;

    public String getSeller() {
        return sellerEnterName;
    }

    public void setSeller(String seller) {
        this.sellerEnterName = seller;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPackType() {
        return packType;
    }

    public void setPackType(String packType) {
        this.packType = packType;
    }

    public String getEggSpec() {
        return eggSpec;
    }

    public void setEggSpec(String eggSpec) {
        this.eggSpec = eggSpec;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getFactoryBrand() {
        return factoryBrand;
    }

    public void setFactoryBrand(String factoryBrand) {
        this.factoryBrand = factoryBrand;
    }
}
