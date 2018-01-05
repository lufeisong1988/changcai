package com.changcai.buyer.util;

import android.text.TextUtils;

import com.changcai.buyer.CommonApplication;
import com.changcai.buyer.bean.UserInfo;
import com.changcai.buyer.common.Constants;

/**
 * Created by liuxingwei on 2017/1/24.
 * <p>
 * 用户相关信息
 */

public class UserDataUtil {


    private static UserInfo u;

    public static boolean isLogin() {
        return SPUtil.getBoolean(Constants.KEY_IS_LOGIN);
    }

    public static String userMobile(){
        if (u == null) {
            u = SPUtil.getObjectFromShare(Constants.KEY_USER_INFO);
        }
        return u.getMobile();
    }
    public static String userEnterType(){
        if (u == null){
            u = SPUtil.getObjectFromShare(Constants.KEY_USER_INFO);
        }
        return  u.getEnterType();
    }

    public static String userEnterStatus(){
        if ( u==null){
            u = SPUtil.getObjectFromShare(Constants.KEY_USER_INFO);
        }
        return  u.getEnterStatus();
    }
    public static String userGradeName(){
        if (u == null) {
            u = SPUtil.getObjectFromShare(Constants.KEY_USER_INFO);
        }
        return  u.getGradeName();
    }
    /**
     * 极光推动绑定
     *
     * @return
     */
    public static boolean getPushBind() {
        return SPUtil.getBoolean("push_bind");
    }

    public static void setPushBind(boolean isBind) {
        SPUtil.saveboolean("push_bind", isBind);
    }

    /**
     * 完全认证
     *
     * @return
     */
    public static boolean isEntirelyAuth() {
        if (u == null) {
            u = SPUtil.getObjectFromShare(Constants.KEY_USER_INFO);
        }
        return "SUCCESS".equalsIgnoreCase(u.getEnterStatus()) && "SUCCESS".equalsIgnoreCase(u.getBankSignStatus()) && Boolean.parseBoolean(u.getPayPassword()) && Boolean.parseBoolean(u.getIsContracted()) && (!TextUtils.isEmpty(u.getBuyerInformation()) || !TextUtils.isEmpty(u.getSellerInformation()));
    }

    /**
     * 管理员
     *
     * @return
     */
    public static boolean isAdmin() {
        if (u == null) {
            u = SPUtil.getObjectFromShare(Constants.KEY_USER_INFO);
        }
        return

                u.getType().equalsIgnoreCase("admin");
    }

    /**
     * 财务员
     *
     * @return
     */
    public static boolean isFinance() {
        if (u == null) {
            u = SPUtil.getObjectFromShare(Constants.KEY_USER_INFO);
        }
        return u.getType().equalsIgnoreCase("finance");
    }

    /**
     * 交易员
     *
     * @return
     */
    public static boolean isTransaction() {
        if (u == null) {
            u = SPUtil.getObjectFromShare(Constants.KEY_USER_INFO);
        }
        return u.getType().equalsIgnoreCase("transaction");
    }

    /**
     * 业务员
     *
     * @return
     */
    public static boolean isBusiness() {
        if (u == null) {
            u = SPUtil.getObjectFromShare(Constants.KEY_USER_INFO);
        }
        return u.getType().equalsIgnoreCase("business");
    }

    /**
     * 是否认证
     *
     * @return
     */
    public static boolean isAuth() {

        return "success".equalsIgnoreCase((SPUtil.getString(Constants.KEY_IS_AUTH)));
    }

    /**
     * 是否认证中
     *
     * @return
     */
    public static boolean isProcessAuth() {
        return "PROCESS".equalsIgnoreCase((SPUtil.getString(Constants.KEY_IS_AUTH)));
    }


    /**
     * 是否工厂
     *
     * @return
     */
    public static boolean isFactory() {
        return SPUtil.getBoolean(Constants.KEY_IS_FACTORY);
    }

    /**
     * 是否买家
     *
     * @return
     */
    public static boolean isBuyer() {
        return SPUtil.getBoolean(Constants.KEY_IS_BUYER);
    }


    public static String getTokenId(){return SPUtil.getString(Constants.KEY_TOKEN_ID);}

    /**
     * 获取用户等级
     */
    public static String getGrade(){
        if (u == null) {
            u = SPUtil.getObjectFromShare(Constants.KEY_USER_INFO);
        }
        return u.getGrade();
    }

    /**
     * 注销登录清除用户数据
     */
    public static void clearUserData() {
        SPUtil.saveString(Constants.KEY_USER_INFO, "");
        SPUtil.saveString(Constants.KEY_TOKEN_ID, "");
        SPUtil.saveString(Constants.NINEVIEW_PASSWORD, "");
        setPushBind(false);
        SPUtil.saveboolean(Constants.KEY_IS_LOGIN, false);
        SPUtil.saveString(Constants.KEY_IS_AUTH, "");
        SPUtil.saveboolean(Constants.KEY_IS_BUYER, false);
        SPUtil.saveboolean(Constants.KEY_IS_FACTORY, false);
        u = null;
        UserInfoUtil.removeCookie(CommonApplication.getInstance().getApplicationContext());
    }

}
