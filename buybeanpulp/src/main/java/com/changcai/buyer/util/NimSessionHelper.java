package com.changcai.buyer.util;

import com.changcai.buyer.bean.UserInfo;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.im.DemoCache;
import com.changcai.buyer.im.config.preference.Preferences;
import com.changcai.buyer.im.config.preference.UserPreferences;
import com.changcai.buyer.im.provider.LoginProvider;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.common.util.TeamMemberProvider;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.LoginInfo;

/**
 * Created by lufeisong on 2017/12/25.
 */

public class NimSessionHelper {
    private static NimSessionHelper instance;

    public NimSessionHelper() {
    }

    public static NimSessionHelper getInstance() {
        if(instance == null){
            instance = new NimSessionHelper();
        }
        return instance;
    }
    public void login(){
        if (UserDataUtil.isLogin()){
            UserInfo userInfo = SPUtil.getObjectFromShare(Constants.KEY_USER_INFO);
            LoginInfo loginInfo = new LoginInfo(userInfo.getAccount(),userInfo.getImToken());
            NimUIKit.login(loginInfo, new RequestCallback<LoginInfo>() {
                @Override
                public void onSuccess(LoginInfo loginInfo) {
                    LogUtil.d("NimIM","login onSuccess");
                    DemoCache.setAccount(loginInfo.getAccount());
                    saveLoginInfo(loginInfo.getAccount(), loginInfo.getToken());
                    LoginProvider.getInstance().updateLoginCallbackSucceed();
                    initNotificationConfig();
//                    registerOnlineStatus(true);
                }

                @Override
                public void onFailed(int i) {
                    LogUtil.d("NimIM","login onFailed i = " + i);
                    LoginProvider.getInstance().updateLoginCallbackFail("请求顾问数据失败（"+ i+ "）,请稍后重试");
                }

                @Override
                public void onException(Throwable throwable) {
                    LoginProvider.getInstance().updateLoginCallbackFail("请求顾问数据失败,请稍后重试");
                    LogUtil.d("NimIM","login onException throwable = " + throwable.toString());
                }
            });
        }

    }
    public void loginOut()
    {
        // 清理缓存&注销监听&清除状态
        NimUIKit.logout();
        DemoCache.clear();
        clearLoginInfo();
//        registerOnlineStatus(false);
        TeamMemberProvider.getInstance().clear();
    }
    private void saveLoginInfo(final String account, final String token) {
        Preferences.saveUserAccount(account);
        Preferences.saveUserToken(token);
    }
    /**
     * 注销
     */
    private void clearLoginInfo() {
        NIMClient.getService(AuthService.class).logout();
        Preferences.saveUserToken("");
    }

    /**
     * 注册用户状态
     * @param register
     */
    public void registerOnlineStatus(boolean register){
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, register);
    }
    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {

        @Override
        public void onEvent(StatusCode code) {
            LogUtil.d("NimIM","statusCode = " + code.getValue());
        }
    };

    private void initNotificationConfig() {
        // 初始化消息提醒
        NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

        // 加载状态栏配置
        StatusBarNotificationConfig statusBarNotificationConfig = UserPreferences.getStatusConfig();
        if (statusBarNotificationConfig == null) {
            statusBarNotificationConfig = DemoCache.getNotificationConfig();
            UserPreferences.setStatusConfig(statusBarNotificationConfig);
        }
        // 更新配置
        NIMClient.updateStatusBarNotificationConfig(statusBarNotificationConfig);
    }

}
