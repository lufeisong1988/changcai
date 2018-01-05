package com.changcai.buyer.ui.order.bean;

import com.changcai.buyer.IKeepFromProguard;

/**
 * @author wlv
 * @version 1.0
 * @description 交易信息
 * @date 16/6/18 下午4:02
 */
public class Trader  implements IKeepFromProguard {

//    "afterStatus": "CONTRACT_CONFIRMED",
//            "afterStatusZh": "合同已确认",
//            "createTime": "2016-06-15",
//            "tradeObject": "BUYER",
//            "tradeObjectZh": "买家"

    private String afterStatus;
    private String afterStatusZh;
    private String createTime;
    private String tradeObject;
    private String tradeObjectZh;

    public String getAfterStatus() {
        return afterStatus;
    }

    public void setAfterStatus(String afterStatus) {
        this.afterStatus = afterStatus;
    }

    public String getAfterStatusZh() {
        return afterStatusZh;
    }

    public void setAfterStatusZh(String afterStatusZh) {
        this.afterStatusZh = afterStatusZh;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTradeObject() {
        return tradeObject;
    }

    public void setTradeObject(String tradeObject) {
        this.tradeObject = tradeObject;
    }

    public String getTradeObjectZh() {
        return tradeObjectZh;
    }

    public void setTradeObjectZh(String tradeObjectZh) {
        this.tradeObjectZh = tradeObjectZh;
    }
}
