package com.changcai.buyer.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;

/**
 * Created by liuxingwei on 2017/8/1.
 */

public class SpotFolderListBean implements Serializable, IKeepFromProguard {


    /**
     * folderId : 123
     * name : 点睛
     */

    private String folderId;
    private String name;

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
