package com.changcai.buyer.bean;

/**
 * Created by liuxingwei on 2017/4/6.
 * 派单类型
 */

public enum OrderEnum {

    CASH_ONHAND_ORDER("当日现款现货（无定金）订单", "CASH_ONHAND_ORDER"),
    //    当日现款现货（无定金）订单
    RESERVE_DEPOSIT_ORDER("预定订单", "RESERVE_DEPOSIT_ORDER");
//    预定订单


    private String orderTypeDescription;
    private String orderTypeCode;

    private OrderEnum(String orderTypeDescription, String orderTypeCode) {
        this.orderTypeCode = orderTypeCode;
        this.orderTypeDescription = orderTypeDescription;
    }

    // get set 方法
    public String getOrderTypeDescription() {
        return orderTypeDescription;
    }


    public String getrderTypeCode() {
        return orderTypeCode;
    }


}
