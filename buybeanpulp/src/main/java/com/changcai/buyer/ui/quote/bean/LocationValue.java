package com.changcai.buyer.ui.quote.bean;

import com.changcai.buyer.IKeepFromProguard;

/**
 * @author wlv
 * @version 1.0
 * @description
 * @date 16/6/5 下午9:17
 */
public class LocationValue  implements IKeepFromProguard {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
