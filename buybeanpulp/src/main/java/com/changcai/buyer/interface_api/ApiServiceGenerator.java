package com.changcai.buyer.interface_api;

import com.changcai.buyer.common.Urls;
import com.changcai.buyer.okhttp.GeneratorJsonConvertFactory;
import com.changcai.buyer.okhttp.HttpClientHelper;
import com.changcai.buyer.okhttp.ProgressRequestListener;
import com.changcai.buyer.okhttp.ProgressResponseListener;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by liuxingwei on 2016/11/28.
 */

public class ApiServiceGenerator {

    public static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(Urls.BASE_URL)
            .addConverterFactory(GeneratorJsonConvertFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create());


    public static <T> T createService(Class<T> tClass) {
        return builder.client(HttpClientHelper.
                okHttpClient()
                .build())
                .build()
                .create(tClass);
    }


    /**
     * 创建带响应进度(下载进度)回调的service
     */
    public static <T> T createResponseService(Class<T> tClass, ProgressResponseListener listener) {
        return builder
                .client(HttpClientHelper.addProgressResponseListener(listener))
                .build()
                .create(tClass);
    }


    /**
     * 创建带请求体进度(上传进度)回调的service
     */
    public static <T> T createReqeustService(Class<T> tClass, ProgressRequestListener listener) {
        return builder
                .client(HttpClientHelper.addProgressRequestListener(listener))
                .build()
                .create(tClass);
    }

}
