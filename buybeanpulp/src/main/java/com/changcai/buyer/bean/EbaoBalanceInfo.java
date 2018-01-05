package com.changcai.buyer.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;

/**
 * Created by liuxingwei on 2017/1/8.
 */
public class EbaoBalanceInfo implements Serializable, IKeepFromProguard {


    private String bankAcountBalance;

    private String bankAccNo;

    public String getBankAccNo() {
        return bankAccNo;
    }

    public void setBankAccNo(String bankAccNo) {
        this.bankAccNo = bankAccNo;
    }

    public String getBankAcountBalance() {
        return bankAcountBalance;
    }

    public void setBankAcountBalance(String bankAcountBalance) {
        this.bankAcountBalance = bankAcountBalance;
    }
}
