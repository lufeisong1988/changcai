package com.changcai.buyer.im.main.viewmodel;

import com.changcai.buyer.bean.GetCounselorsModel;
import com.changcai.buyer.bean.GroupDetailModel;
import com.netease.nim.uikit.business.session.TeamOnlineModel;

import java.util.List;

/**
 * Created by lufeisong on 2017/12/22.
 */

public interface NotifacitonListViewModel {
    void showLoading();

    void dismissLoading();

    void getCounselorsModelSucceed(List<GetCounselorsModel.InfoBean> info);

    void getCounselorsModelFail(String failStr);

    void getCounselorsModelError();

    void unLogin();

    void toNOTIFACTION();

    void hideNOTIFACTION();

    void showNOTIFACTION();

    void loginNimFail(String failStr);
    void loginNimSucceed();

    void updateConsultantStatus(boolean showDot, String message, long time);

    void updateAllStatus(boolean showDot, String message, long time);

    void updateTeamStatus(int unReadCount, String teamId, String message, long time);

    void joinTeam(String teamId);

    void unJoinTeam(String teamId);

    void unExistTeam();

    void joinTeamFail(String fail);

    void joinTeamError();

    void updateOnlineMembers(boolean showAble,List<TeamOnlineModel> teamOnlineModels);

    void exitTeamByManager();

    void kickOut();
    void refreshGroupList(List<GroupDetailModel> teamList);
}
