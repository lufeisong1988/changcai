package com.changcai.buyer.ui.order.bean;

import android.os.Parcelable;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;

/**
 * @author wlv
 * @version 1.0
 * @description 提货信息
 * @date 16/6/18 下午3:58
 */
public class PricingInfo implements Serializable , IKeepFromProguard {

//    priceId
//            price
//    status
//            viewStatus
//    nextStatus
//            nextViewStatus
//    applyQuantity
//            successQuantity

    private String priceId;
    private String price;
    private String status;
    private String viewStatus;
    private String nextStatus;
    private String nextViewStatus;
    private String applyQuantity;
    private String successQuantity;
    private String transactionInfo;
    private String createTime;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTransactionInfo() {
        return transactionInfo;
    }

    public void setTransactionInfo(String transactionInfo) {
        this.transactionInfo = transactionInfo;
    }

    public String getPriceId() {
        return priceId;
    }

    public void setPriceId(String priceId) {
        this.priceId = priceId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getViewStatus() {
        return viewStatus;
    }

    public void setViewStatus(String viewStatus) {
        this.viewStatus = viewStatus;
    }

    public String getNextStatus() {
        return nextStatus;
    }

    public void setNextStatus(String nextStatus) {
        this.nextStatus = nextStatus;
    }

    public String getNextViewStatus() {
        return nextViewStatus;
    }

    public void setNextViewStatus(String nextViewStatus) {
        this.nextViewStatus = nextViewStatus;
    }

    public String getApplyQuantity() {
        return applyQuantity;
    }

    public void setApplyQuantity(String applyQuantity) {
        this.applyQuantity = applyQuantity;
    }

    public String getSuccessQuantity() {
        return successQuantity;
    }

    public void setSuccessQuantity(String successQuantity) {
        this.successQuantity = successQuantity;
    }
}
