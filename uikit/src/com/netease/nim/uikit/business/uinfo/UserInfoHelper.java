package com.netease.nim.uikit.business.uinfo;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;

public class UserInfoHelper {

    // 获取用户显示在标题栏和最近联系人中的名字
    public static String getUserTitleName(String id, SessionTypeEnum sessionType) {
        if (sessionType == SessionTypeEnum.P2P) {
//            if (NimUIKit.getAccount().equals(id)) {
//                return "我的电脑";
//            } else {
                return getUserDisplayName(id);
//            }
        } else if (sessionType == SessionTypeEnum.Team) {
            return TeamHelper.getTeamName(id);
        }
        return id;
    }

    /**
     * @param account 用户帐号
     * @return
     */
    public static String getUserDisplayName(String account) {
        String alias = NimUIKit.getContactProvider().getAlias(account);
        if (!TextUtils.isEmpty(alias)) {
            return alias;
        } else {
            UserInfo userInfo = NimUIKit.getUserInfoProvider().getUserInfo(account);
            if (userInfo != null && !TextUtils.isEmpty(userInfo.getName())) {
                return userInfo.getName();
            } else {
                return account;
            }
        }
    }

    // 获取用户原本的昵称
    public static String getUserName(String account) {
        UserInfo userInfo = NimUIKit.getUserInfoProvider().getUserInfo(account);
        if (userInfo != null && !TextUtils.isEmpty(userInfo.getName())) {
            return userInfo.getName();
        } else {
            return account;
        }
    }
    public static String getUserNameWithHiden(String account){
        UserInfo userInfo = NimUIKit.getUserInfoProvider().getUserInfo(account);
        if (userInfo != null && !TextUtils.isEmpty(userInfo.getName())) {
            return userInfo.getName();
        } else {
            if(isMobileNO(account)){
                return setHideNumber(account,3,4);
            }
            return account;
        }
    }
    public static boolean isMobileNO(String mobiles) {
        /*
        移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		联通：130、131、132、152、155、156、185、186
		电信：133、153、180、189、（1349卫通）
		特殊：177，144
		总结起来就是第一位必定为1，第二位必定为3或5或8或7或4，其他位置的可以为0-9
		*/
        String telRegex = "[1][35874]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }
    public static String setHideNumber(String str, int index, int length) {
        StringBuilder sb = new StringBuilder();
        StringBuilder star = new StringBuilder();
        sb.append(str.replaceAll(" ", ""));
        for (int i = 0; i < length; i++)
            star.append("*");
        return sb.length() >= (index + length) ? sb.replace(index, index + length, star.toString()).toString() : str;
    }
    /**
     * @param account         账号
     * @param selfNameDisplay 如果是自己，则显示内容
     * @return
     */
    public static String getUserDisplayNameEx(String account, String selfNameDisplay) {
        if (account.equals(NimUIKit.getAccount())) {
            return selfNameDisplay;
        }

        return getUserDisplayName(account);
    }

    /**
     *
     * @param account
     * @return
     */
    public static String getUserExtLevel(String account){
        LogUtil.d("level","account = " + account);
        NimUserInfo nimUserInfo = NIMClient.getService(UserService.class).getUserInfo(account);
        if (nimUserInfo != null && !TextUtils.isEmpty(nimUserInfo.getExtension())) {
            JSONObject obj = JSONObject.parseObject(nimUserInfo.getExtension());
            String grade = obj.getString("grade");
            return grade == null ? "" : grade;
        } else {
            return "";
        }
    }
}
