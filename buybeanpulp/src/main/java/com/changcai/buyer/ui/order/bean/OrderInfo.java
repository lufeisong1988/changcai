package com.changcai.buyer.ui.order.bean;

import com.changcai.buyer.IKeepFromProguard;
import com.changcai.buyer.bean.PriceInfo;

import java.io.Serializable;
import java.util.List;

/**
 * @author wlv
 * @version 1.0
 * @description
 * @date 16/6/5 下午9:00
 */
public class OrderInfo implements Serializable, IKeepFromProguard {
    //    id
//    orderAmount
//    orderNo
//    orderViewStatus
//    price
//            quantity
//    regionAndLocation
//            deliverys
//    trades
//            payments
//    订单id，查看详情用，不显示
//            订单总金额
//    订单号，给用户看的
//            订单状态
//    单价
//            数量
//    交货地点
//    提货单列表（数组），参看5.4的提货单字段说明
//    订单信息（数组）
//    支付信息（数组）
    private String id;
    private String orderAmount;
    private String orderNo;
    private String orderViewStatus;
    private String price;
    private String quantity;
    private String regionAndLocation;
    private String deliveryEndTime;
    private String deliveryStartTime;
    private List<DeliveryInfo> deliverys;
    private List<Payment> payments;
    private List<Trader> trades;
    private List<PricingInfo> pricings;
    private String orderStatus;//订单状态，显示用
    //    private String nextOrderStatus;//订单状态，后台自用
//    private String nextOrderViewStatus;//下一步订单状态
    private String updateTime;
    private String payOffset;
    private String createTime;
    private String orderType;//基差活动一口价

    public String getOrderModel() {
        return orderModel;
    }

    public void setOrderModel(String orderModel) {
        this.orderModel = orderModel;
    }

    private String orderModel;//派单

    public String getPayModel() {
        return payModel;
    }

    public void setPayModel(String payModel) {
        this.payModel = payModel;
    }

    public String getBuyerFastPayStatus() {
        return buyerFastPayStatus;
    }

    public void setBuyerFastPayStatus(String buyerFastPayStatus) {
        this.buyerFastPayStatus = buyerFastPayStatus;
    }

    private String payModel;
    private String buyerFastPayStatus;

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    private String currentTime;

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    private PriceInfo product;

    private String contractTempURI;

    public String getContractTempURI() {
        return contractTempURI;
    }

    public void setContractTempURI(String contractTempURI) {
        this.contractTempURI = contractTempURI;
    }


    private String havePricingQuantityHand;//已点数
    private String unPricingQuantityHand;    //	未点数
    private String haveDeliveryQuantity;    //已提数
    private String unDeliveryQuantity;//	string	未提数

    public String getHavePricingQuantityHand() {
        return havePricingQuantityHand;
    }

    public void setHavePricingQuantityHand(String havePricingQuantityHand) {
        this.havePricingQuantityHand = havePricingQuantityHand;
    }

    public String getUnPricingQuantityHand() {
        return unPricingQuantityHand;
    }

    public void setUnPricingQuantityHand(String unPricingQuantityHand) {
        this.unPricingQuantityHand = unPricingQuantityHand;
    }

    public String getHaveDeliveryQuantity() {
        return haveDeliveryQuantity;
    }

    public void setHaveDeliveryQuantity(String haveDeliveryQuantity) {
        this.haveDeliveryQuantity = haveDeliveryQuantity;
    }

    public String getUnDeliveryQuantity() {
        return unDeliveryQuantity;
    }

    public void setUnDeliveryQuantity(String unDeliveryQuantity) {
        this.unDeliveryQuantity = unDeliveryQuantity;
    }

    /**
     *
     * 控制订单按钮状态和行为
     */
    private List<Buttons> buttons;

    public List<Buttons> getButtons() {
        return buttons;
    }

    public void setButtons(List<Buttons> buttons) {
        this.buttons = buttons;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPayOffset() {
        return payOffset;
    }

    public void setPayOffset(String payOffset) {
        this.payOffset = payOffset;
    }

    public PriceInfo getProduct() {
        return product;
    }

    public void setProduct(PriceInfo product) {
        this.product = product;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

//    public String getNextOrderStatus() {
//        return nextOrderStatus;
//    }
//
//    public void setNextOrderStatus(String nextOrderStatus) {
//        this.nextOrderStatus = nextOrderStatus;
//    }
//
//    public String getNextOrderViewStatus() {
//        return nextOrderViewStatus;
//    }
//
//    public void setNextOrderViewStatus(String nextOrderViewStatus) {
//        this.nextOrderViewStatus = nextOrderViewStatus;
//    }

    public String getDeliveryEndTime() {
        return deliveryEndTime;
    }

    public void setDeliveryEndTime(String deliveryEndTime) {
        this.deliveryEndTime = deliveryEndTime;
    }

    public String getDeliveryStartTime() {
        return deliveryStartTime;
    }

    public void setDeliveryStartTime(String deliveryStartTime) {
        this.deliveryStartTime = deliveryStartTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderViewStatus() {
        return orderViewStatus;
    }

    public void setOrderViewStatus(String orderViewStatus) {
        this.orderViewStatus = orderViewStatus;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRegionAndLocation() {
        return regionAndLocation;
    }

    public void setRegionAndLocation(String regionAndLocation) {
        this.regionAndLocation = regionAndLocation;
    }

    public List<DeliveryInfo> getDeliverys() {
        return deliverys;
    }

    public void setDeliverys(List<DeliveryInfo> deliverys) {
        this.deliverys = deliverys;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public List<Trader> getTrades() {
        return trades;
    }

    public void setTrades(List<Trader> trades) {
        this.trades = trades;
    }

    public List<PricingInfo> getPricings() {
        return pricings;
    }

    public void setPricings(List<PricingInfo> pricings) {
        this.pricings = pricings;
    }
}
