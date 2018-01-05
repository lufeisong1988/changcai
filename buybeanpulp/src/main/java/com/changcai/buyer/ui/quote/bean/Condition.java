package com.changcai.buyer.ui.quote.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.util.List;

/**
 * @author wlv
 * @version 1.0
 * @description
 * @date 16/6/5 下午9:00
 */
public class Condition  implements IKeepFromProguard {

    private String dataType;
    private String filterType;
    private String name;
    private List<ConditionValue> value;

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ConditionValue> getValue() {
        return value;
    }

    public void setValue(List<ConditionValue> value) {
        this.value = value;
    }
}
