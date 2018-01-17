package com.netease.nim.uikit.common.util;

import com.netease.nim.uikit.api.model.main.OnlineStateChangeObserver;
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

    private List<TeamMember> members;//需要观察的成员

    private HashMap<String,String> onLineMap;//在线人
    private HashMap<String,String> offLineMap;//离线人
    public interface TeamMemberOnlineCallback{
        void updateOnline(HashMap<String,String> onLineMap,HashMap<String,String> offLineMap);
    }
    public TeamMemberProvider() {

        calls = new ArrayList<>();
        members = new ArrayList<>();
        onLineMap = new HashMap<>();
        offLineMap = new HashMap<>();
        registerOnlineStateChangeListener(true);
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
        for (TeamMemberOnlineCallback callback:calls){
            callback.updateOnline(onLineMap,offLineMap);
        }
    }

    public void setTeamMembers(List<TeamMember> members){
        onLineMap.clear();
        offLineMap.clear();
        this.members.clear();
        this.members.addAll(members);
        for(TeamMember member : this.members){
            updateOnlineStatus(member.getAccount());
        }
        updateCallback();
    }
    private void updateOnlineStatus(String account){
        String detailContent = NimUIKitImpl.getOnlineStateContentProvider().getDetailDisplay(account);
        if(detailContent == null || detailContent.isEmpty() || detailContent.equals("离线")){
            //暂未关注她的状态，获取离线
            if(onLineMap.containsKey(account)){
                onLineMap.remove(account);
            }
            if(!offLineMap.containsKey(account)){
                offLineMap.put(account,OFFLINE);
            }
        }else{
            //在线
            if(!onLineMap.containsKey(account)){
                onLineMap.put(account,ONLINE);
            }
            if(offLineMap.containsKey(account)){
                offLineMap.remove(account);
            }
        }
    }
    private void registerOnlineStateChangeListener(boolean register) {
        if (!NimUIKitImpl.enableOnlineState()) {
            return;
        }
        NimUIKitImpl.getOnlineStateChangeObservable().registerOnlineStateChangeListeners(onlineStateChangeObserver, register);
    }
    OnlineStateChangeObserver onlineStateChangeObserver = new OnlineStateChangeObserver() {
        @Override
        public void onlineStateChange(Set<String> accounts) {
            LogUtil.d("contactId",accounts.toString());
            for(String account : accounts){
                updateOnlineStatus(account);
            }
            updateCallback();
        }
    };
}
