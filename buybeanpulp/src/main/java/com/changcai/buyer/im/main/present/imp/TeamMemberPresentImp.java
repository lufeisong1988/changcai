package com.changcai.buyer.im.main.present.imp;

import android.content.Context;

import com.changcai.buyer.im.main.present.TeamMemberPresent;
import com.changcai.buyer.im.main.viewmodel.TeamMemberViewModel;
import com.changcai.buyer.im.session.SessionHelper;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.util.TeamMemberOnlineProviderImp;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamMemberType;
import com.netease.nimlib.sdk.team.model.TeamMember;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lufeisong on 2018/1/16.
 */

public class TeamMemberPresentImp implements TeamMemberPresent,TeamMemberOnlineProviderImp.TeamMemberOnlineCallback {
    private Context context;
    private String teamId;
    private TeamMemberViewModel view;

    public TeamMemberPresentImp(Context context, String teamId, TeamMemberViewModel view) {
        this.context = context;
        this.teamId = teamId;
        this.view = view;
        TeamMemberOnlineProviderImp.getInstance().addCallback(this);
    }

    @Override
    public void queryMemberList() {
        if(view != null){
            view.showLoading();
        }
        NIMClient.getService(TeamService.class)
                .queryMemberList(teamId)
                .setCallback(new RequestCallback<List<TeamMember>>() {
                    @Override
                    public void onSuccess(List<TeamMember> teamMembers) {
                        LogUtil.d("NimIM","teamMembers.size = " + teamMembers.size());


                            if(teamMembers != null && teamMembers.size() > 0){
                                if(view != null){
                                    view.dismissLoading();
                                    view.queryMemberListSucceed(teamMembers);
                                }
                                //更新管理员数据
                                List<String> manager = new ArrayList<String>();
                                for(TeamMember teamMember:teamMembers){
                                    if(teamMember.getType().getValue() == TeamMemberType.Owner.getValue() || teamMember.getType().getValue() == TeamMemberType.Manager.getValue()){
                                        manager.add(teamMember.getAccount());
                                    }
                                }
                                SessionHelper.setMamager(manager);
                                //更新在线人数
                                TeamMemberOnlineProviderImp.getInstance().setTeamMembers(teamMembers);
                            }else{
                                if(view != null){
                                    view.dismissLoading();
                                    view.queryMemberListFail("获取群成员列表失败" );
                                }
                            }


                    }

                    @Override
                    public void onFailed(int i) {
                        if(view != null){
                            view.dismissLoading();
                            view.queryMemberListFail("获取群成员列表失败 : " + i);
                        }
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        if(view != null){
                            view.dismissLoading();
                            view.queryMemberListError();
                        }
                    }
                });
    }

    @Override
    public void removeMember(final String member) {
        if(view != null){
            view.showLoading();
        }
        NIMClient.getService(TeamService.class)
                .removeMember(teamId, member)
                .setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        LogUtil.d("NimIM","removeMember succeed");
                        if(view != null){
                            view.dismissLoading();
                            view.removeMemberSucceed(member);
                        }
                    }

                    @Override
                    public void onFailed(int i) {
                        if(view != null){
                            view.dismissLoading();
                            view.removeMemberFail("删除群成员失败 : " + i);
                        }
                    }

                    @Override
                    public void onException(Throwable throwable) {
                        if(view != null){
                            view.dismissLoading();
                            view.removeMemberError();
                        }
                    }
                });
    }

    @Override
    public void onDestory() {
        view = null;
        TeamMemberOnlineProviderImp.getInstance().removeCallback(this);
    }

    /**
     * 更新在线人数 回调
     */
    @Override
    public void updateOnline(HashMap<String,String> onLineMap, HashMap<String,String> offLineMap) {
        if(view != null ){
            LogUtil.d("NimIM","TeamMember updateOnline :" + onLineMap.size() + "/" + (onLineMap.size() + offLineMap.size()));
            view.updateOnlineMembers(onLineMap.size(),onLineMap.size() + offLineMap.size());
            view.updateOnlineMembersAdapter(onLineMap,offLineMap);
        }
    }

    @Override
    public void exitTeamByManager() {
        if(view != null){
            view.exitTeamByManager();
        }
    }

}
