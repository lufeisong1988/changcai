package com.changcai.buyer.im.main.present.imp;

import com.changcai.buyer.im.main.present.MessageFilterInterface;
import com.changcai.buyer.im.main.present.MessageUpdateCallback;
import com.changcai.buyer.util.LogUtil;
import com.netease.nim.uikit.common.util.MsgUtil;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lufeisong on 2018/3/13.
 * 所有消息（过滤掉顾问发给所有人的初始化消息）
 */

public class AllMessageFilterImp implements MessageFilterInterface {
    MessageUpdateCallback callback;

    public AllMessageFilterImp(MessageUpdateCallback callback) {
        this.callback = callback;
    }

    @Override
    public void filterMessage(final List<RecentContact> recentContacts) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<RecentContact> contactsAllBlock = new ArrayList<>();//所有人集合（P2P）

                if (recentContacts == null) {
                    return;
                }
                //遍历出所有人集合
                //剔除顾问发给每个人的初始化消息
                for (int i = 0; i < recentContacts.size(); i++) {//遍历出所有未读消息数目和message
                    RecentContact recentContact = recentContacts.get(i);
                    int position = -1;
                    if (recentContact.getSessionType().getValue() == SessionTypeEnum.P2P.getValue()) {//P2P
                        LogUtil.d("NimIM", "最近联系 ： 第" + i + "位: contactId = " + recentContact.getContactId() + "; fromAccount = " + recentContact.getFromAccount() + " ; unreadCount = " + recentContact.getUnreadCount() + " ; message = " + recentContact.getContent() + " ext = " + (recentContact.getExtension() == null ? " null" : recentContact.getExtension().toString()) + " ; time = " + recentContact.getTime());
                        //遍历出所有人（剔除掉顾问发的初始化信息）
                        for (int j = 0; j < contactsAllBlock.size(); j++) {
                            RecentContact contactsAll = contactsAllBlock.get(j);
                            if (contactsAll.getContactId().equals(recentContact.getContactId())) {
                                position = j;
                                break;
                            }
                        }
                        if (position != -1) {
                            if (contactsAllBlock.size() > position && !MsgUtil.fliteMessage(recentContact)) {
                                contactsAllBlock.set(position, recentContact);
                            }
                        } else {
                            if (!MsgUtil.fliteMessage(recentContact)) {
                                contactsAllBlock.add(recentContact);
                            }
                        }
                    }
                }
                //遍历所有人未读消息数量，最近一条消息
                if(callback != null){
                    callback.messageUpdate(contactsAllBlock);
                }
            }
        }).start();
    }
}
