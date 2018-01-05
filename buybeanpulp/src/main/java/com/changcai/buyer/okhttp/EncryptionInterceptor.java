package com.changcai.buyer.okhttp;

import com.android.volley.NetworkError;
import com.changcai.buyer.CommonApplication;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.interface_api.ApiCodeErrorException;
import com.changcai.buyer.util.DesUtil;
import com.changcai.buyer.util.NetUtil;
import com.changcai.buyer.util.SPUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by liuxingwei on 2016/12/28.
 * ok http interceptor
 * you can implements Interceptor do anything you want
 */
public class EncryptionInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Gson gson = new Gson();
        Map<String, String> requestMap = SPUtil.getObjectFromShare(Constants.REQUEST_BASE_PARAMETERS);
        FormBody b = (FormBody) request.body();
        for (int i = 0; i < b.size(); i++) {
            requestMap.put(b.name(i), b.value(i));
        }
        if (!requestMap.containsKey(Constants.KEY_TOKEN_ID)){
            requestMap.put(Constants.KEY_TOKEN_ID,SPUtil.getString(Constants.KEY_TOKEN_ID));
        }
        String requestString = DesUtil.encrypt(gson.toJson(requestMap), "abcdefgh");
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        bodyBuilder.add("requestJSON",requestString);
        request = request.newBuilder().post(bodyBuilder.build()).build();
        return chain.proceed(request);
    }


}
