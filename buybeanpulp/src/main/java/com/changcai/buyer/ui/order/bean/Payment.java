package com.changcai.buyer.ui.order.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;

/**
 * @author wlv
 * @version 1.0
 * @description 付款信息
 * @date 16/6/18 下午4:06
 */
public class Payment  implements Serializable,IKeepFromProguard {

//    "amountZh": "31,140",
//    "createTime": "2016-06-15",
//    "payObjectZh": "订单提货单支付确认",
//            "symbolZh": "+"
    private String amountZh ;
    private String createTime ;
    private String payObjectZh ;
    private String symbolZh ;

    public String getAmountZh() {
        return amountZh;
    }

    public void setAmountZh(String amountZh) {
        this.amountZh = amountZh;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPayObjectZh() {
        return payObjectZh;
    }

    public void setPayObjectZh(String payObjectZh) {
        this.payObjectZh = payObjectZh;
    }

    public String getSymbolZh() {
        return symbolZh;
    }

    public void setSymbolZh(String symbolZh) {
        this.symbolZh = symbolZh;
    }
}
