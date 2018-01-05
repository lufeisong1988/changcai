package com.changcai.buyer.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author wlv
 * @version 1.0
 * @description
 * @date 16/6/11 上午12:31
 */
public class UserInfo implements Serializable,IKeepFromProguard {

    private static final long serialVersionUID = 7880715551086333847L;
    private String account;
    private String bankCardId;
    private String id;
    private String enterId;
    private String mobile;
    private String name;
    private String sex;
    private String tokenId;
    /**
     * 管理员或者财务
     */
    private String typeName;
    private String usrNo;
    private String enterStatus;
    private String enterType;
    private String isBuyer;
    /**
     * 等级
     */
    private String grade;
    /**
     * 等级名称
     */
    private String gradeName;
    /**
     * 等级图片地址
     */
    private String gradePic;
    /**
     * 企业类型 （油厂 ，个体商户，贸易商）
     */
    private String enterTypeName;
    /**
     * SUCCESS 绑定银行
     */
    private String bankSignStatus;

    /**
     * false 支付密码
     */
    private String payPassword;
    /**
     * false 签署入驻协议
     */
    private String isContracted;
    /**
     * 买家介绍信息
     */
    private String buyerInformation;
    /**
     * 卖家介绍信息
     */
    private String sellerInformation;

    /**
     * 支付通道
     */
    private ArrayList<PayChannel> payChannels;
    /**
     * admin／child account
     */
    private String type;


    private String gradeEndTime;
    /**
     * 顾问信息
     */
    private String imToken;
    private String serviceStatus;
    private String serviceLevel;
    private String counselorStatus;

    public String getImToken() {
        return imToken;
    }

    public void setImToken(String imToken) {
        this.imToken = imToken;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getServiceLevel() {
        return serviceLevel;
    }

    public void setServiceLevel(String serviceLevel) {
        this.serviceLevel = serviceLevel;
    }

    public String getCounselorStatus() {
        return counselorStatus;
    }

    public void setCounselorStatus(String counselorStatus) {
        this.counselorStatus = counselorStatus;
    }

    public String getGradeEndTime() {
        return gradeEndTime;
    }

    public void setGradeEndTime(String gradeEndTime) {
        this.gradeEndTime = gradeEndTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * 企业名称

     */
    private  String  enterName;

    public String getEnterName() {
        return enterName;
    }

    public void setEnterName(String enterName) {
        this.enterName = enterName;
    }

    public String getEnterTypeName() {
        return enterTypeName;
    }

    public void setEnterTypeName(String enterTypeName) {
        this.enterTypeName = enterTypeName;
    }

    public String getBankSignStatus() {
        return bankSignStatus;
    }

    public void setBankSignStatus(String bankSignStatus) {
        this.bankSignStatus = bankSignStatus;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public String getIsContracted() {
        return isContracted;
    }

    public void setIsContracted(String isContracted) {
        this.isContracted = isContracted;
    }

    public String getBuyerInformation() {
        return buyerInformation;
    }

    public void setBuyerInformation(String buyerInformation) {
        this.buyerInformation = buyerInformation;
    }

    public String getSellerInformation() {
        return sellerInformation;
    }

    public void setSellerInformation(String sellerInformation) {
        this.sellerInformation = sellerInformation;
    }

    public ArrayList<PayChannel> getPayChannels() {
        return payChannels;
    }

    public void setPayChannels(ArrayList<PayChannel> payChannels) {
        this.payChannels = payChannels;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getIsBuyer() {
        return isBuyer;
    }

    public void setIsBuyer(String isBuyer) {
        this.isBuyer = isBuyer;
    }

    public String getEnterType() {
        return enterType;
    }

    public void setEnterType(String enterType) {
        this.enterType = enterType;
    }

    public String getEnterStatus() {
        return enterStatus;
    }

    public void setEnterStatus(String enterStatus) {
        this.enterStatus = enterStatus;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBankCardId() {
        return bankCardId;
    }

    public void setBankCardId(String bankCardId) {
        this.bankCardId = bankCardId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnterId() {
        return enterId;
    }

    public void setEnterId(String enterId) {
        this.enterId = enterId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getUsrNo() {
        return usrNo;
    }

    public void setUsrNo(String usrNo) {
        this.usrNo = usrNo;
    }

    public String getGradePic() {
        return gradePic;
    }

    public void setGradePic(String gradePic) {
        this.gradePic = gradePic;
    }
}
