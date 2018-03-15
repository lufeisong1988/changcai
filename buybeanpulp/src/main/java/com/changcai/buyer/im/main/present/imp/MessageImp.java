package com.changcai.buyer.im.main.present.imp;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.List;

/**
 * Created by lufeisong on 2018/3/12.
 * 获取IM的消息
 */

public abstract class MessageImp {
    public abstract void dispatchMessage(List<RecentContact> recentContacts );

    public MessageImp() {
        initMessageObserave();
        registerRecentContact(true);
    }


    //主动获取未读消息和message
    private void initMessageObserave() {
        List<RecentContact> contactsBlock = NIMClient.getService(MsgService.class).queryRecentContactsBlock();
        dispatchMessage(contactsBlock);
    }

    //注册获取未读消息和message监听
    public void registerRecentContact(boolean register) {
        NIMClient.getService(MsgServiceObserve.class).observeRecentContact(messageObserver, register);
    }

    Observer<List<RecentContact>> messageObserver = new Observer<List<RecentContact>>() {
        @Override
        public void onEvent(List<RecentContact> recentContacts) {
            dispatchMessage(recentContacts);
        }
    };

    void onDestory(){
        registerRecentContact(false);
    }

}
