package com.changcai.buyer.ui.news.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lufeisong on 2017/11/17.
 */

public class TradeEventBean implements Serializable, IKeepFromProguard {
    private Date date;
    private int  responseState;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getResponseState() {
        return responseState;
    }

    public void setResponseState(int responseState) {
        this.responseState = responseState;
    }
}
