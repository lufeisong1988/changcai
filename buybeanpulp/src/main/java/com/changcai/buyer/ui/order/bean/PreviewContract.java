package com.changcai.buyer.ui.order.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;

/**
 * Created by liuxingwei on 2017/1/13.
 */
public class PreviewContract implements Serializable,IKeepFromProguard{


    /**
     * id : 6797
     * name : 20170113_0012017011300000825_ORD_ORDER_from.pdf
     * path : http://files2.maidoupo.com/ccfile//pdf/20170113_164d506a8f21713e84ece7548a072a5f.pdf
     * type : PDF
     * objectId : 428
     * createTime : 1484295885000
     * objectName : CONT_CONTRACT
     * enterId : -1
     */

    private int id;
    private String name;
    private String path;
    private String type;
    private int objectId;
    private long createTime;
    private String objectName;
    private int enterId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public int getEnterId() {
        return enterId;
    }

    public void setEnterId(int enterId) {
        this.enterId = enterId;
    }
}
