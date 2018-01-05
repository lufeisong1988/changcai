package com.changcai.buyer.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lufeisong on 2017/12/27.
 * 会员等级信息
 */

public class GetUserLevelBean implements Serializable, IKeepFromProguard {


    private List<String> name;
    private List<String> code;

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public List<String> getCode() {
        return code;
    }

    public void setCode(List<String> code) {
        this.code = code;
    }
}
