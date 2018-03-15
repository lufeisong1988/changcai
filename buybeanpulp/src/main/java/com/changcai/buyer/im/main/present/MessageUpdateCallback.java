package com.changcai.buyer.im.main.present;

import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.List;

/**
 * Created by lufeisong on 2018/3/13.
 */

public interface MessageUpdateCallback {
    void messageUpdate(List<RecentContact> recentContacts);
}
