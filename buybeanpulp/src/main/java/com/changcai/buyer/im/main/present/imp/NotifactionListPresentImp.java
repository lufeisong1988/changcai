package com.changcai.buyer.im.main.present.imp;

import android.os.Handler;
import android.text.TextUtils;

import com.changcai.buyer.bean.GetCounselorsModel;
import com.changcai.buyer.bean.GetImTeamsBean;
import com.changcai.buyer.bean.GroupDetailModel;
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
import com.changcai.buyer.util.TeamMemberOnlineProviderImp;
import com.changcai.buyer.util.UserDataUtil;
import com.netease.nim.uikit.business.session.TeamOnlineModel;
import com.netease.nim.uikit.common.util.MsgUtil;
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
import java.util.List;

/**
 * Created by lufeisong on 2017/12/22.
 */

public class NotifactionListPresentImp implements NotifactionListPresentInterface, NotifactionListModelImp.NotifactionListModelCallback, LoginProvider.LoginCallback, TeamMemberOnlineProviderImp.TeamMemberOnlineCallback {
    private NotifactionListModelInterface model;
    private NotifacitonListViewModel view;


    private List<GetCounselorsModel.InfoBean> counselors = new ArrayList<>();//顾问组集合
    private boolean counselorsViewResponseAble = false;//顾问组是否同步响应view

    List<GroupDetailModel> teamList = new ArrayList<>();//所有的群组



    private List<RecentContact> contactsAllBlock = new ArrayList<>();//所有人集合（P2P）
    private List<RecentContact> contactsConsultantBlock = new ArrayList<>();//顾问集合 (P2P）
    private List<RecentContact> contactsTeamBlock = new ArrayList<>();//产业联盟集合 (Team）




    public NotifactionListPresentImp(NotifacitonListViewModel view) {
        this.view = view;
        model = new NotifactionListModelImp(this);

    }

    @Override
    public void init(String tag) {
        LogUtil.d("onLine","tag = " + tag);
        LoginProvider.getInstance().addLoginCallback(this);
        TeamMemberOnlineProviderImp.getInstance().addCallback(this);
        registerRecentContact(true);
        if(UserDataUtil.isLogin()){
            //如果顾问，就展示消息列表
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



            counselorsViewResponseAble = false;

            //获取数据
            getCounselorsList();
            getMessageList();
            getOnline();
        }else{
            if(view != null){
                view.hideNOTIFACTION();
                view.updateConsultantStatus(false, "登录后查看会话消息", System.currentTimeMillis());
            }
        }
        //不需要登录就能获取，主要为了获取高级群
        getTeamList();
    }


    /**
     * 批量获取顾问信息
     */
    @Override
    public void getCounselorsList() {
        if(UserDataUtil.isLogin()){
            showLoading();
            model.getCounselorsModel();
        }
    }
    @Override
    public void getCounselorsModelSucceed(List<GetCounselorsModel.InfoBean> counselors) {
        this.counselors = counselors;
        if (view != null) {
            view.dismissLoading();
            if(counselorsViewResponseAble){
                view.getCounselorsModelSucceed(counselors);
            }
        }

        SessionHelper.setInfo(counselors);
        getMessageList();
    }

    @Override
    public void getCounselorsModelFail(String failStr) {
        if (view != null) {
            view.dismissLoading();
            view.getCounselorsModelFail(failStr);
        }
    }

    @Override
    public void getCounselorsModelError() {
        if (view != null) {
            view.dismissLoading();
            view.getCounselorsModelError();
        }
    }
    /**
     * 获取群组列表
     */
    @Override
    public void getTeamList() {
        getSpecialTeamList();
    }

    //获取个人在买豆粕的特殊群组
    void getSpecialTeamList() {
        showLoading();
        model.getImTeams();
    }

    @Override
    public void getImTeamsSucceed(final GetImTeamsBean getImTeamsBeen) {
        dismissLoading();
        if (getImTeamsBeen != null && getImTeamsBeen.getInfo() != null) {
            teamList.clear();
            for (GetImTeamsBean.InfoBean infoBean : getImTeamsBeen.getInfo()) {
                GroupDetailModel groupDetailModel = new GroupDetailModel();
                groupDetailModel.settId(infoBean.getTid());
                groupDetailModel.setCreatedId(infoBean.getTaccid());
                groupDetailModel.settName(infoBean.getTname());
                groupDetailModel.setIcon(infoBean.getTicon());
                groupDetailModel.setMsg(infoBean.getTintro());
                groupDetailModel.setTdesc(infoBean.getTdesc());
                teamList.add(groupDetailModel);
            }

            getNimTeamList();
        }
    }

    @Override
    public void getImTeamsFail(String failStr) {
        dismissLoading();
    }

    @Override
    public void getImTeamsError() {
        dismissLoading();
    }

    //获取个人在网易云信存在的群组
    void getNimTeamList() {
        if (canLoginNIM()) {//网易云讯已经登录，查询自己所加入的群组
            showLoading();
            NIMClient.getService(TeamService.class)
                    .queryTeamList()
                    .setCallback(new RequestCallback<List<Team>>() {
                        @Override
                        public void onSuccess(List<Team> teams) {
                            dismissLoading();
                            for (Team team : teams) {
                                GroupDetailModel groupDetailModel = new GroupDetailModel();
                                groupDetailModel.settName(team.getName());
                                groupDetailModel.setCreatedId(team.getCreator());
                                groupDetailModel.settId(team.getId());
                                groupDetailModel.setIcon(team.getIcon());
                                groupDetailModel.setMyTeam(team.isMyTeam());
                                groupDetailModel.setMsg(team.getIntroduce());
                                groupDetailModel.setTdesc("");
                                int index = -1;
                                for(int i = 0;i < teamList.size();i++){
                                    GroupDetailModel temp = teamList.get(i);
                                    if(team.getId().equals(temp.gettId())){
                                        groupDetailModel.setTdesc(temp.getTdesc());
                                        index = i;
                                        break;
                                    }
                                }
                                if(index > -1 && teamList.size() > index){//如果存在，就把群信息替换成从NIM获取的信息为准
                                    teamList.set(index,groupDetailModel);
                                }else{
                                    teamList.add(groupDetailModel);
                                }
                            }

                            if (view != null) {
                                view.refreshGroupList(teamList);
                            }
                            getOnline();
                            getMessageList();
                        }

                        @Override
                        public void onFailed(int i) {
                            dismissLoading();
                            if (view != null) {
                                view.refreshGroupList(teamList);
                            }
                        }

                        @Override
                        public void onException(Throwable throwable) {
                            dismissLoading();
                            if (view != null) {
                                view.refreshGroupList(teamList);
                            }
                        }
                    });
        }else{
            if (view != null) {
                view.refreshGroupList(teamList);
            }
        }
    }


    /**
     * 获取最新留言
     */
    @Override
    public void getMessageList() {
        if(canLoginNIM()){
            initMessageObserave();
            registerRecentContact(true);
        }
    }

    //主动获取未读消息和message
    private void initMessageObserave() {
        //清空消息信息数据
        contactsAllBlock.clear();
        contactsConsultantBlock.clear();
        contactsTeamBlock.clear();
        List<RecentContact> contactsBlock = NIMClient.getService(MsgService.class).queryRecentContactsBlock();
        updateMessageUI(contactsBlock);
    }

    //注册获取未读消息和message监听
    private void registerRecentContact(boolean register) {
        NIMClient.getService(MsgServiceObserve.class).observeRecentContact(messageObserver, register);
    }

    Observer<List<RecentContact>> messageObserver = new Observer<List<RecentContact>>() {
        @Override
        public void onEvent(List<RecentContact> recentContacts) {
            updateMessageUI(recentContacts);
        }
    };

    //刷新UI
    private void updateMessageUI(final List<RecentContact> contactsBlock) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    List<GetCounselorsModel.InfoBean> tmpCounselors = new ArrayList<GetCounselorsModel.InfoBean>();
                    List<RecentContact> tmpContactsBlock = new ArrayList<RecentContact>();
                    List<RecentContact> tmpContactsAllBlock = new ArrayList<>();//所有人集合（P2P）
                    List<RecentContact> tmpContactsConsultantBlock = new ArrayList<>();//顾问集合 (P2P）
                    List<RecentContact> tmpContactsTeamBlock = new ArrayList<>();//产业联盟集合 (Team）
                    tmpCounselors.addAll(counselors);
                    tmpContactsBlock.addAll(contactsBlock);
                    tmpContactsAllBlock.addAll(contactsAllBlock);
                    tmpContactsConsultantBlock.addAll(contactsConsultantBlock);
                    tmpContactsTeamBlock.addAll(contactsTeamBlock);


                    int unReadMsgCount = 0;
                    int unReadMsgConsultantCount = 0;//顾问未读数目
                    int unReadMsgTeamCount = 0;//产业联盟未读数目
                    long unReadMsgTime = 0;
                    long unReadMsgConsultantTime = 0;//顾问最新一条消息时间
                    long unReadMsgTeamTime = 0;//产业联盟最新一条消息时间
                    String unReadMessage = "";
                    String unReadConsultantMessage = "";//顾问最新一条信息
                    String unReadTeamMessage = "";//产业联盟最新一条信息

                    if (tmpContactsBlock == null) {
                        return;
                    }
                    //遍历所有未读消息数量，最近一条消息
                    //遍历出所有人集合
                    //遍历出顾问集合
                    //遍历出产业联盟集合
                    for (int i = 0; i < tmpContactsBlock.size(); i++) {//遍历出所有未读消息数目和message
                        RecentContact recentContact = tmpContactsBlock.get(i);
                        int position = -1;
                        LogUtil.d("NimIM", "最近联系 ： 第" + i + "位: contactId = " + recentContact.getContactId() + "; fromAccount = " + recentContact.getFromAccount() + " ; unreadCount = " + recentContact.getUnreadCount() + " ; message = " + recentContact.getContent() + " ext = " + (recentContact.getExtension() == null ? " null" : recentContact.getExtension().toString()) + " ; time = " + recentContact.getTime());
                        if (recentContact.getSessionType().getValue() == SessionTypeEnum.P2P.getValue()) {//P2P
                            //遍历出所有人（剔除掉顾问发的初始化信息）
                            for (int j = 0; j < tmpContactsAllBlock.size(); j++) {
                                RecentContact contactsAll = tmpContactsAllBlock.get(j);
                                if (contactsAll.getContactId().equals(recentContact.getContactId())) {
                                    position = j;
                                    break;
                                }
                            }
                            if (position != -1) {
                                if(tmpContactsAllBlock.size() > position && !MsgUtil.fliteMessage(recentContact)){
                                    tmpContactsAllBlock.set(position, recentContact);
                                }
                            } else {
                                if (!MsgUtil.fliteMessage(recentContact)) {
                                    tmpContactsAllBlock.add(recentContact);
                                }
                            }

                            position = -1;
                            //遍历出顾问
                            if (tmpCounselors != null) {
                                for (int j = 0; j < tmpCounselors.size(); j++) {
                                    if (tmpCounselors.get(j).getAccid().equals(recentContact.getContactId())) {
                                        for (int n = 0; n < tmpContactsConsultantBlock.size(); n++) {
                                            if (tmpContactsConsultantBlock.get(n).getContactId().equals(recentContact.getContactId())) {
                                                position = n;
                                                break;
                                            }
                                        }
                                        if (position != -1) {
                                            if(tmpContactsConsultantBlock.size() > position){
                                                tmpContactsConsultantBlock.set(position, recentContact);
                                            }
                                        } else {
                                            tmpContactsConsultantBlock.add(recentContact);
                                        }
                                        break;
                                    }
                                }
                            }
                            position = -1;
                        } else if (recentContact.getSessionType().getValue() == SessionTypeEnum.Team.getValue()) {//Team
                            for (int j = 0; j < tmpContactsTeamBlock.size(); j++) {
                                if (tmpContactsTeamBlock.get(j).getContactId().equals(recentContact.getContactId())) {
                                    position = j;
                                    break;
                                }
                            }
                            Team team = NIMClient.getService(TeamService.class).queryTeamBlock(recentContact.getContactId());
                            if (team != null) {
                                if (position != -1 ) {
                                    if (team.isMyTeam() && tmpContactsTeamBlock.size() > position) {
                                        tmpContactsTeamBlock.set(position, recentContact);
                                    }
                                } else {
                                    if (team.isMyTeam()) {
                                        tmpContactsTeamBlock.add(recentContact);
                                    }
                                }
                            }

                            position = -1;
                        }
                    }

                    //重置数据
                    contactsAllBlock.clear();
                    contactsConsultantBlock.clear();
                    contactsTeamBlock.clear();
                    contactsAllBlock.addAll(tmpContactsAllBlock);
                    contactsConsultantBlock.addAll(tmpContactsConsultantBlock);
                    contactsTeamBlock.addAll(tmpContactsTeamBlock);

                    //遍历所有人未读消息数量，最近一条消息
                    for (int i = 0; i < tmpContactsAllBlock.size(); i++) {
                        RecentContact recentContact = tmpContactsAllBlock.get(i);
                        LogUtil.d("NimIM", "所有人 ： 第" + i + "位: id = " + recentContact.getContactId() + " ; unreadCount = " + recentContact.getUnreadCount() + " ; message = " + recentContact.getContent());
                        unReadMsgCount += recentContact.getUnreadCount();
                        if (i == 0) {
                            unReadMsgTime = recentContact.getTime();
                            unReadMessage = recentContact.getContent();
                        }
                    }
                    //遍历顾问未读消息数量，最近一条消息
                    for (int i = 0; i < tmpContactsConsultantBlock.size(); i++) {
                        RecentContact recentContact = tmpContactsConsultantBlock.get(i);
                        unReadMsgConsultantCount += recentContact.getUnreadCount();
                        if (i == 0) {
                            unReadConsultantMessage = recentContact.getContent();
                            unReadMsgConsultantTime = recentContact.getTime();
                        }
                    }
                    if (view != null) {
                        //刷新所有消息
                        view.updateAllStatus(unReadMsgCount > 0 ? true : false, unReadMessage, unReadMsgTime == 0 ? System.currentTimeMillis() : unReadMsgTime);
                        //刷新顾问
                        if (unReadMsgConsultantCount > 0 && tmpContactsConsultantBlock.size() > 0) {
                            view.updateConsultantStatus(true, unReadConsultantMessage, unReadMsgConsultantTime == 0 ? System.currentTimeMillis() : unReadMsgConsultantTime);
                        } else if (unReadMsgConsultantCount <= 0 && tmpContactsConsultantBlock.size() > 0) {
                            view.updateConsultantStatus(false, unReadConsultantMessage, unReadMsgConsultantTime == 0 ? System.currentTimeMillis() : unReadMsgConsultantTime);
                        }
                        //刷新所有群
                        for(int i = 0 ;i < tmpContactsTeamBlock.size();i++){
                            RecentContact tmp = tmpContactsTeamBlock.get(i);
                            unReadMsgTeamCount = tmp.getUnreadCount();
                            unReadMsgTeamTime = tmp.getTime();
                            String tid = tmp.getContactId();
                            unReadMessage = tmp.getContent();
                            view.updateTeamStatus(unReadMsgTeamCount,tid, unReadMessage, unReadMsgTeamTime == 0 ? System.currentTimeMillis() : unReadMsgTeamTime);
                        }
                    }
                }catch (Exception e){

                }

            }
        }).start();

    }

    /**
     * 获取在线人数
     */
    @Override
    public void getOnline() {
        //遍历所有群，获取每个群的成员，并查询每个群的在线人数
        for(GroupDetailModel groupDetailModel:teamList){
            getTeamMember(groupDetailModel.gettId());
        }
    }

    //获取每个群的人数
    void getTeamMember(final String tid) {
        NIMClient.getService(TeamService.class)
                .queryMemberList(tid)
                .setCallback(new RequestCallback<List<TeamMember>>() {
                    @Override
                    public void onSuccess(List<TeamMember> teamMembers) {
                        if (teamMembers != null && teamMembers.size() > 0) {
                            //更新在线人数
                            TeamMemberOnlineProviderImp.getInstance().setTeamMembers(teamMembers);
                        }
                    }

                    @Override
                    public void onFailed(int i) {
                    }

                    @Override
                    public void onException(Throwable throwable) {
                    }
                });
    }
    //更新在线人数 回调
    @Override
    public void updateOnline(List<TeamOnlineModel> teamOnlineModels) {
        if (view != null) {
            view.updateOnlineMembers(true, teamOnlineModels);
        }
    }
    void showLoading() {
        if (view != null) {
            view.showLoading();
        }
    }

    void dismissLoading() {
        if (view != null) {
            view.dismissLoading();
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
    @Override
    public void onDestory() {
        LogUtil.d("onLine","view == null");
        view = null;
        LoginProvider.getInstance().removeLoginCallback(this);
        TeamMemberOnlineProviderImp.getInstance().removeCallback(this);
        registerRecentContact(false);
    }

    /**
     * 跳转会员顾问
     */
    @Override
    public void toVip() {
        counselorsViewResponseAble = true;
        if (checkLogin()) {
            getCounselorsList();
        }
    }

    /**
     * 跳转产业联盟
     */
    @Override
    public void toTeam(int position) {
        if (checkLogin()) {
            if(position > -1 && teamList.size() > position){
                NIMClient.getService(TeamService.class).queryTeam(teamList.get(position).gettId())
                        .setCallback(new RequestCallback<Team>() {
                            @Override
                            public void onSuccess(Team team) {
                                if(team != null && team.isMyTeam()){
                                    if(view != null){
                                        view.joinTeam(team.getId());
                                    }
                                }else{
                                    if(view != null){
                                        view.unJoinTeam(team.getId());
                                    }
                                }
                            }

                            @Override
                            public void onFailed(int i) {
                                if(view != null){
                                    view.joinTeamError();
                                }
                            }

                            @Override
                            public void onException(Throwable throwable) {
                                if(view != null){
                                    view.joinTeamError();
                                }
                            }
                        });
            }
        }
    }

    /**
     * 跳转消息回复
     */
    @Override
    public void toAnswers() {
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

    /**
     * 登录网易云信结果
     */
    @Override
    public void nimLoginSucceed() {
        if (view != null) {
            view.dismissLoading();
            view.loginNimSucceed();
        }
//        LogUtil.i("onLine","nimLoginSucceed ");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
             init("nimLoginSucceed init");
            }
        },1200);

    }
    @Override
    public void nimLoginFail(String failStr) {
        if (view != null) {
            view.dismissLoading();
            view.loginNimFail(failStr);
        }
    }

    /**
     * 被t下线
     */
    @Override
    public void nimKicked() {

        if(view != null){
            view.kickOut();
        }
    }








    /**
     * 被管理员清出群
     */
    @Override
    public void exitTeamByManager() {
        if (view != null) {
            view.exitTeamByManager();
        }
    }



}
