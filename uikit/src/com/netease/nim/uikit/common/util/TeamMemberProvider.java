package com.netease.nim.uikit.common.util;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.main.OnlineStateChangeObserver;
import com.netease.nim.uikit.api.model.team.TeamMemberDataChangedObserver;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.team.model.TeamMember;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by lufeisong on 2018/1/17.
 */

public class TeamMemberProvider {
    private static final String OFFLINE = "offline";
    private static final String ONLINE = "online";
    private static TeamMemberProvider instance;

    private ArrayList<TeamMemberOnlineCallback> calls ;


    private HashMap<String,String> onLineMap;//在线人
    private HashMap<String,String> offLineMap;//离线人
    public interface TeamMemberOnlineCallback{
        void updateOnline(HashMap<String,String> onLineMap,HashMap<String,String> offLineMap);
    }
    public TeamMemberProvider() {

        calls = new ArrayList<>();
        onLineMap = new HashMap<>();
        offLineMap = new HashMap<>();
        registerOnlineStateChangeListener(true);
        registerTeamMemberDataChangedObserver(true);
    }

    public static TeamMemberProvider getInstance() {
        if(instance == null){
            instance = new TeamMemberProvider();
        }
        return instance;
    }

    public void addCallback(TeamMemberOnlineCallback callback){
        if(!calls.contains(callback)){
            calls.add(callback);
        }
    }
    public void removeCallback(TeamMemberOnlineCallback callback){
        if(calls.contains(callback)){
            calls.remove(callback);
        }
    }

    private void updateCallback(){
        LogUtil.d("NimIM","onLineMap = " + onLineMap.toString());
        for (TeamMemberOnlineCallback callback:calls){
            callback.updateOnline(onLineMap,offLineMap);
        }
    }

    public void setTeamMembers(List<TeamMember> members){
        onLineMap.clear();
        offLineMap.clear();
        for(TeamMember member : members){
            updateOnlineStatus(member.getAccount());
        }
        updateCallback();
    }
    private void updateOnlineStatus(String account){
        String detailContent = NimUIKitImpl.getOnlineStateContentProvider().getDetailDisplay(account);
        if("".equals(detailContent)){//剔除自己
            if(NimUIKit.getAccount() != null && account.equals(NimUIKit.getAccount())){//观察的账号是自己，返回在线
                onLine(account,"自己");
            }else{
                offLine(account,detailContent);
            }
        }else if("离线".equals(detailContent)){
            //暂未关注她的状态，获取离线
            offLine(account,detailContent);
        }else{
            //在线
            onLine(account,detailContent);
        }
    }
    private void removeOnlineStatus(String account){
        if(onLineMap.containsKey(account)){
            onLineMap.remove(account);
        }
        if(offLineMap.containsKey(account)){
            offLineMap.remove(account);
        }
    }
    //在线
    private void onLine(String account, String detailContent){
        if(!onLineMap.containsKey(account)){
            onLineMap.put(account,detailContent);
        }
        if(offLineMap.containsKey(account)){
            offLineMap.remove(account);
        }
    }
    //离线
    private void offLine(String account, String detailContent){
        if(onLineMap.containsKey(account)){
            onLineMap.remove(account);
        }
        if(!offLineMap.containsKey(account)){
            offLineMap.put(account,detailContent);
        }
    }
    /**
     * 通知在线状态事件变化
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
            for(String account : accounts){
                updateOnlineStatus(account);
            }
            updateCallback();
        }
    };

    private void registerTeamMemberDataChangedObserver(boolean register){
        NimUIKit.getTeamChangedObservable().registerTeamMemberDataChangedObserver(teamMemberDataChangedObserver, register);
    }
    /**
     * 群成员资料变动通知和移除群成员通知
     */
    TeamMemberDataChangedObserver teamMemberDataChangedObserver = new TeamMemberDataChangedObserver() {

        @Override
        public void onUpdateTeamMember(List<TeamMember> members) {
            for(TeamMember member : members){
                updateOnlineStatus(member.getAccount());
            }
            updateCallback();
        }

        @Override
        public void onRemoveTeamMember(List<TeamMember> members) {
            for(TeamMember member : members){
                removeOnlineStatus(member.getAccount());
            }
            updateCallback();
        }
    };
}
