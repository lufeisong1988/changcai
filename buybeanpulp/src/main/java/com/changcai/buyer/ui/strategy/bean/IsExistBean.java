package com.changcai.buyer.ui.strategy.bean;

import com.changcai.buyer.IKeepFromProguard;

/**
 * Created by lufeisong on 2017/9/4.
 */

public class IsExistBean implements IKeepFromProguard {

    /**
     * links : 环比：对应客户销售额月环比数
     * enterName : 泰州福海饲料贸易有限公司
     * isExist : true
     * sales : 销售额：在指定月份的销售单价*销售数量合计数 注意：如果是基差未点价合同时，销售单价取查询最新一个交易日货收盘价+基差价
     * hint :
     */

    private String links;
    private String enterName;
    private boolean isExist;
    private String sales;
    private String hint;

    public String getLinks() {
        return links;
    }

    public void setLinks(String links) {
        this.links = links;
    }

    public String getEnterName() {
        return enterName;
    }

    public void setEnterName(String enterName) {
        this.enterName = enterName;
    }

    public boolean isIsExist() {
        return isExist;
    }

    public void setIsExist(boolean isExist) {
        this.isExist = isExist;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
}
