package com.changcai.buyer.http;

import com.google.gson.JsonObject;

public class HttpListener implements IHttpListener {

	/**
	 * 字符串
	 */
	@Override
	public void onResponseString(String response) {

	}
	/**
	 * jsonobject
	 */
	@Override
	public void onResponseJson(JsonObject response) {
		
	}
	/**
	 * 错误
	 */
	@Override
	public void onError(String error) {
		
	}
	
}
