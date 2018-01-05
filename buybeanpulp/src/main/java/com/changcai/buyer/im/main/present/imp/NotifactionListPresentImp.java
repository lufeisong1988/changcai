package com.changcai.buyer.im.main.present.imp;

import android.text.TextUtils;

import com.changcai.buyer.bean.GetCounselorsModel;
import com.changcai.buyer.bean.UserInfo;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.im.config.preference.Preferences;
import com.changcai.buyer.im.main.model.NotifactionListModelInterface;
import com.changcai.buyer.im.main.model.imp.NotifactionListModelImp;
import com.changcai.buyer.im.main.present.NotifactionListPresentInterface;
import com.changcai.buyer.im.main.viewmodel.NotifacitonListViewModel;
import com.changcai.buyer.im.provider.LoginProvider;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.util.NimSessionHelper;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.UserDataUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lufeisong on 2017/12/22.
 */

public class NotifactionListPresentImp implements NotifactionListPresentInterface, NotifactionListModelImp.NotifactionListModelCallback, LoginProvider.LoginCallback {
    private NotifactionListModelInterface model;
    private NotifacitonListViewModel view;
    private int position = 0;
    private static final int VIP = 0;
    private static final int NOTIFACTION = 1;
    private List<GetCounselorsModel.InfoBean> info;
    private boolean syncAble = false;//是否同步请求

    public NotifactionListPresentImp(NotifacitonListViewModel view) {
        this.view = view;
        model = new NotifactionListModelImp(this);
        LoginProvider.getInstance().addLoginCallback(this);
        registerRecentContact(true);

    }

    @Override
    public void init() {
        if (!UserDataUtil.isLogin()) {
            if (view != null) {
                view.hideNOTIFACTION();
            }
        } else {
            UserInfo userInfo = SPUtil.getObjectFromShare(Constants.KEY_USER_INFO);
            if (userInfo.getServiceLevel() == null && userInfo.getServiceStatus() == null && userInfo.getCounselorStatus() == null) {
                if (view != null) {
                    view.hideNOTIFACTION();
                }
            } else {
                if (view != null) {
                    view.showNOTIFACTION();
                }
            }
        }
        initMessageObserave();
    }

    /**
     * 获取顾问信息
     */
    @Override
    public void getCounselorsModel(boolean syncAble) {
        this.syncAble = syncAble;
        if (view != null && syncAble) {
            view.showLoading();
        }
        model.getCounselorsModel();
    }

    @Override
    public void toVip() {
        position = VIP;
        if (checkLogin()) {
            getCounselorsModel(true);
        }
    }

    /**
     * 跳转消息回复
     */
    @Override
    public void toAnswers() {
        position = NOTIFACTION;
        if (checkLogin()) {
            if (view != null) {
                view.toNOTIFACTION();
            }
        }
    }

    /**
     * 检测 系统账号 和网易云信账号登录
     */
    private boolean checkLogin() {
        if (!UserDataUtil.isLogin()) {
            if (view != null) {
                view.unLogin();
            }
            return false;
        }
        if (!canLoginNIM()) {
            if (view != null) {
                view.showLoading();
                NimSessionHelper.getInstance().login();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onDestory() {
        view = null;
        LoginProvider.getInstance().deleteLoginCallback(this);
        registerRecentContact(false);
    }

    //批量获取顾问信息 回调
    @Override
    public void getCounselorsModelSucceed(List<GetCounselorsModel.InfoBean> info) {
        this.info = info;
        if (view != null && syncAble) {
            view.dismissLoading();
            view.getCounselorsModelSucceed(info);
        }
        initMessageObserave();
    }

    @Override
    public void getCounselorsModelFail(String failStr) {
        if (view != null && syncAble) {
            view.getCounselorsModelFail(failStr);
            view.dismissLoading();
        }
    }

    @Override
    public void getCounselorsModelError() {
        if (view != null && syncAble) {
            view.dismissLoading();
            view.getCounselorsModelError();
        }
    }

    /**
     * 网易云信是否登录
     */
    private boolean canLoginNIM() {
        String account = Preferences.getUserAccount();
        String token = Preferences.getUserToken();

        return !TextUtils.isEmpty(account) && !TextUtils.isEmpty(token);
    }

    /**
     * 登录网易云信结果
     */
    @Override
    public void nimLoginSucceed() {
        if (view != null) {
            view.dismissLoading();
        }
        if (position == VIP) {
            getCounselorsModel(true);
        } else if (position == NOTIFACTION) {
            toAnswers();
        }
    }

    @Override
    public void nimLoginFail(String failStr) {
        if (view != null) {
            view.dismissLoading();
            view.loginNimFail(failStr);
        }
    }

    //主动获取未读消息和message
    private void initMessageObserave() {
        List<RecentContact> contactsBlock = NIMClient.getService(MsgService.class).queryRecentContactsBlock();
        updateUI(contactsBlock);
    }

    //注册获取未读消息和message监听
    private void registerRecentContact(boolean register) {
        NIMClient.getService(MsgServiceObserve.class).observeRecentContact(messageObserver, register);
    }

    Observer<List<RecentContact>> messageObserver = new Observer<List<RecentContact>>() {
        @Override
        public void onEvent(List<RecentContact> recentContacts) {
            updateUI(recentContacts);
        }
    };

    //刷新UI
    private void updateUI(final List<RecentContact> contactsBlock) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int unReadMsgCount = 0;
                int unReadMsgConsultantCount = 0;//顾问未读数目
                long unReadMsgTime = 0;
                long unReadMsgConsultantTime = 0;
                String unReadMessage = "";
                String unReadConsultantMessage = "";//顾问最新一条信息
                List<RecentContact> contactsConsultantBlock = new ArrayList<>();//顾问集合
                if (contactsBlock == null) {
                    if (view != null) {
                        view.updateAllStatus(false, "", System.currentTimeMillis());
                        view.updateConsultantStatus(false, "", System.currentTimeMillis());
                    }
                    return;
                }
                //遍历所有未读消息数量，最近一条消息
                //遍历出顾问集合
                for (int i = 0; i < contactsBlock.size(); i++) {//遍历出所有未读消息数目和message
                    RecentContact recentContact = contactsBlock.get(i);
                    LogUtil.d("NimIM", "所有 ： 第" + i + "位: id = " + recentContact.getContactId() + " ; unreadCount = " + recentContact.getUnreadCount() + " ; message = " + recentContact.getContent());
                    if (info != null) {
                        for (int j = 0; j < info.size(); j++) {//遍历出顾问
                            if (info.get(j).getAccid().equals(recentContact.getContactId())) {
                                contactsConsultantBlock.add(recentContact);
                                break;
                            }
                        }
                    }
                    unReadMsgCount += recentContact.getUnreadCount();
                    if (i == 0) {
                        unReadMsgTime = recentContact.getTime();
                        unReadMessage = recentContact.getContent();
                    }
                }
                //遍历顾问未读消息数量，最近一条消息
                for (int i = 0; i < contactsConsultantBlock.size(); i++) {
                    RecentContact recentContact = contactsConsultantBlock.get(i);
                    LogUtil.d("NimIM", "顾问团 ： 第" + i + "位: id = " + recentContact.getContactId() + " ; unreadCount = " + recentContact.getUnreadCount() + " ; message = " + recentContact.getContent());
                    unReadMsgConsultantCount += recentContact.getUnreadCount();
                    if (i == 0) {
                        unReadConsultantMessage = recentContact.getContent();
                        unReadMsgConsultantTime = recentContact.getTime();
                    }
                }
                if (view != null) {
                    if (unReadMsgCount > 0) {
                        view.updateAllStatus(true, unReadMessage, unReadMsgTime == 0 ? System.currentTimeMillis() : unReadMsgTime);
                    } else {
                        view.updateAllStatus(false, unReadMessage, unReadMsgTime == 0 ? System.currentTimeMillis() : unReadMsgTime);

                    }
                    if (unReadMsgConsultantCount > 0 && contactsConsultantBlock.size() > 0) {
                        view.updateConsultantStatus(true, unReadConsultantMessage, unReadMsgConsultantTime == 0 ? System.currentTimeMillis() : unReadMsgConsultantTime);
                    } else if (unReadMsgConsultantCount <= 0 && contactsConsultantBlock.size() > 0) {
                        view.updateConsultantStatus(false, unReadConsultantMessage, unReadMsgConsultantTime == 0 ? System.currentTimeMillis() : unReadMsgConsultantTime);
                    }

                }
            }
        }).start();

    }
}
