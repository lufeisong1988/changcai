package com.changcai.buyer.bean;

import com.changcai.buyer.IKeepFromProguard;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lufeisong on 2018/1/16.
 */

public class GetImTeamsBean implements Serializable,IKeepFromProguard {


    private List<InfoBean> info;

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean implements Serializable,IKeepFromProguard {
        /**
         * tname : 群名称：产业联盟
         * tid : 群id:279340661
         * taccid : 群主:18702178786
         */

        private String tname;
        private String tid;
        private String taccid;
        private String ticon;

        private String tintro;
        private String tdesc;

        public String getTdesc() {
            return tdesc;
        }

        public void setTdesc(String tdesc) {
            this.tdesc = tdesc;
        }

        public String getTintro() {
            return tintro;
        }

        public void setTintro(String tintro) {
            this.tintro = tintro;
        }

        public String getTname() {
            return tname;
        }

        public void setTname(String tname) {
            this.tname = tname;
        }

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }

        public String getTaccid() {
            return taccid;
        }

        public void setTaccid(String taccid) {
            this.taccid = taccid;
        }

        public String getTicon() {
            return ticon;
        }

        public void setTicon(String ticon) {
            this.ticon = ticon;
        }

        @Override
        public String toString() {
            return "InfoBean{" +
                    "tname='" + tname + '\'' +
                    ", tid='" + tid + '\'' +
                    ", taccid='" + taccid + '\'' +
                    '}';
        }
    }
}
