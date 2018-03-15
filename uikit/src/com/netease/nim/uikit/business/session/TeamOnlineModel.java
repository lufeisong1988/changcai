package com.netease.nim.uikit.business.session;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by lufeisong on 2018/3/8.
 * 群在线人数model
 */

public class TeamOnlineModel implements Serializable,IKeepFromProguard{
    String tid;
    HashMap<String,String> onLineMap = new HashMap<>();
    HashMap<String,String> offLineMap = new HashMap<>();

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
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

    @Override
    public String toString() {
        return "TeamOnlineModel{" +
                "tid='" + tid + '\'' +
                ", onLineMap=" + onLineMap +
                ", offLineMap=" + offLineMap +
                '}';
    }
}
