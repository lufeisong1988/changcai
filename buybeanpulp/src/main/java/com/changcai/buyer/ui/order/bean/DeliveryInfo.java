package com.changcai.buyer.ui.order.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;
import java.util.List;

/**
 * @author wlv
 * @version 1.0
 * @description 提货信息
 * @date 16/6/18 下午3:58
 */
public class DeliveryInfo implements Serializable , IKeepFromProguard{
//    "actualAmount": "",
//            "address": "安徽 蚌埠徽商码头",
//            "allQuantity": "1000",
//            "amount": "",
//            "createTime": "2016-06-22",
//            "deliveryEndTime": "",
//            "deliveryPre": "",
//            "deliveryStartTime": "2016-06-21",
//            "deliveryTime": "",
//            "eggSpec": "43%",
//            "factoryBrand": "邦基",
//            "id": "162",
//            "nextStatus": "CONFIRM",
//            "nextViewStatus": "待确认收货",
//            "orderId": "801",
//            "payOffset": "",
//            "price": "",
//            "quantity": "1000",
//            "sellerName": "刘轶",
//            "status": "UNPAY",
//            "userName": "",
//            "userPhone": "",
//            "viewStatus": "待支付货款"
//    货款金额amount 保证金可抵扣金额payOffset 占保证金总额百分比 payOffsetPre实际支付金额actualAmount
    private String quantity;
    private String address;
    private String allQuantity;
    private String amount;
    private String createTime;
    private String deliveryEndTime;
    private String deliveryStartTime;
    private String eggSpec;
    private String factoryBrand;
    private String id;
    private String orderId;
    private String payOffset;
    private String price;
    private String sellerName;
    private String status;
    private String userName;
    private String userPhone;
    private String viewStatus;
    private String nextStatus;
    private String nextViewStatus;
    private String payOffsetPre;
    private String deliveryTime;
    private String deliveryPre;
    private String originalAmount;
    private String packType;
    private String deliveryMode;
    private String actualAmount;
    private String memo;
    private String payTypeDesc;
    private String deliveryNo;
    private String sellerMobile;
    private String payType;
    private String sellerEnterName;
    /**
     * 弹窗专用
     */
    private String deliveryId;

    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getSellerEnterName() {
        return sellerEnterName;
    }

    public void setSellerEnterName(String sellerEnterName) {
        this.sellerEnterName = sellerEnterName;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getSellerMobile() {
        return sellerMobile;
    }

    public void setSellerMobile(String sellerMobile) {
        this.sellerMobile = sellerMobile;
    }

    public String getPayTypeDesc() {
        return payTypeDesc;
    }

    public void setPayTypeDesc(String payTypeDesc) {
        this.payTypeDesc = payTypeDesc;
    }

    public String getDeliveryNo() {
        return deliveryNo;
    }

    public void setDeliveryNo(String deliveryNo) {
        this.deliveryNo = deliveryNo;
    }


    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(String originalAmount) {
        this.originalAmount = originalAmount;
    }

    public String getPackType() {
        return packType;
    }

    public void setPackType(String packType) {
        this.packType = packType;
    }

    public String getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(String deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    /**
     * 控制订单按钮状态和行为
     */
    private List<Buttons> buttons;

    public List<Buttons> getButtons() {
        return buttons;
    }

    public void setButtons(List<Buttons> buttons) {
        this.buttons = buttons;
    }

    public String getPayOffsetPre() {
        return payOffsetPre;
    }

    public void setPayOffsetPre(String payOffsetPre) {
        this.payOffsetPre = payOffsetPre;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(String actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAllQuantity() {
        return allQuantity;
    }

    public void setAllQuantity(String allQuantity) {
        this.allQuantity = allQuantity;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDeliveryEndTime() {
        return deliveryEndTime;
    }

    public void setDeliveryEndTime(String deliveryEndTime) {
        this.deliveryEndTime = deliveryEndTime;
    }

    public String getDeliveryPre() {
        return deliveryPre;
    }

    public void setDeliveryPre(String deliveryPre) {
        this.deliveryPre = deliveryPre;
    }

    public String getDeliveryStartTime() {
        return deliveryStartTime;
    }

    public void setDeliveryStartTime(String deliveryStartTime) {
        this.deliveryStartTime = deliveryStartTime;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getEggSpec() {
        return eggSpec;
    }

    public void setEggSpec(String eggSpec) {
        this.eggSpec = eggSpec;
    }

    public String getFactoryBrand() {
        return factoryBrand;
    }

    public void setFactoryBrand(String factoryBrand) {
        this.factoryBrand = factoryBrand;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPayOffset() {
        return payOffset;
    }

    public void setPayOffset(String payOffset) {
        this.payOffset = payOffset;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
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


}
