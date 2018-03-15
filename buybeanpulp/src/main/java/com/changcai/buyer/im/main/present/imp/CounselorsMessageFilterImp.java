package com.changcai.buyer.im.main.present.imp;

import com.changcai.buyer.bean.GetCounselorsModel;
import com.changcai.buyer.im.main.present.MessageFilterInterface;
import com.changcai.buyer.im.main.present.MessageUpdateCallback;
import com.changcai.buyer.util.LogUtil;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lufeisong on 2018/3/13.
 * 过滤出顾问的信息
 */

public class CounselorsMessageFilterImp implements MessageFilterInterface {
    List<GetCounselorsModel.InfoBean> counselors;
    MessageUpdateCallback callback;

    public CounselorsMessageFilterImp( MessageUpdateCallback callback) {
        this.callback = callback;
    }

    public void setCounselors(List<GetCounselorsModel.InfoBean> counselors) {
        this.counselors = counselors;
    }

    @Override
    public void filterMessage(final List<RecentContact> recentContacts) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<RecentContact> contactsConsultantBlock = new ArrayList<>();//顾问集合 (P2P）

                if (recentContacts == null) {
                    return;
                }
                //遍历出顾问集合
                for (int i = 0; i < recentContacts.size(); i++) {//遍历出所有未读消息数目和message
                    RecentContact recentContact = recentContacts.get(i);
                    int position = -1;
                    LogUtil.d("NimIM", "顾问 ： 第" + i + "位: contactId = " + recentContact.getContactId() + "; fromAccount = " + recentContact.getFromAccount() + " ; unreadCount = " + recentContact.getUnreadCount() + " ; message = " + recentContact.getContent() + " ext = " + (recentContact.getExtension() == null ? " null" : recentContact.getExtension().toString()) + " ; time = " + recentContact.getTime());
                    if (recentContact.getSessionType().getValue() == SessionTypeEnum.P2P.getValue()) {//P2P
                        //遍历出顾问
                        if (counselors != null) {
                            for (int j = 0; j < counselors.size(); j++) {
                                if (counselors.get(j).getAccid().equals(recentContact.getContactId())) {
                                    for (int n = 0; n < contactsConsultantBlock.size(); n++) {
                                        if (contactsConsultantBlock.get(n).getContactId().equals(recentContact.getContactId())) {
                                            position = n;
                                            break;
                                        }
                                    }
                                    if (position != -1) {
                                        if (contactsConsultantBlock.size() > position) {
                                            contactsConsultantBlock.set(position, recentContact);
                                        }
                                    } else {
                                        contactsConsultantBlock.add(recentContact);
                                    }
                                    break;
                                }
                            }
                        }

                    }
                }
                //遍历所有人未读消息数量，最近一条消息
                if (callback != null) {
                    callback.messageUpdate(contactsConsultantBlock);
                }

            }
        }).start();
    }
}
