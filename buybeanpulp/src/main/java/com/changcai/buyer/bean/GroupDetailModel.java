package com.changcai.buyer.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by lufeisong on 2018/3/9.
 */

public class GroupDetailModel implements Serializable,IKeepFromProguard {
    String tId;
    String tName;
    String icon = "";
    String createdId;
    int msgUnreadCount = 0;
    long msgTime = 0;
    String msg = "";
    String tdesc = "";
    HashMap<String,String> onLineMap = new HashMap<>();
    HashMap<String,String> offLineMap = new HashMap<>();
    boolean myTeam = false;

    public String getTdesc() {
        return tdesc;
    }

    public void setTdesc(String tdesc) {
        this.tdesc = tdesc;
    }

    public String gettId() {
        return tId;
    }

    public void settId(String tId) {
        this.tId = tId;
    }

    public String gettName() {
        return tName;
    }

    public void settName(String tName) {
        this.tName = tName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCreatedId() {
        return createdId;
    }

    public void setCreatedId(String createdId) {
        this.createdId = createdId;
    }

    public HashMap<String, String> getOnLineMap() {
        return onLineMap;
    }

    public void setOnLineMap(HashMap<String, String> onLineMap) {
        this.onLineMap = onLineMap;
    }

    public HashMap<String, String> getOffLineMap() {
        return offLineMap;
    }

    public void setOffLineMap(HashMap<String, String> offLineMap) {
        this.offLineMap = offLineMap;
    }

    public int getMsgUnreadCount() {
        return msgUnreadCount;
    }

    public void setMsgUnreadCount(int msgUnreadCount) {
        this.msgUnreadCount = msgUnreadCount;
    }

    public long getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(long msgTime) {
        this.msgTime = msgTime;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isMyTeam() {
        return myTeam;
    }

    public void setMyTeam(boolean myTeam) {
        this.myTeam = myTeam;
    }

    @Override
    public String toString() {
        return "GroupDetailModel{" +
                "tId='" + tId + '\'' +
                ", tName='" + tName + '\'' +
                ", icon='" + icon + '\'' +
                ", createdId='" + createdId + '\'' +
                ", msgUnreadCount=" + msgUnreadCount +
                ", msgTime=" + msgTime +
                ", msg='" + msg + '\'' +
                ", onLineMap=" + onLineMap +
                ", offLineMap=" + offLineMap +
                ", myTeam=" + myTeam +
                '}';
    }
}
