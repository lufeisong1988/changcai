package com.changcai.buyer.ui.order.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;

/**
 * Created by liuxingwei on 2017/1/10.
 */
public class Buttons implements Serializable,IKeepFromProguard{


    /**
     * type : VIEW_CONTRACT
     * message : 查看合同
     */

    private String type;
    private String message;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
