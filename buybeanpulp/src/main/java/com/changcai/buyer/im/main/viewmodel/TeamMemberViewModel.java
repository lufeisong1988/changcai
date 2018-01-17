package com.changcai.buyer.im.main.viewmodel;

import com.netease.nimlib.sdk.team.model.TeamMember;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lufeisong on 2018/1/16.
 */

public interface TeamMemberViewModel {

    void showLoading();

    void dismissLoading();

    void queryMemberListSucceed(List<TeamMember> teamMemberList);

    void queryMemberListFail(String failStr);

    void queryMemberListError();

    void removeMemberSucceed(String member);

    void removeMemberFail(String failStr);

    void removeMemberError();

    void updateOnlineMembers(int onLineMemberNums,int totalMemberNums);

    void updateOnlineMembersAdapter(HashMap<String,String> onLineMap, HashMap<String,String> offLineMap);
}
