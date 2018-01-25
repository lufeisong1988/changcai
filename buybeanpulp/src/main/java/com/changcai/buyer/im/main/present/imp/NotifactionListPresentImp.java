package com.changcai.buyer.im.main.present.imp;

import android.text.TextUtils;

import com.changcai.buyer.bean.GetCounselorsModel;
import com.changcai.buyer.bean.GetImTeamsBean;
import com.changcai.buyer.bean.UserInfo;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.im.config.preference.Preferences;
import com.changcai.buyer.im.main.model.NotifactionListModelInterface;
import com.changcai.buyer.im.main.model.imp.NotifactionListModelImp;
import com.changcai.buyer.im.main.present.NotifactionListPresentInterface;
import com.changcai.buyer.im.main.viewmodel.NotifacitonListViewModel;
import com.changcai.buyer.im.provider.LoginProvider;
import com.changcai.buyer.im.session.SessionHelper;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.util.NimSessionHelper;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.UserDataUtil;
import com.netease.nim.uikit.common.util.MsgUtil;
import com.changcai.buyer.util.TeamMemberOnlineProviderImp;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lufeisong on 2017/12/22.
 */

public class NotifactionListPresentImp implements NotifactionListPresentInterface, NotifactionListModelImp.NotifactionListModelCallback, LoginProvider.LoginCallback ,TeamMemberOnlineProviderImp.TeamMemberOnlineCallback
{
    private NotifactionListModelInterface model;
    private NotifacitonListViewModel view;
    private int position = 0;
    private static final int VIP = 0;
    private static final int TEAM = 1;
    private static final int NOTIFACTION = 2;
    private boolean isSyncGetImItemAble = false;//获取群组，是否需要响应view
    private List<GetCounselorsModel.InfoBean> info;
    private boolean syncAble = false;//是否同步请求

    private String tid;

    private List<RecentContact> contactsAllBlock = new ArrayList<>();//所有人集合（P2P）
    private List<RecentContact> contactsConsultantBlock = new ArrayList<>();//顾问集合 (P2P）
    private List<RecentContact> contactsTeamBlock = new ArrayList<>();//产业联盟集合 (Team）

    public NotifactionListPresentImp(NotifacitonListViewModel view) {
        this.view = view;
        model = new NotifactionListModelImp(this);
        LoginProvider.getInstance().addLoginCallback(this);
        TeamMemberOnlineProviderImp.getInstance().addCallback(this);
        registerRecentContact(true);

    }

    @Override
    public void init() {
        contactsAllBlock.clear();
        contactsConsultantBlock .clear();
        contactsTeamBlock.clear();
        if (!UserDataUtil.isLogin()) {
            if (view != null) {
                view.hideNOTIFACTION();
                view.updateConsultantStatus(false, "登录后查看会话消息", System.currentTimeMillis());
                view.updateTeamStatus(false,"登录后查看会话消息",0);
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
            asyncUpdateOnlineMembers();
            getCounselorsModel(false);
        }
//        initMessageObserave();
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
     * 跳转产业联盟
     */
    @Override
    public void toTeam() {
        isSyncGetImItemAble = true;
        position = TEAM;
        if (checkLogin()) {
            getImTeams();
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
     * 更新产业联盟在线人数(异步)
     */
    private void asyncUpdateOnlineMembers() {
        isSyncGetImItemAble = false;
        model.getImTeams();
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
        LoginProvider.getInstance().removeLoginCallback(this);
        TeamMemberOnlineProviderImp.getInstance().removeCallback(this);
        registerRecentContact(false);
    }

    private void getImTeams() {
        if (view != null) {
            view.showLoading();
        }
        model.getImTeams();
    }

    //批量获取顾问信息 回调
    @Override
    public void getCounselorsModelSucceed(List<GetCounselorsModel.InfoBean> info) {
        this.info = info;
        if (view != null && syncAble) {
            view.dismissLoading();
            view.getCounselorsModelSucceed(info);
        }
        SessionHelper.setInfo(info);
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

    @Override
    public void getImTeamsSucceed(final GetImTeamsBean getImTeamsBeen) {//暂时只有一个群
        if (view != null) {
            view.dismissLoading();
            if(getImTeamsBeen != null && getImTeamsBeen.getInfo() != null &&getImTeamsBeen.getInfo().size()> 0){
                tid = getImTeamsBeen.getInfo().get(0).getTid();
                if(isSyncGetImItemAble){//跳转产业联盟
                    NIMClient.getService(TeamService.class)
                            .queryTeamList()
                            .setCallback(new RequestCallback<List<Team>>() {
                                @Override
                                public void onSuccess(List<Team> teams) {

                                    boolean joinAble = false;
                                    for(Team team :teams){
                                        if(team.getId().equals(tid)){
                                            joinAble = true;
                                            break;
                                        }

                                    }
                                    if(view != null){
                                        if(joinAble){
                                            view.joinTeam(tid);
                                        }else{
                                            view.unJoinTeam();
                                        }
                                    }

                                }

                                @Override
                                public void onFailed(int i) {
                                    if(view != null){
                                        view.joinTeamFail("进群失败 : " + i);
                                    }
                                }

                                @Override
                                public void onException(Throwable throwable) {
                                    if(view != null){
                                        view.joinTeamError();
                                    }
                                }
                            });
                }else{//获取产业联盟在线人数
                    NIMClient.getService(TeamService.class)
                            .queryMemberList(tid)
                            .setCallback(new RequestCallback<List<TeamMember>>() {
                                @Override
                                public void onSuccess(List<TeamMember> teamMembers) {
                                    LogUtil.d("NimIM","queryMemberList.asy teamMembers.size = " + teamMembers.size());
                                    if(teamMembers != null && teamMembers.size() > 0){
                                        TeamMemberOnlineProviderImp.getInstance().setTeamMembers(teamMembers);
                                    }

                                }

                                @Override
                                public void onFailed(int i) {
                                    LogUtil.d("NimIM","queryMemberList.asy Failed = " + i);
                                }

                                @Override
                                public void onException(Throwable throwable) {
                                    LogUtil.d("NimIM","queryMemberList.asy Exception = " + throwable.toString());
                                }
                            });
                }

            }else{//买豆粕 返回的群不存在
                if(view != null && isSyncGetImItemAble){
                    view.unExistTeam();
                }
            }

        }
    }

    @Override
    public void getImTeamsFail(String failStr) {
        if (view != null && isSyncGetImItemAble) {
            view.dismissLoading();
            view.joinTeamFail(failStr);
        }
    }

    @Override
    public void getImTeamsError() {
        if (view != null && isSyncGetImItemAble) {
            view.dismissLoading();
            view.joinTeamError();
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
        } else if(position == TEAM){
            toTeam();
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
                int unReadMsgTeamCount = 0;//产业联盟未读数目
                long unReadMsgTime = 0;
                long unReadMsgConsultantTime = 0;//顾问最新一条消息时间
                long unReadMsgTeamTime = 0;//产业联盟最新一条消息时间
                String unReadMessage = "";
                String unReadConsultantMessage = "";//顾问最新一条信息
                String unReadTeamMessage = "";//产业联盟最新一条信息

                if (contactsBlock == null) {
                    if (view != null) {
                        view.updateAllStatus(false, "", System.currentTimeMillis());
                        view.updateConsultantStatus(false, UserDataUtil.isLogin() ? "" : "登录后查看会话消息", System.currentTimeMillis());
                        view.updateTeamStatus(false,"登录后查看会话消息",0);
                        view.updateOnlineMembers(false,0,0);
                    }
                    return;
                }
                //遍历所有未读消息数量，最近一条消息
                //遍历出所有人集合
                //遍历出顾问集合
                //遍历出产业联盟集合
                for (int i = 0; i < contactsBlock.size(); i++) {//遍历出所有未读消息数目和message
                    RecentContact recentContact = contactsBlock.get(i);
                    int position = -1;
                    LogUtil.d("NimIM", "最近联系 ： 第" + i + "位: contactId = " + recentContact.getContactId() + "; fromAccount = " + recentContact.getFromAccount() + " ; unreadCount = " + recentContact.getUnreadCount() + " ; message = " + recentContact.getContent() + " ext = "+ (recentContact.getExtension() == null ? " null" :recentContact.getExtension() .toString() ) + " ; time = " + recentContact.getTime());
                    if(recentContact.getSessionType().getValue() == SessionTypeEnum.P2P.getValue()){//P2P

                        //遍历出所有人（剔除掉顾问发的初始化信息）
                        for(int j = 0; j < contactsAllBlock.size();j++){
                            RecentContact contactsAll =  contactsAllBlock.get(j);
                            if(contactsAll.getContactId().equals(recentContact.getContactId())){
                                position = j;
                                break;
                            }
                        }
                        if(position != -1 ){
                            contactsAllBlock.remove(position);
                            if(!MsgUtil.fliteMessage(recentContact)){
                                contactsAllBlock.add(position,recentContact);
                            }
                        }else {
                            if(!MsgUtil.fliteMessage(recentContact)){
                                contactsAllBlock.add(recentContact);
                            }
                        }

                        position = -1;
                        //遍历出顾问
                        if (info != null) {
                            for (int j = 0; j < info.size(); j++) {
                                if (info.get(j).getAccid().equals(recentContact.getContactId())) {
                                    for(int n = 0; n < contactsConsultantBlock.size();n++){
                                        if(contactsConsultantBlock.get(n).getContactId().equals(recentContact.getContactId())){
                                            position = n;
                                            break;
                                        }
                                    }
                                    if(position != -1){
                                        contactsConsultantBlock.remove(position);
                                        contactsConsultantBlock.add(position,recentContact);
                                    }else{
                                        contactsConsultantBlock.add(recentContact);
                                    }
                                    break;
                                }
                            }
                        }
                        position = -1;
                    }else if(recentContact.getSessionType().getValue() == SessionTypeEnum.Team.getValue()){//Team
                        for(int j = 0; j < contactsTeamBlock.size();j++){
                            if(contactsTeamBlock.get(j).getContactId().equals(recentContact.getContactId())){
                                position = j;
                                break;
                            }
                        }
                        Team team = NIMClient.getService(TeamService.class).queryTeamBlock(recentContact.getContactId());
                        if(position != -1){
                            contactsTeamBlock.remove(position);
                            if(team.isMyTeam()){
                                contactsTeamBlock.add(position,recentContact);
                            }
                        }else{
                            if(team.isMyTeam()){
                                contactsTeamBlock.add(recentContact);
                            }
                        }
                        position = -1;
                    }
                }
                //遍历所有人未读消息数量，最近一条消息
                for(int i = 0;i < contactsAllBlock.size();i++ ){
                    RecentContact recentContact = contactsAllBlock.get(i);
                    LogUtil.d("NimIM", "所有人 ： 第" + i + "位: id = " + recentContact.getContactId() + " ; unreadCount = " + recentContact.getUnreadCount() + " ; message = " + recentContact.getContent());
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
                //遍历产业联盟未读消息数量，最近一条消息
                for (int i = 0; i < contactsTeamBlock.size(); i++) {
                    RecentContact recentContact = contactsTeamBlock.get(i);
                    LogUtil.d("NimIM", "产业联盟 ： 第" + i + "位: id = " + recentContact.getContactId() + " ; unreadCount = " + recentContact.getUnreadCount() + " ; message = " + recentContact.getContent());
                    unReadMsgTeamCount += recentContact.getUnreadCount();
                    if (i == 0) {
                        unReadTeamMessage = recentContact.getContent();
                        unReadMsgTeamTime = recentContact.getTime();
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
                    if(unReadMsgTeamCount > 0){
                        view.updateTeamStatus(true, "联盟成员交流群", unReadMsgTeamTime == 0 ? System.currentTimeMillis() : unReadMsgTeamTime);
                    } else {
                        view.updateTeamStatus(false, "联盟成员交流群", unReadMsgTeamTime == 0 ? System.currentTimeMillis() : unReadMsgTeamTime);

                    }
                }
            }
        }).start();

    }
    /**
     * 更新在线人数 回调
     */
    @Override
    public void updateOnline(HashMap<String,String> onLineMap, HashMap<String,String> offLineMap) {
        if(view != null){
            LogUtil.d("NimIM","Notifaction updateOnline :" + onLineMap.size() + "/" + (onLineMap.size() + offLineMap.size()));
            view.updateOnlineMembers(true,onLineMap.size(),onLineMap.size() + offLineMap.size());
        }
    }

    /**
     * 被管理员清出群
     */
    @Override
    public void exitTeamByManager() {
        if(view != null){
            view.exitTeamByManager();
        }
    }


}
