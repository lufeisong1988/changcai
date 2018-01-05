package com.changcai.buyer.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liuxingwei on 2017/2/13.
 */

public class FiltrateModel implements IKeepFromProguard,Serializable{

   private String name;
    private List<FiltrateDetailModel> list;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FiltrateDetailModel> getList() {
        return list;
    }

    public void setList(List<FiltrateDetailModel> list) {
        this.list = list;
    }
}
