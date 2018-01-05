package com.changcai.buyer.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * SharedPreferences工具类
 * @author Zhoujun
 * 说明：SharedPreferences的操作工具类，需要缓存到SharedPreferences中的数据在此设置。
 */
public class SharedPrefUtil {
	
	public static final String SINA_UID = "sina_uid";// 新浪微博唯一id
	public static final String WEIBO_ACCESS_TOKEN = "weibo_access_token";//新浪微博令牌
	public static final String WEIBO_EXPIRES_IN = "weibo_expires_in";//新浪微博令牌时间
	public static final String WEIBO_ACCESS_CURR_TIME = "weibo_sccess_curr_time";//新浪微博授权时间
	
	

	//-----------------------------新浪微博验证信息-----------------
	/**
	 * 设置微博绑定信息
	 * @param context
	 * @param access_token
	 * @param expires_in
	 */
	public static void setWeiboInfo(Context context,String sina_uid,String access_token,String expires_in,  String access_curr_time){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putString(SINA_UID, sina_uid);
		e.putString(WEIBO_ACCESS_TOKEN, access_token);
		e.putString(WEIBO_EXPIRES_IN, expires_in);
		e.putString(WEIBO_ACCESS_CURR_TIME, access_curr_time);
		e.commit();
	}
	/**
	 * 清除微博绑定
	 * @param context
	 * @return
	 */
	public static void clearWeiboBind(Context context){
		SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(context);
		sp.edit().remove(WEIBO_ACCESS_TOKEN).remove(WEIBO_EXPIRES_IN).commit();
	}
	public static String getWeiboAccessToken(Context context){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(WEIBO_ACCESS_TOKEN, null);
	}
	public static String getWeiboExpiresIn(Context context){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(WEIBO_EXPIRES_IN, null);
	}
	public static String getWeiboAccessCurrTime(Context context){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(WEIBO_ACCESS_CURR_TIME, null);
	}

	/**
	 * 检测新浪微博是否绑定
	 */
	public static boolean checkWeiboBind(Context context) {
		String WeiboAccessToken = getWeiboAccessToken(context);
		String WeiboExpiresIn = getWeiboExpiresIn(context);
		String weiboAccessCurrTime = getWeiboAccessCurrTime(context);
		if (WeiboAccessToken == null || WeiboExpiresIn == null || weiboAccessCurrTime == null) {
			return false;
		} else {
			long currTime = System.currentTimeMillis();
			long accessCurrTime = Long.parseLong(weiboAccessCurrTime);
			long expiresIn = Long.parseLong(WeiboExpiresIn);
			if((currTime-accessCurrTime)/1000>expiresIn){
				return false;
			}else{
				return true;
			}
		}
	}
	
	
	/**
	 * 获取服务号码
	 * @param context
	 * @return
	 */
	public static String getServiceNum(Context context,String key){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString(key, "");
	}
	/**
	 * 设置服务号码
	 * @param context
	 */
	public static void setServiceNum(Context context, String key,String serviceNum){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor e = sp.edit();
		e.putString(key, serviceNum);
		e.commit();
	}
}
