package com.changcai.buyer.im.main.present.imp;

import com.changcai.buyer.im.main.present.MessageFilterInterface;
import com.changcai.buyer.im.main.present.MessageUpdateCallback;
import com.changcai.buyer.util.LogUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lufeisong on 2018/3/13.
 * 过滤出群消息
 */

public class TeamMessageFilterImp implements MessageFilterInterface {
    MessageUpdateCallback callback;

    public TeamMessageFilterImp(MessageUpdateCallback callback) {
        this.callback = callback;
    }

    @Override
    public void filterMessage(final List<RecentContact> recentContacts) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<RecentContact> contactsTeamBlock = new ArrayList<>();//产业联盟集合 (Team）

                if (recentContacts == null) {
                    return;
                }
                //遍历出产业联盟集合
                for (int i = 0; i < recentContacts.size(); i++) {//遍历出所有未读消息数目和message
                    RecentContact recentContact = recentContacts.get(i);
                    int position = -1;
                    if (recentContact.getSessionType().getValue() == SessionTypeEnum.Team.getValue()) {//Team
                        LogUtil.d("NimIM", "群消息 ： 第" + i + "个: contactId = " + recentContact.getContactId() + "; fromAccount = " + recentContact.getFromAccount() + " ; unreadCount = " + recentContact.getUnreadCount() + " ; message = " + recentContact.getContent() + " ext = " + (recentContact.getExtension() == null ? " null" : recentContact.getExtension().toString()) + " ; time = " + recentContact.getTime());
                        for (int j = 0; j < contactsTeamBlock.size(); j++) {
                            if (contactsTeamBlock.get(j).getContactId().equals(recentContact.getContactId())) {
                                position = j;
                                break;
                            }
                        }
                        Team team = NIMClient.getService(TeamService.class).queryTeamBlock(recentContact.getContactId());
                        if (team != null) {
                            if (position != -1) {
                                if (team.isMyTeam() && contactsTeamBlock.size() > position) {
                                    contactsTeamBlock.set(position, recentContact);
                                }
                            } else {
                                if (team.isMyTeam()) {
                                    contactsTeamBlock.add(recentContact);
                                }
                            }
                        }
                    }
                }
                //刷新所有群
                if (callback != null) {
                    callback.messageUpdate(contactsTeamBlock);
                }
            }
        }).start();
    }
}
