package com.changcai.buyer.im.main.present.imp;

import com.changcai.buyer.im.main.present.MessageFilterInterface;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lufeisong on 2018/3/13.
 */

public class MessageController extends MessageImp {
    List<MessageFilterInterface> messageFilter = new ArrayList<>();

    public void setMessageFilter(List<MessageFilterInterface> messageFilter) {
        this.messageFilter = messageFilter;
    }

    @Override
    public void dispatchMessage(List<RecentContact> recentContacts) {
        for(MessageFilterInterface messageFilterInterface:messageFilter){
            messageFilterInterface.filterMessage(recentContacts);
        }
    }
}
