package com.changcai.buyer.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;

/**
 * Created by liuxingwei on 2016/11/9.
 */

public class BackReseverInfo implements Serializable ,IKeepFromProguard{

        private String releaseStatus;
        private String needUpdate;

        public String getReleaseStatus() {
            return releaseStatus;
        }

        public void setReleaseStatus(String releaseStatus) {
            this.releaseStatus = releaseStatus;
        }

        public String getNeedUpdate() {
            return needUpdate;
        }

        public void setNeedUpdate(String needUpdate) {
            this.needUpdate = needUpdate;
        }
}
