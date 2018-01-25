package com.changcai.buyer.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lufeisong on 2017/12/21.
 * 批量获取顾问信息
 */

public class GetCounselorsModel implements Serializable, IKeepFromProguard {


    private List<InfoBean> info;

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean implements Serializable, IKeepFromProguard{
        /**
         * counselorName : 吃吃吃
         * serviceStatus : EXIT
         * accid : 18516603218
         * serviceLevelDesc : 钻石会员
         * serviceLevel : 300
         * counselorStatus : DOWNLINE
         */

        private String counselorName;
        private String serviceStatus;
        private String accid;
        private String serviceLevelDesc;
        private int serviceLevel;
        private String counselorStatus;


        public String getCounselorName() {
            return counselorName;
        }

        public void setCounselorName(String counselorName) {
            this.counselorName = counselorName;
        }

        public String getServiceStatus() {
            return serviceStatus;
        }

        public void setServiceStatus(String serviceStatus) {
            this.serviceStatus = serviceStatus;
        }

        public String getAccid() {
            return accid;
        }

        public void setAccid(String accid) {
            this.accid = accid;
        }

        public String getServiceLevelDesc() {
            return serviceLevelDesc;
        }

        public void setServiceLevelDesc(String serviceLevelDesc) {
            this.serviceLevelDesc = serviceLevelDesc;
        }

        public int getServiceLevel() {
            return serviceLevel;
        }

        public void setServiceLevel(int serviceLevel) {
            this.serviceLevel = serviceLevel;
        }

        public String getCounselorStatus() {
            return counselorStatus;
        }

        public void setCounselorStatus(String counselorStatus) {
            this.counselorStatus = counselorStatus;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            InfoBean infoBean = (InfoBean) o;

            if (serviceLevel != infoBean.serviceLevel) return false;
            if (counselorName != null ? !counselorName.equals(infoBean.counselorName) : infoBean.counselorName != null)
                return false;
            if (serviceStatus != null ? !serviceStatus.equals(infoBean.serviceStatus) : infoBean.serviceStatus != null)
                return false;
            if (accid != null ? !accid.equals(infoBean.accid) : infoBean.accid != null)
                return false;
            if (serviceLevelDesc != null ? !serviceLevelDesc.equals(infoBean.serviceLevelDesc) : infoBean.serviceLevelDesc != null)
                return false;
            return counselorStatus != null ? counselorStatus.equals(infoBean.counselorStatus) : infoBean.counselorStatus == null;

        }

        @Override
        public int hashCode() {
            int result = counselorName != null ? counselorName.hashCode() : 0;
            result = 31 * result + (serviceStatus != null ? serviceStatus.hashCode() : 0);
            result = 31 * result + (accid != null ? accid.hashCode() : 0);
            result = 31 * result + (serviceLevelDesc != null ? serviceLevelDesc.hashCode() : 0);
            result = 31 * result + serviceLevel;
            result = 31 * result + (counselorStatus != null ? counselorStatus.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "InfoBean{" +
                    "counselorName='" + counselorName + '\'' +
                    ", serviceStatus='" + serviceStatus + '\'' +
                    ", accid='" + accid + '\'' +
                    ", serviceLevelDesc='" + serviceLevelDesc + '\'' +
                    ", serviceLevel=" + serviceLevel +
                    ", counselorStatus='" + counselorStatus + '\'' +
                    '}';
        }
    }
}
