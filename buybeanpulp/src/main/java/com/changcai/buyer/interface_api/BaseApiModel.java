package com.changcai.buyer.interface_api;

import android.support.annotation.Keep;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;

/**
 * Created by liuxingwei on 2016/11/28.
 */

@Keep
public class BaseApiModel<T> implements Serializable,IKeepFromProguard{


    private String errorCode;
    private String errorDesc;
    private T resultObject;

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

    public T getResultObject() {
        return resultObject;
    }

    public void setResultObject(T resultObject) {
        this.resultObject = resultObject;
    }
}
