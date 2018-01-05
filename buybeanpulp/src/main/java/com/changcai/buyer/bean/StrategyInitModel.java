package com.changcai.buyer.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liuxingwei on 2017/8/1.
 */

public class StrategyInitModel implements Serializable , IKeepFromProguard{


    private List<SpotFolderListBean> spotFolderList;
    private List<AuthsBean> auths;

    public List<SpotFolderListBean> getSpotFolderList() {
        return spotFolderList;
    }

    public void setSpotFolderList(List<SpotFolderListBean> spotFolderList) {
        this.spotFolderList = spotFolderList;
    }

    public List<AuthsBean> getAuths() {
        return auths;
    }

    public void setAuths(List<AuthsBean> auths) {
        this.auths = auths;
    }
}
