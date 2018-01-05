package com.changcai.buyer.bean;

import com.changcai.buyer.IKeepFromProguard;
import com.changcai.buyer.im.main.model.NotifactionItem;

import java.io.Serializable;

/**
 * Created by lufeisong on 2017/12/23.
 */

public class NotifactionItemModel implements Serializable, IKeepFromProguard {
    private String content;
    private boolean showDot;
    private NotifactionItem notifactionItem;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isShowDot() {
        return showDot;
    }

    public void setShowDot(boolean showDot) {
        this.showDot = showDot;
    }

    public NotifactionItem getNotifactionItem() {
        return notifactionItem;
    }

    public void setNotifactionItem(NotifactionItem notifactionItem) {
        this.notifactionItem = notifactionItem;
    }
}
