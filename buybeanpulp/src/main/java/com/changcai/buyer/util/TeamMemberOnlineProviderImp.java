package com.changcai.buyer.util;

import com.changcai.buyer.im.event.OnlineStateEventSubscribe;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.main.OnlineStateChangeObserver;
import com.netease.nim.uikit.api.model.team.TeamMemberDataChangedObserver;
import com.netease.nim.uikit.api.model.team.TeamMemberOnlineProvider;
import com.netease.nim.uikit.business.session.TeamOnlineModel;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.team.model.TeamMember;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by lufeisong on 2018/1/17.
 */

public class TeamMemberOnlineProviderImp implements TeamMemberOnlineProvider {
    private static TeamMemberOnlineProviderImp instance;

    private boolean registerAble = false;
    private static int MODE_ONLINE = 0;
    private static int MODE_OFFLINE = 1;
    private static int MODE_REMOVELINE = 2;

    private ArrayList<TeamMemberOnlineCallback> calls;

    private List<TeamOnlineModel> groupOnlineList;//在线人数【群组】

    public TeamMemberOnlineProviderImp() {

        calls = new ArrayList<>();
        groupOnlineList = new ArrayList<>();

    }

    public static TeamMemberOnlineProviderImp getInstance() {
        if (instance == null) {
            instance = new TeamMemberOnlineProviderImp();
        }
        return instance;
    }

    @Override
    public void addCallback(TeamMemberOnlineCallback callback) {
        if (!calls.contains(callback)) {
            calls.add(callback);
        }
    }

    @Override
    public void removeCallback(TeamMemberOnlineCallback callback) {
        if (calls.contains(callback)) {
            calls.remove(callback);
        }
    }

    private void updateCallback() {
//        LogUtil.d("onLine","updateCallback ======================== start" );
        for (TeamMemberOnlineCallback callback : calls) {
            callback.updateOnline(groupOnlineList);
        }
//        LogUtil.d("onLine","updateCallback " +groupOnlineList.toString());
//        LogUtil.d("onLine","updateCallback ======================== end" );
    }

    private void exitTeamByManager() {
        for (TeamMemberOnlineCallback callback : calls) {
            callback.exitTeamByManager();
        }
    }

    @Override
    public void setTeamMembers(List<TeamMember> members) {
        //重置，清空该群的在线人数信息
        if (members != null && members.size() > 0) {
            //查询该群的信息是否存在
            String tid = members.get(0).getTid();
            int index = indexOfTeam(tid);
            if (index > -1) {//该群存在，清空该群在线信息
                groupOnlineList.get(index).getOnLineMap().clear();
                groupOnlineList.get(index).getOffLineMap().clear();
            }
            for (TeamMember member : members) {
                updateOnlineStatus(member.getTid(), member.getAccount(), true);
            }
            updateCallback();
        }

    }
    //更新成员在线状态
    private void updateAllTeamOnlineStatus(String account) {
        for (TeamOnlineModel teamOnlineModel : groupOnlineList) {
            updateOnlineStatus(teamOnlineModel.getTid(), account, false);
        }
    }
    private void updateOnlineStatus(String tid, String account, boolean createTeamOlineModelAble) {
        if (!registerAble) {
            registerAble = true;
            registerOnlineStateChangeListener(registerAble);
            registerTeamMemberDataChangedObserver(registerAble);
        }
        String detailContent = NimUIKitImpl.getOnlineStateContentProvider().getDetailDisplay(account);
        LogUtil.d("onLine","账号: " + account + " 状态: " + detailContent);
        if ("".equals(detailContent)) {//剔除自己
            if (NimUIKit.getAccount() != null && account.equals(NimUIKit.getAccount())) {//观察的账号是自己，返回在线
                if(createTeamOlineModelAble) {
                    changeLineDataCreateTeam(tid, account, "自己", MODE_ONLINE);
                } else{
                    changeLineDataUnCreateTeam(tid, account, "自己", MODE_ONLINE);
                }
            } else {
                if(createTeamOlineModelAble){
                    changeLineDataCreateTeam(tid, account, detailContent, MODE_OFFLINE);
                }else{
                    changeLineDataUnCreateTeam(tid, account, detailContent, MODE_OFFLINE);
                }
            }
        } else if ("离线".equals(detailContent)) {
            //离线
            if(createTeamOlineModelAble){
                changeLineDataCreateTeam(tid, account, detailContent, MODE_OFFLINE);
            }else{
                changeLineDataUnCreateTeam(tid, account, detailContent, MODE_OFFLINE);
            }
        } else {
            //在线
            if(createTeamOlineModelAble){
                changeLineDataCreateTeam(tid, account, detailContent, MODE_ONLINE);
            }else {
                changeLineDataUnCreateTeam(tid, account, detailContent, MODE_ONLINE);
            }
        }
    }



    /**
     * 修改个人状态 如果群信息不存在，需要创建
     * 1，第一次初始化群里人员在线信息
     * 2.群人员发生变化
     * @param tid
     * @param account
     * @param detailContent
     * @param MODE
     */
    private synchronized void changeLineDataCreateTeam(String tid, String account, String detailContent, int MODE) {
        int index = indexOfTeam(tid);
        if (index == -1 ) {//群信息不存在，添加群信息
            TeamOnlineModel teamOnlineModel = new TeamOnlineModel();
            teamOnlineModel.setTid(tid);
            groupOnlineList.add(teamOnlineModel);
        }
        index = indexOfTeam(tid);
        changeLineData(index,account,detailContent,MODE);

    }
    /**
     * 修改个人状态 如果群信息不存在，不需要创建
     * 1.个人在线/离线 如果群里不存在此人信息，就不修改
     * @param tid
     * @param account
     * @param detailContent
     * @param MODE
     */
    private synchronized void changeLineDataUnCreateTeam(String tid, String account, String detailContent, int MODE){
        int index = indexOfTeam(tid);
        if(groupOnlineList.get(index).getOffLineMap().containsKey(account) || groupOnlineList.get(index).getOnLineMap().containsKey(account)){
            changeLineData(index,account,detailContent,MODE);
        }
    }

    private void changeLineData(int index, String account, String detailContent, int MODE){
        if (MODE == MODE_ONLINE) {
            if (!groupOnlineList.get(index).getOnLineMap().containsKey(account)) {
                groupOnlineList.get(index).getOnLineMap().put(account, detailContent);
            }
            if (groupOnlineList.get(index).getOffLineMap().containsKey(account)) {
                groupOnlineList.get(index).getOffLineMap().remove(account);
            }

        } else if (MODE == MODE_OFFLINE) {
            if (groupOnlineList.get(index).getOnLineMap().containsKey(account)) {
                groupOnlineList.get(index).getOnLineMap().remove(account);
            }
            if (!groupOnlineList.get(index).getOffLineMap().containsKey(account)) {
                groupOnlineList.get(index).getOffLineMap().put(account, detailContent);
            }
        } else if (MODE == MODE_REMOVELINE) {
            if (groupOnlineList.get(index).getOnLineMap().containsKey(account)) {
                groupOnlineList.get(index).getOnLineMap().remove(account);
            }
            if (groupOnlineList.get(index).getOffLineMap().containsKey(account)) {
                groupOnlineList.get(index).getOffLineMap().remove(account);
            }

        }
    }
    /**
     * 通知在线状态事件变化
     *
     * @param register
     */
    private void registerOnlineStateChangeListener(boolean register) {
        if (!NimUIKitImpl.enableOnlineState()) {
            return;
        }
        NimUIKitImpl.getOnlineStateChangeObservable().registerOnlineStateChangeListeners(onlineStateChangeObserver, register);
    }

    OnlineStateChangeObserver onlineStateChangeObserver = new OnlineStateChangeObserver() {
        @Override
        public void onlineStateChange(Set<String> accounts) {
            for (String account : accounts) {
                updateAllTeamOnlineStatus(account);
            }
            updateCallback();
        }
    };

    /**
     * 群成员资料变动通知和移除群成员通知
     */
    private void registerTeamMemberDataChangedObserver(boolean register) {
        NimUIKit.getTeamChangedObservable().registerTeamMemberDataChangedObserver(teamMemberDataChangedObserver, register);
    }

    TeamMemberDataChangedObserver teamMemberDataChangedObserver = new TeamMemberDataChangedObserver() {

        @Override
        public void onUpdateTeamMember(List<TeamMember> members) {
            for (TeamMember member : members) {
                updateOnlineStatus(member.getTid(), member.getAccount(), true);
            }
            updateCallback();
        }

        @Override
        public void onRemoveTeamMember(List<TeamMember> members) {
            boolean removeIsMe = false;
            for (TeamMember member : members) {
                unSubscribeOnlineStateEvent(member.getAccount());
                int index = indexOfTeam(member.getTid());
                if (index == -1) {//该群不存在
                    TeamOnlineModel teamOnlineModel = new TeamOnlineModel();
                    teamOnlineModel.setTid(member.getTid());
                    groupOnlineList.add(teamOnlineModel);
                }
                changeLineDataCreateTeam(member.getTid(), member.getAccount(), "", MODE_REMOVELINE);
                if (NimUIKit.getAccount() != null && NimUIKit.getAccount().equals(member.getAccount())) {
                    removeIsMe = true;
                    break;
                }
            }
            if (removeIsMe) {//自己被移出群
                exitTeamByManager();
            } else {
                updateCallback();
            }
        }
    };

    @Override
    public void clear() {
        for (TeamOnlineModel teamOnlineModel : groupOnlineList) {
            for (String key : teamOnlineModel.getOnLineMap().keySet()) {
                unSubscribeOnlineStateEvent(key);
            }
            for (String key : teamOnlineModel.getOffLineMap().keySet()) {
                unSubscribeOnlineStateEvent(key);
            }
        }
        calls = new ArrayList<>();
        groupOnlineList = new ArrayList<>();
        registerAble = false;
        registerOnlineStateChangeListener(registerAble);
        registerTeamMemberDataChangedObserver(registerAble);
    }

    //取消订阅
    private void unSubscribeOnlineStateEvent(String account) {
        List<String> accounts = new ArrayList<>(1);
        accounts.add(account);
        OnlineStateEventSubscribe.unSubscribeOnlineStateEvent(accounts);
    }

    /**
     * 判断群信息是否存在
     *
     * @param tid
     * @return
     */
    private int indexOfTeam(String tid) {
        int index = -1;
        for (int i = 0; i < groupOnlineList.size(); i++) {
            if (groupOnlineList.get(i).getTid().equals(tid)) {
                index = i;
                break;
            }
        }
        return index;
    }
}
