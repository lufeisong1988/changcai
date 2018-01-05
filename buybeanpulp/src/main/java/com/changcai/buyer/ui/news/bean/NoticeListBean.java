package com.changcai.buyer.ui.news.bean;

import com.changcai.buyer.IKeepFromProguard;
import com.changcai.buyer.bean.NoticeBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lufeisong on 2017/11/17.
 */

public class NoticeListBean implements Serializable, IKeepFromProguard {
    private List<NoticeBean.NoticeListBean> noticeList;
    private int responseState;


    public List<NoticeBean.NoticeListBean> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(List<NoticeBean.NoticeListBean> noticeList) {
        this.noticeList = noticeList;
    }

    public int getResponseState() {
        return responseState;
    }

    public void setResponseState(int responseState) {
        this.responseState = responseState;
    }
}
