package com.changcai.buyer.http;


import com.google.gson.JsonObject;

/**
 * @description http请求响应接口
 * @author zhoujun
 * @date 2014年6月21日 下午11:49:45
 * @version 1.0
 */
public interface IHttpListener {
	
	void onResponseString(String response);
	void onResponseJson(JsonObject response);
	void onError(String error);
}
