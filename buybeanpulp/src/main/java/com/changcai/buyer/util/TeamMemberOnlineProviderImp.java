package com.changcai.buyer.util;

import com.changcai.buyer.im.event.OnlineStateEventSubscribe;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.main.OnlineStateChangeObserver;
import com.netease.nim.uikit.api.model.team.TeamMemberDataChangedObserver;
import com.netease.nim.uikit.api.model.team.TeamMemberOnlineProvider;
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

public class TeamMemberOnlineProviderImp implements TeamMemberOnlineProvider {
    private static TeamMemberOnlineProviderImp instance;

    private boolean registerAble = false;
    private static int MODE_ONLINE = 0;
    private static int MODE_OFFLINE = 1;
    private static int MODE_REMOVELINE = 2;

    private ArrayList<TeamMemberOnlineCallback> calls ;

    private HashMap<String,String> onLineMap;//在线人
    private HashMap<String,String> offLineMap;//离线人

    public TeamMemberOnlineProviderImp() {

        calls = new ArrayList<>();
        onLineMap = new HashMap<>();
        offLineMap = new HashMap<>();

    }

    public static TeamMemberOnlineProviderImp getInstance() {
        if(instance == null){
            instance = new TeamMemberOnlineProviderImp();
        }
        return instance;
    }
    @Override
    public void addCallback(TeamMemberOnlineCallback callback){
        if(!calls.contains(callback)){
            calls.add(callback);
        }
    }
    @Override
    public void removeCallback(TeamMemberOnlineCallback callback){
        if(calls.contains(callback)){
            calls.remove(callback);
        }
    }

    private void updateCallback(){
        LogUtil.d("NimIM","onLineMap = " + onLineMap.toString());
        LogUtil.d("NimIM","offLineMap = " + offLineMap.toString());
        for (TeamMemberOnlineCallback callback:calls){
            callback.updateOnline(onLineMap,offLineMap);
        }
    }
    private void exitTeamByManager(){
        for (TeamMemberOnlineCallback callback:calls){
            callback.exitTeamByManager();
        }
    }
    @Override
    public void setTeamMembers(List<TeamMember> members){
        onLineMap.clear();
        offLineMap.clear();

        for(TeamMember member : members){
            LogUtil.d("NimIM","member = " + member.getAccount());
            updateOnlineStatus(member.getAccount());
        }
        LogUtil.d("NimIM","updateCallback setTeamMembers");
        updateCallback();
    }
    private void updateOnlineStatus(String account){
        if(!registerAble){
            registerAble = true;
            registerOnlineStateChangeListener(registerAble);
            registerTeamMemberDataChangedObserver(registerAble);
        }
        String detailContent = NimUIKitImpl.getOnlineStateContentProvider().getDetailDisplay(account);
        LogUtil.d("NimIM","test");
        if(detailContent == null){
            LogUtil.d("NimIM","updateOnlineStatus account = " + account + " online = is null " );
        }else{
            LogUtil.d("NimIM","updateOnlineStatus account = " + account + " online =  " + detailContent);
        }

        if("".equals(detailContent)){//剔除自己
            if(NimUIKit.getAccount() != null && account.equals(NimUIKit.getAccount())){//观察的账号是自己，返回在线
                changeLineData(account,"自己",MODE_ONLINE);
            }else{
                changeLineData(account,detailContent,MODE_OFFLINE);
            }
        }else if("离线".equals(detailContent)){
            //离线
            changeLineData(account,detailContent,MODE_OFFLINE);
        }else{
            //在线
            changeLineData(account,detailContent,MODE_ONLINE);
        }
    }
    private synchronized void changeLineData(String account,String detailContent,int MODE){
        if(MODE == MODE_ONLINE){
            if(!onLineMap.containsKey(account)){
                onLineMap.put(account,detailContent);
            }
            if(offLineMap.containsKey(account)){
                offLineMap.remove(account);
            }
        }else if(MODE == MODE_OFFLINE){
            if(onLineMap.containsKey(account)){
                onLineMap.remove(account);
            }
            if(!offLineMap.containsKey(account)){
                offLineMap.put(account,detailContent);
            }
        }else if(MODE == MODE_REMOVELINE){
            if(onLineMap.containsKey(account)){
                onLineMap.remove(account);
            }
            if(offLineMap.containsKey(account)){
                offLineMap.remove(account);
            }
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
            LogUtil.d("NimIM","updateCallback onlineStateChangeObserver");
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
            LogUtil.d("NimIM","updateCallback onUpdateTeamMember");
            updateCallback();
        }

        @Override
        public void onRemoveTeamMember(List<TeamMember> members) {
            boolean removeIsMe = false;
            for(TeamMember member : members){
                unSubscribeOnlineStateEvent(member.getAccount());
                changeLineData(member.getAccount(),"",MODE_REMOVELINE);
                if(NimUIKit.getAccount() != null && NimUIKit.getAccount().equals(member.getAccount())){
                    removeIsMe = true;
                    break;
                }
            }
            LogUtil.d("NimIM","updateCallback onRemoveTeamMember");
            if(removeIsMe){//自己被移出群
                exitTeamByManager();
            }else{
                updateCallback();
            }
        }
    };
    @Override
    public void clear(){
        for(String key:onLineMap.keySet()){
            unSubscribeOnlineStateEvent(key);
        }
        for(String key:offLineMap.keySet()){
            unSubscribeOnlineStateEvent(key);
        }
        calls = new ArrayList<>();
        onLineMap = new HashMap<>();
        offLineMap = new HashMap<>();
        registerAble = false;
        registerOnlineStateChangeListener(registerAble);
        registerTeamMemberDataChangedObserver(registerAble);
    }

    //取消订阅
    private void unSubscribeOnlineStateEvent(String account){
        List<String> accounts = new ArrayList<>(1);
        accounts.add(account);
        OnlineStateEventSubscribe.unSubscribeOnlineStateEvent(accounts);
    }
}
