package com.changcai.buyer.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liuxingwei on 2017/2/13.
 */
public class FiltrateDetailModel implements IKeepFromProguard,Serializable{


    /**
     * filterType : spec
     * filterName : 蛋白规格
     * filterList : [{"id":"","name":"全部"},{"id":"PERCENT42","name":"42%"},{"id":"PERCENT43","name":"43%"},{"id":"PERCENT45","name":"45%"},{"id":"PERCENT46","name":"46%"},{"id":"PERCENT48","name":"48%"}]
     */

    private String filterType;
    private String filterName;
    private List<FilterListBean> filterList;

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public List<FilterListBean> getFilterList() {
        return filterList;
    }

    public void setFilterList(List<FilterListBean> filterList) {
        this.filterList = filterList;
    }

    public static class FilterListBean implements Serializable,IKeepFromProguard{
        /**
         * id :
         * name : 全部
         */

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
