package com.changcai.buyer.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;

/**
 * Created by huangjian299 on 16/6/7.
 */
public class PriceInfo implements Serializable, IKeepFromProguard{
//
//    productType  SPOT：一口价BASIS：基差
//    packType
//    priceStartTime
//    priceEndTime
//    deliveryStartTime
//    deliveryEndTime
//    contactPhone
//    inventory
//    Id
//    price
//    enterStatus
//    enterId
//    enterInformation
//    enterName
//    factoryBrand
//    Region  + location
//    产品类型
//    包装类型
//    报价有效开始时间
//    报价有效结束时间
//    交货开始时间
//    交货结束时间
//    联系电话
//    库存
//    价格ID
//    价格
//    企业id，下单需要传给后台
//            企业介绍
//    企业名称
//            厂商品牌
//    交货地点
//            库存

//    "basisCode": "M1609",
//            "closingPrice": "3256",
//            "contactPhone": "",
//            "currentTimeMillis": "",
//            "deliveryEndTime": "2016-07-31",
//            "deliveryStartTime": "2016-07-01",
//            "eggSpec": "43%",
//            "enterId": "",
//            "enterInformation": "",
//            "enterName": "",
//            "enterPic": "http://192.168.5.13/ccfile//jpg/20160614_1465905742750.JPG",
//            "enterStatus": "SUCCESS",
//            "factoryBrand": "丰苑",
//            "id": "306",
//            "inventory": "900",
//            "location": "蚌埠徽商码头",
//            "minPricingNum": "",
//            "minPurchaseNum": "",
//            "packType": "KG_50",
//            "price": "110",
//            "priceEndTime": "2016-06-25 23:20:00",
//            "priceStartTime": "2016-06-14 20:50:00",
//            "productType": "BASIS",
//            "productTypeName": "基差",
//            "region": "安徽"
//    basisCode
//    基差代码，基差报价用到（元）
//    closingPrice
//    昨天收盘价，基差下单用
//    3.	报价详情增加 userEnterStatus 买家认证状态  userIsBuyer买家是否可以购买
    private String basisCode;
    private String closingPrice;
    private String price;
    private String contactPhone;
    private String deliveryEndTime;
    private String deliveryStartTime;
    private String eggSpec;
    private String enterId;
    private String enterInformation;
    private String enterName;
    private String enterStatus;
    private String factoryBrand;
    private String id;
    private String inventory;
    private String location;
    private String packType;
    private String priceEndTime;
    private String priceStartTime;
    private String productType;
    private String productTypeName;
    private String region;
//minPurchaseNum 最小购买数
//    minPricingNum 最小点价数
//    currentTimeMillis    当前服务器时间
    private String minPurchaseNum;
    private String minPricingNum;
    private String enterPic;
    private String currentTimeMillis;
    private String basisMonth;
    private String enterType;
    private String enterTypeName;
    private String priceStatus;
    private String helpArticleUrl;

    private String userEnterStatus;
    private String userIsBuyer;

    private String buyerDepositRate;
    private String depositRate;
    private String updateTime;
    private String publishTime;


    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getBuyerDepositRate() {
        return buyerDepositRate;
    }

    public void setBuyerDepositRate(String buyerDepositRate) {
        this.buyerDepositRate = buyerDepositRate;
    }

    public String getDepositRate() {
        return depositRate;
    }

    public void setDepositRate(String depositRate) {
        this.depositRate = depositRate;
    }

    public String getUserEnterStatus() {
        return userEnterStatus;
    }

    public void setUserEnterStatus(String userEnterStatus) {
        this.userEnterStatus = userEnterStatus;
    }

    public String getUserIsBuyer() {
        return userIsBuyer;
    }

    public void setUserIsBuyer(String userIsBuyer) {
        this.userIsBuyer = userIsBuyer;
    }

    public String getEnterTypeName() {
        return enterTypeName;
    }

    public void setEnterTypeName(String enterTypeName) {
        this.enterTypeName = enterTypeName;
    }

    public String getPriceStatus() {
        return priceStatus;
    }

    public void setPriceStatus(String priceStatus) {
        this.priceStatus = priceStatus;
    }

    public String getHelpArticleUrl() {
        return helpArticleUrl;
    }

    public void setHelpArticleUrl(String helpArticleUrl) {
        this.helpArticleUrl = helpArticleUrl;
    }

    public String getBasisMonth() {
        return basisMonth;
    }

    public void setBasisMonth(String basisMonth) {
        this.basisMonth = basisMonth;
    }

    public String getEnterPic() {
        return enterPic;
    }

    public void setEnterPic(String enterPic) {
        this.enterPic = enterPic;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

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

    public String getEggSpec() {
        return eggSpec;
    }

    public void setEggSpec(String eggSpec) {
        this.eggSpec = eggSpec;
    }

    public String getEnterId() {
        return enterId;
    }

    public void setEnterId(String enterId) {
        this.enterId = enterId;
    }

    public String getEnterInformation() {
        return enterInformation;
    }

    public void setEnterInformation(String enterInformation) {
        this.enterInformation = enterInformation;
    }

    public String getEnterName() {
        return enterName;
    }

    public void setEnterName(String enterName) {
        this.enterName = enterName;
    }

    public String getEnterStatus() {
        return enterStatus;
    }

    public void setEnterStatus(String enterStatus) {
        this.enterStatus = enterStatus;
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

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPackType() {
        return packType;
    }

    public void setPackType(String packType) {
        this.packType = packType;
    }

    public String getPriceEndTime() {
        return priceEndTime;
    }

    public void setPriceEndTime(String priceEndTime) {
        this.priceEndTime = priceEndTime;
    }

    public String getPriceStartTime() {
        return priceStartTime;
    }

    public void setPriceStartTime(String priceStartTime) {
        this.priceStartTime = priceStartTime;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public String getMinPurchaseNum() {
        return minPurchaseNum;
    }

    public void setMinPurchaseNum(String minPurchaseNum) {
        this.minPurchaseNum = minPurchaseNum;
    }

    public String getMinPricingNum() {
        return minPricingNum;
    }

    public void setMinPricingNum(String minPricingNum) {
        this.minPricingNum = minPricingNum;
    }

    public String getCurrentTimeMillis() {
        return currentTimeMillis;
    }

    public void setCurrentTimeMillis(String currentTimeMillis) {
        this.currentTimeMillis = currentTimeMillis;
    }

    public String getBasisCode() {
        return basisCode;
    }

    public void setBasisCode(String basisCode) {
        this.basisCode = basisCode;
    }

    public String getClosingPrice() {
        return closingPrice;
    }

    public void setClosingPrice(String closingPrice) {
        this.closingPrice = closingPrice;
    }

    public String getEnterType() {
        return enterType;
    }

    public void setEnterType(String enterType) {
        this.enterType = enterType;
    }
}
