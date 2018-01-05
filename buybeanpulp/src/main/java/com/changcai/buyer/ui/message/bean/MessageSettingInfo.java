package com.changcai.buyer.ui.message.bean;

import com.changcai.buyer.IKeepFromProguard;

/**
 * @author wlv
 * @version 1.0
 * @description
 * @date 16/6/25 下午3:48
 */
public class MessageSettingInfo implements IKeepFromProguard {

//    "key": "changcaiView",
//            "name": "畅采观察",
//            "info": "畅采观察",
//            "status": "1"

    private String key;
    private String name;
    private String status;
    private String info;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
