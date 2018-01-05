package com.changcai.buyer.bean;

import com.changcai.buyer.IKeepFromProguard;
import com.changcai.buyer.ui.order.bean.DeliveryInfo;
import com.changcai.buyer.ui.order.bean.OrderInfo;

import java.io.Serializable;

/**
 * Created by liuxingwei on 2017/3/8.
 */
public class SpaceStatistic implements Serializable, IKeepFromProguard {


    /**
     * buyDeliveryPayed : 3
     * buyDeliveryUnpay : 0
     * newBuyConfirming : 0
     * newBuyDepositing : 0
     * openBuyPick : 0
     * openBuyPicking : 0
     */

    private String buyDeliveryPayed;
    private String buyDeliveryUnpay;

    public String getUnReadMsg() {
        return unReadMsg;
    }

    public void setUnReadMsg(String unReadMsg) {
        this.unReadMsg = unReadMsg;
    }

    private String newBuyConfirming;
    private String newBuyDepositing;
    private String openBuyPick;
    private String openBuyPicking;
    private String allPayOrder;
    private String unReadMsg;
    private String fastPayOrder;
    private String deliveryOrder;


    public String getDeliveryOrder() {
        return deliveryOrder;
    }

    public void setDeliveryOrder(String deliveryOrder) {
        this.deliveryOrder = deliveryOrder;
    }

    private OrderInfo2 orderInfo;

    private DeliveryInfo  deliveryInfo;
    private String reserveDepositPayOrder;

    public String getReserveDepositPayOrder() {
        return reserveDepositPayOrder;
    }

    public void setReserveDepositPayOrder(String reserveDepositPayOrder) {
        this.reserveDepositPayOrder = reserveDepositPayOrder;
    }

    public DeliveryInfo getDeliveryInfo() {
        return deliveryInfo;
    }

    public void setDeliveryInfo(DeliveryInfo deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }

    public OrderInfo2 getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo2 orderInfo) {
        this.orderInfo = orderInfo;
    }

    public String getAllPayOrder() {
        return allPayOrder;
    }

    public void setAllPayOrder(String allPayOrder) {
        this.allPayOrder = allPayOrder;
    }

    public String getFastPayOrder() {
        return fastPayOrder;
    }

    public void setFastPayOrder(String fastPayOrder) {
        this.fastPayOrder = fastPayOrder;
    }

    public String getBuyDeliveryPayed() {
        return buyDeliveryPayed;
    }

    public void setBuyDeliveryPayed(String buyDeliveryPayed) {
        this.buyDeliveryPayed = buyDeliveryPayed;
    }

    public String getBuyDeliveryUnpay() {
        return buyDeliveryUnpay;
    }

    public void setBuyDeliveryUnpay(String buyDeliveryUnpay) {
        this.buyDeliveryUnpay = buyDeliveryUnpay;
    }

    public String getNewBuyConfirming() {
        return newBuyConfirming;
    }

    public void setNewBuyConfirming(String newBuyConfirming) {
        this.newBuyConfirming = newBuyConfirming;
    }

    public String getNewBuyDepositing() {
        return newBuyDepositing;
    }

    public void setNewBuyDepositing(String newBuyDepositing) {
        this.newBuyDepositing = newBuyDepositing;
    }

    public String getOpenBuyPick() {
        return openBuyPick;
    }

    public void setOpenBuyPick(String openBuyPick) {
        this.openBuyPick = openBuyPick;
    }

    public String getOpenBuyPicking() {
        return openBuyPicking;
    }

    public void setOpenBuyPicking(String openBuyPicking) {
        this.openBuyPicking = openBuyPicking;
    }
}
