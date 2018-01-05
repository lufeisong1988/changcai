package com.changcai.buyer.okhttp;

/**
 * Created by liuxingwei on 2016/11/28.
 * <p>
 * 响应体进度回调接口，用于文件下载进度回调
 */

public interface ProgressResponseListener {
    void onResponseProgress(long bytesRead, long contentLength, boolean done);
}
