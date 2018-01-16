package com.changcai.buyer.im.main.present.imp;

import com.changcai.buyer.im.main.present.TeamMemberAddPresent;
import com.changcai.buyer.im.main.viewmodel.TeamMemberAddViewModel;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lufeisong on 2018/1/15.
 */

public class TeamMemberAddPresentImp implements TeamMemberAddPresent {
    private TeamMemberAddViewModel view;

    public TeamMemberAddPresentImp(TeamMemberAddViewModel view) {
        this.view = view;

    }

    @Override
    public void fetchUserInfo(final String account) {
        if(view != null ){
            view.showLoading();
        }
        List<String> accounts = new ArrayList<>();
        accounts.add(account);
        NIMClient.getService(UserService.class).fetchUserInfo(accounts).setCallback(new RequestCallback<List<NimUserInfo>>() {
            @Override
            public void onSuccess(List<NimUserInfo> userInfos) {
                if(userInfos != null && userInfos.size() > 0){
                    if(view != null){
                        view.dismissLoading();
                        view.showExistAccount(account);
                    }
                }else{
                    if(view != null){
                        view.dismissLoading();
                        view.showUnExistAccount(account);
                    }
                }
            }

            @Override
            public void onFailed(int i) {
                if(view != null){
                    view.dismissLoading();
                    view.showErrorStr("获取用户信息失败 : " + i);
                }
            }

            @Override
            public void onException(Throwable throwable) {
                if(view != null){
                    view.dismissLoading();
                    view.showErrorStr("请求数据失败");
                }
            }
        });
    }

    @Override
    public void onDestory() {
        view = null;
    }


}
