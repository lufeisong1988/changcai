package com.changcai.buyer.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lufeisong on 2018/3/6.
 */

public class StrategyTargetModel implements Serializable ,IKeepFromProguard {

    /**
     * date : 2017-10-24
     * targets : [{"targetId":"指标id","dataUrlParam":"/basis/3#futCommVal#spotCommVal#reg","name":"豆粕基差-华东","code":"M%.DCE#普通蛋白豆粕#华东地区"},{"targetId":"指标id","dataUrlParam":"/basis/3#futCommVal#spotCommVal#reg","name":"豆粕基差-华北","code":"M%.DCE#普通蛋白豆粕#华北地区"},{"targetId":"指标id","dataUrlParam":"/basis/3#futCommVal#spotCommVal#reg","name":"豆粕基差-东北","code":"M%.DCE#普通蛋白豆粕#东北地区"}]
     */

    private String date;
    private List<TargetsBean> targets;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<TargetsBean> getTargets() {
        return targets;
    }

    public void setTargets(List<TargetsBean> targets) {
        this.targets = targets;
    }

    public static class TargetsBean implements Serializable ,IKeepFromProguard {
        /**
         * targetId : 指标id
         * dataUrlParam : /basis/3#futCommVal#spotCommVal#reg
         * name : 豆粕基差-华东
         * code : M%.DCE#普通蛋白豆粕#华东地区
         */

        private String targetId;
        private String dataUrlParam;
        private String name;
        private String code;

        public String getTargetId() {
            return targetId;
        }

        public void setTargetId(String targetId) {
            this.targetId = targetId;
        }

        public String getDataUrlParam() {
            return dataUrlParam;
        }

        public void setDataUrlParam(String dataUrlParam) {
            this.dataUrlParam = dataUrlParam;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }


    }
}
