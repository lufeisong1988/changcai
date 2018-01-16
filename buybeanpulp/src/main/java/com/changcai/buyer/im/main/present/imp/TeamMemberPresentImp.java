package com.changcai.buyer.im.main.present.imp;

import com.changcai.buyer.im.main.present.TeamMemberPresent;
import com.changcai.buyer.im.main.viewmodel.TeamMemberViewModel;
import com.changcai.buyer.util.LogUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.TeamMember;

import java.util.List;

/**
 * Created by lufeisong on 2018/1/16.
 */

public class TeamMemberPresentImp implements TeamMemberPresent {
    private String teamId;
    private TeamMemberViewModel view;

    public TeamMemberPresentImp(String teamId,TeamMemberViewModel view) {
        this.teamId = teamId;
        this.view = view;
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
                        if(view != null){
                            view.dismissLoading();
                            if(teamMembers != null){
                                view.queryMemberListSucceed(teamMembers);
                            }else{
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
    }
}
