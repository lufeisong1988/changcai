package com.changcai.buyer.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;

/**
 * Created by liuxingwei on 2017/2/27.
 * 判断服务器状态
 */

public class ResponseStatus implements IKeepFromProguard,Serializable{
    /**
     * errorCode == 0 则表示成功
     */
    private String errorCode;
    private String errorDesc;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }


    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }
}
