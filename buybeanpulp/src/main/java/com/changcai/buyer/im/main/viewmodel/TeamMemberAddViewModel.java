package com.changcai.buyer.im.main.viewmodel;

/**
 * Created by lufeisong on 2018/1/15.
 */

public interface TeamMemberAddViewModel {
    void showLoading();

    void dismissLoading();

    void showExistAccount(String account);

    void showUnExistAccount(String account);



    void showErrorStr(String errorStr);
}
