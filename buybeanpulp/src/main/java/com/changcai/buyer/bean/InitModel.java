package com.changcai.buyer.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liuxingwei on 2016/12/2.
 */

public class InitModel implements Serializable, IKeepFromProguard {


    /**
     * customerCenter : 021-54180261
     * needUpdate : false
     * updateLog : 新版本大幅度优化资讯速度
     */

    private String customerCenter;
    private String needUpdate;
    private String updateLog;
    private List<H5Resources> h5Resources;
    //广告弹窗
    private Advertisement advertisement;


    public Advertisement getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(Advertisement advertisement) {
        this.advertisement = advertisement;
    }

    public List<H5Resources> getH5Resources() {
        return h5Resources;
    }

    public void setH5Resources(List<H5Resources> h5Resources) {
        this.h5Resources = h5Resources;
    }

    public String getCustomerCenter() {
        return customerCenter;
    }

    public void setCustomerCenter(String customerCenter) {
        this.customerCenter = customerCenter;
    }

    public String getNeedUpdate() {
        return needUpdate;
    }

    public void setNeedUpdate(String needUpdate) {
        this.needUpdate = needUpdate;
    }

    public String getUpdateLog() {
        return updateLog;
    }

    public void setUpdateLog(String updateLog) {
        this.updateLog = updateLog;
    }
}
