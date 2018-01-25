package com.netease.nim.uikit.api.model.team;

import com.netease.nimlib.sdk.team.model.TeamMember;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lufeisong on 2018/1/25.
 */

public interface TeamMemberOnlineProvider {
    void addCallback(TeamMemberOnlineCallback callback);
    void removeCallback(TeamMemberOnlineCallback callback);
    void setTeamMembers(List<TeamMember> members);
    void clear();
    interface TeamMemberOnlineCallback{
        void updateOnline(HashMap<String,String> onLineMap, HashMap<String,String> offLineMap);
        void exitTeamByManager();
    }
}
