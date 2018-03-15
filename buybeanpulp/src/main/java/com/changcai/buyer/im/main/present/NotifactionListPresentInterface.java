package com.changcai.buyer.im.main.present;

/**
 * Created by lufeisong on 2017/12/22.
 */

public interface NotifactionListPresentInterface {
    void init(String tag);
    void getCounselorsList();
    void getTeamList();
    void getMessageList();
    void getOnline();

    void toVip();
    void toTeam(int position);
    void toAnswers();
    void onDestory();

}
