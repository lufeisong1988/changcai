package com.changcai.buyer.ui.quote.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.util.List;

/**
 * @author wlv
 * @version 1.0
 * @description
 * @date 16/6/5 下午9:16
 */
public class RegionValue implements IKeepFromProguard {

    private String id;
    private String name;
    private List<LocationValue> lstMetaLocationInfo;
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

    public List<LocationValue> getLstMetaLocationInfo() {
        return lstMetaLocationInfo;
    }

    public void setLstMetaLocationInfo(List<LocationValue> lstMetaLocationInfo) {
        this.lstMetaLocationInfo = lstMetaLocationInfo;
    }
}
