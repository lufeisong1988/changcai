package com.changcai.buyer.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;

/**
 * Created by liuxingwei on 2017/4/7.
 */

public class PlatformAmount implements IKeepFromProguard , Serializable{


    /**
     * accountType : RECHARGE
     * amountYuan : 1000
     */

    private String accountType;
    private String amountYuan;

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAmountYuan() {
        return amountYuan;
    }

    public void setAmountYuan(String amountYuan) {
        this.amountYuan = amountYuan;
    }
}
