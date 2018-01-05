package com.changcai.buyer.http;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.changcai.buyer.util.DesUtil;
import com.changcai.buyer.util.SPUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.util.AndroidUtil;
import com.changcai.buyer.util.JsonFormat;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.util.StringUtil;
import com.changcai.buyer.util.UserInfoUtil;
import com.changcai.buyer.view.LoadingProgressDialog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 * @author zhoujun
 * @version 1.0
 * @description Volley请求框架
 * @date 2014年6月21日 下午11:21:09
 */
public class VolleyUtil {
    private static final String TAG = "VolleyUtil";
    private static final int REQUEST_TIME = 6000;
    private static VolleyUtil instance;
    private static RequestQueue queue;

    private VolleyUtil() {

    }

    public static VolleyUtil getInstance() {
        if (instance == null) {
            instance = new VolleyUtil();
        }
        return instance;
    }

    /**
     * VolleyUtil Post请求
     *
     * @param context
     * @param url            地址
     * @param params         请求参数
     * @param listener       请求返回结果监听
     * @param isShowProgress 是否提示progress
     * @param needCookie     是否设置cookie
     */
    public void httpPost(final Context context, final String url, final Map<String, String> params,
                         final HttpListener listener, final boolean isShowProgress, final boolean needCookie) {
        if (isShowProgress && !isShowPd()) {
            showPd(context);
        }
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }
        LogUtil.d("url", url);
//        setAuth(params);
//        Map<String, String> baseParams = SPUtil.getObjectFromShare(Constants.REQUEST_BASE_PARAMETERS);
//        if (baseParams == null) {
            params.put("clientinfo_appname", "买豆粕");//app名称
            params.put("clientinfo_appversion", AndroidUtil.getAppVersionName(context));//app版本号
            params.put("clientinfo_os", "Android");
            params.put("clientinfo_osversion", Build.VERSION.RELEASE + "");//操作系统名称
            params.put("clientinfo_channel", AndroidUtil.getMetaValue(context, "UMENG_CHANNEL"));//渠道号
            params.put("clientinfo_devicevendor", Build.BRAND);//手机品牌
            params.put("clientinfo_devicename", Build.MODEL + "/" + Build.VERSION.RELEASE);
            params.put("clientinfo_deviceid", AndroidUtil.getIMEI(context));
//        }
//        params.putAll(baseParams);

        if (!params.containsKey(Constants.KEY_TOKEN_ID)){
            params.put(Constants.KEY_TOKEN_ID,SPUtil.getString(Constants.KEY_TOKEN_ID));
        }
//        params.put("sign", getSignByValueAsc(params));
//        params.remove("key");
        final Gson gson = new Gson();
        final Map<String, String> realParams = new HashMap<String, String>();
        if (params != null) {
            realParams.put("requestJSON", DesUtil.encrypt(gson.toJson(params), "abcdefgh"));
            LogUtil.d(TAG, "-------" + url + "?requestJSON=" + gson.toJson(params));
            LogUtil.d(TAG, "-------" + url + "?requestJSON=" + DesUtil.encrypt(gson.toJson(params), "abcdefgh"));
        }
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                LogUtil.d(TAG, "url = " + url + "----succeed = "  +response);
                if (listener != null) {
                    listener.onResponseString(response);
                    try {
                        JsonObject jsonObject = JsonFormat.String2Object(response);
                        listener.onResponseJson(jsonObject);
                    } catch (Exception e) {
//                        Toast.makeText(context, "请求出错", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        Log.d("Error", e.toString());
                    }
                }
                cancelProgressDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                LogUtil.d(TAG,"url = " + url + "----error = "  + error.toString());
                if (error != null) {
                    if (null == error.networkResponse) {
                        listener.onError(Constants.CONNECT_SERVER_FAILED);
                    } else if (error instanceof TimeoutError) {
                        listener.onError(Constants.CONNECT_SERVER_TIMEOUT);
                        Toast.makeText(context, Constants.CONNECT_SERVER_TIMEOUT, Toast.LENGTH_SHORT).show();
                    } else if (error.networkResponse.statusCode == 404) {
                        listener.onError(Constants.CONNECT_SERVER_FAILED);
                    } else {
                        listener.onError(Constants.CONNECT_SERVER_TIMEOUT);
                    }

                } else {
                    if (isShowProgress) {
                        Log.d(TAG, Constants.UNKOWN_ERROR);
                    }
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                return realParams;
            }

            // 设置头信息
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Charset", "UTF-8");
                String localCookie = UserInfoUtil.getCookies(context, url);
                if (!TextUtils.isEmpty(localCookie) && needCookie)
                    headers.put("Cookie", localCookie);
                return headers;
            }

            // 设置超时
            @Override
            public RetryPolicy getRetryPolicy() {
                RetryPolicy retryPolicy = new DefaultRetryPolicy(REQUEST_TIME, 0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                return retryPolicy;
            }

            // 设置返回编码格式
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String str = null;
                try {
                    Map<String, String> responseHeaders = response.headers;
                    String rawCookies = responseHeaders.get("Set-Cookie");
                    if (!TextUtils.isEmpty(rawCookies))
                        UserInfoUtil.synCookies(context, url, rawCookies);
                    str = new String(response.data, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return Response.success(str, HttpHeaderParser.parseCacheHeaders(response));
            }

        };
        queue.add(postRequest);
    }

    /**
     * VolleyUtil Post请求
     *
     * @param context
     * @param url            地址
     * @param params         请求参数
     * @param listener       请求返回结果监听
     * @param isShowProgress 是否提示progress
     */
    public void httpPost(final Context context, final String url, final Map<String, String> params,
                         final HttpListener listener, final boolean isShowProgress) {
        this.httpPost(context, url, params, listener, isShowProgress, false);
    }

    private Dialog pd;

    /**
     * 显示dialog
     *
     * @param context
     */
    public void showPd(Context context) {
        try {
            pd = LoadingProgressDialog.createLoadingDialog(context);
            pd.setCanceledOnTouchOutside(true);
            pd.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否正在显示加载dialog
     *
     * @return
     */
    public boolean isShowPd() {
        if (pd != null && pd.isShowing()) {
            return true;
        }
        return false;
    }

    /**
     * 取消dialog
     */
    public void cancelProgressDialog() {
        try {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取get方法url
     *
     * @param url
     * @param params
     * @return
     */
    @SuppressWarnings("unused")
    private String getUrl(String url, HashMap<String, String> params) {
        if (params == null) {
            return url;
        }
        StringBuilder encodeUrl = new StringBuilder(url);
        boolean isFirst = true;
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (isFirst) {
                    isFirst = false;
                    encodeUrl.append("?");
                } else {
                    encodeUrl.append("&");
                }
                encodeUrl.append(URLEncoder.encode(entry.getKey(), "utf-8") + "=" + URLEncoder.encode(entry.getValue(), "utf-8"));
            }
//            Log.d("url", encodeUrl.toString());
            return encodeUrl.toString();
        } catch (UnsupportedEncodingException e) {
            return url;
        }
    }


    /**
     * 根据KEY升序获取
     *
     * @param params
     * @return
     */
    public String getSignByKeyAsc(Map<String, String> params) {
        TreeMap<String, String> tree = new TreeMap<String, String>(new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);
            }
        });
        tree.putAll(params);
        Collection<String> values = tree.values();
        StringBuffer buf = new StringBuffer();
        for (String value : values) {
            buf.append(value).append('&');
        }
        if (0 == buf.length()) {
            return "";
        }
        return StringUtil.MD5(buf.substring(0, buf.length() - 1).toString());
    }


    /**
     * 根据VALUE升序获取
     *
     * @param params
     * @return
     */
    public String getSignByValueAsc(Map<String, String> params) {
        ArrayList<String> list = new ArrayList<String>();
        list.addAll(params.values());
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                if (TextUtils.isEmpty(lhs) && TextUtils.isEmpty(rhs)) {
                    return 0;
                }
                if (TextUtils.isEmpty(lhs)) {
                    return -1;
                }
                if (TextUtils.isEmpty(rhs)) {
                    return 1;
                }
//                int length = lhs.length() > rhs.length() ? rhs.length() : lhs.length();
//                String f = lhs.substring(0, length);
//                String s = rhs.substring(0, length);
                return lhs.compareTo(rhs);
            }
        });
        StringBuffer buf = new StringBuffer();
        for (String value : list) {
            buf.append(value).append('&');
        }
        if (0 == buf.length()) {
            return "";
        }
        return StringUtil.MD5(buf.substring(0, buf.length() - 1).toString());
    }


    public void setAuth(Map<String, String> params) {
        params.put("appid", Constants.APP_ID);
        params.put("key", Constants.KEY);
        params.put("no", UUID.randomUUID().toString().replaceAll("-", ""));
    }

}
