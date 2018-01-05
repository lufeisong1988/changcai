package com.changcai.buyer.ui.calendar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.changcai.buyer.R;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.ui.base.BaseFragment;
import com.changcai.buyer.ui.main.MainActivity;
import com.changcai.buyer.util.AndroidUtil;
import com.changcai.buyer.util.FileUtil;
import com.changcai.buyer.util.SPUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liuxingwei on 2016/12/2.
 */

public class CalendarFragment extends BaseFragment {

    @BindView(R.id.wv_calendar)
    WebView wvCalendar;

    Unbinder unbinder;


    private WebSettings webSettings;
    private final static String assetsFilePath = "file:///android_asset/calendarApp/calendarHome.html";

    public WebView getWvCalendar() {
        return wvCalendar;
    }

    @SuppressWarnings("deprecation")
    public void synCookies(String eachEveryUrl) {
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean value) {

                }
            });
        } else {
            cookieManager.removeAllCookie();
        }
        if (TextUtils.isEmpty(SPUtil.getString(Constants.KEY_TOKEN_ID))) {
            cookieManager.setCookie(eachEveryUrl, "MAIDOUPO_TOKEN" + "=" + AndroidUtil.getAppVersionName(getContext()));
        } else {
            cookieManager.setCookie(eachEveryUrl, "MAIDOUPO_TOKEN" + "=" + SPUtil.getString(Constants.KEY_TOKEN_ID));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_calendar, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWebView();
        if (SPUtil.getBoolean(Constants.KEY_INCREMENT_H5)) {
            String storageHtmlFile = getActivity().getApplication().getFilesDir().getPath().concat(Constants.internal_storage_h5).concat("calendar/calendarHome.html");
            File file = new File(storageHtmlFile);
            if (file.exists()) {
                wvCalendar.loadUrl("file:///".concat(storageHtmlFile));
            } else {
                wvCalendar.loadUrl(assetsFilePath);
            }
        } else {
            wvCalendar.loadUrl(assetsFilePath);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * 初试化webview
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("JavascriptInterface")
    private void initWebView() {
        webSettings = wvCalendar.getSettings();
        // webSettings.setBuiltInZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setDomStorageEnabled(true);// support local storage
        webSettings.setDatabaseEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        File fileDir = new File(Constants.WEB_CACHE);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        webSettings.setDatabasePath(Constants.WEB_CACHE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        wvCalendar.setScrollbarFadingEnabled(true);
        wvCalendar.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wvCalendar.requestFocus();// 使WebView内的输入框等获得焦点
        //获取标题
        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String htmlTitle) {
                super.onReceivedTitle(view, htmlTitle);
//                    if (!TextUtils.isEmpty(title)) {
//                        tvTitle.setText(title);
//                    } else {
//                        tvTitle.setText(htmlTitle);
//                    }
            }

        };
        wvCalendar.addJavascriptInterface(new JsResultInterface(), "ResultModel");

//        webView.addJavascriptInterface(new Object() {
//            @JavascriptInterface
//            public void showApiCall(final String tradeNumber,String comName,String comDepict,String orderNumber,String totalPrice) {//交易号，公司名称，产品名称，订单号、价格
////                dataManager.showToast(tradeNumber + comName + comDepict + orderNumber + totalPrice);
//                AlipayUtil.getInstance(CommonWebViewActivity.this).setOrderInformation(tradeNumber, totalPrice, comName, comDepict, Urls.payNotifyUrl)
//                        .toAlipay();
//            }
//        }, "Android");
        // 设置setWebChromeClient对象
        wvCalendar.setWebChromeClient(wvcc);

        wvCalendar.setWebViewClient(new WebViewClient() {
            // 点击网页里面的链接还是在当前的webView内部跳转，不跳转外部浏览器

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http:") || url.startsWith("https:") || url.startsWith("file:")) {
                    synCookies(url);
                    view.loadUrl(url);
                    return true;
                } else {
                    // Otherwise allow the OS to handle things like tel, mailto, etc.
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }


            public void onLoadResource(WebView view, String url) {

            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //修改3.1.0crash android.view.WindowManager$BadTokenException: Unable to add window -- token android.os.BinderProxy@435a85c8 is not valid; is your activity running?
//                    try {
//                        if (CommonWebViewActivity.this.isFinishing())
//                            return;
//                        if (!VolleyUtil.getInstance().isShowPd()) {
//                            VolleyUtil.getInstance().showPd(CommonWebViewActivity.this);
//                        }
//                        webView.setVisibility(View.VISIBLE);
//                        ll_empty_view.setVisibility(View.GONE);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //修改3.1.0crash android.view.WindowManager$BadTokenException: Unable to add window -- token android.os.BinderProxy@435a85c8 is not valid; is your activity running?
//                    try {
//                        if (CommonWebViewActivity.this.isFinishing())
//                            return;
//                        VolleyUtil.getInstance().cancelProgressDialog();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                if (view.canGoBack()) {
//                    btnBack.setVisibility(View.VISIBLE);
                } else {
//                    btnBack.setVisibility(View.GONE);
                }
            }


            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // mErrorView.setVisibility(View.VISIBLE);
//                    webView.setVisibility(View.GONE);
//                    ll_empty_view.setVisibility(View.VISIBLE);
                showShortToast(getString(R.string.net_work_exception));
            }
        });


    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (wvCalendar.canGoBack()) {
//                btnBack.setVisibility(View.VISIBLE);
            } else {
//                btnBack.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //清空所有Cookie
    }

    @SuppressWarnings("deprecation")
    private void clearWevViewAppCacheAndStorage() {
        wvCalendar.setWebChromeClient(null);
        wvCalendar.setWebViewClient(null);
        wvCalendar.getSettings().setJavaScriptEnabled(false);
        wvCalendar.clearCache(true);
        wvCalendar.clearHistory();
        wvCalendar.clearFormData();
        if (!TextUtils.isEmpty(webSettings.getDatabasePath())) {
            FileUtil.deleteFile(new File(webSettings.getDatabasePath()));
        }
        WebStorage webStorage = WebStorage.getInstance();
        webStorage.deleteAllData();
        File webViewCacheDir = new File(Constants.WEB_CACHE);
        if (webViewCacheDir.exists()) {
            FileUtil.deleteFile(webViewCacheDir);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            if (wvCalendar.canGoBack()) {
//                btnBack.setVisibility(View.VISIBLE);
            } else {
//                btnBack.setVisibility(View.GONE);
            }
        }
    }


    @Override
    protected void initSystemStatusBar() {
        super.initSystemStatusBar();
        immersionBar.statusBarDarkFont(true,0.0f).statusBarColor(R.color.white).fitsSystemWindows(false).init();
    }

    @Override
    public void onDestroyView() {
        clearWevViewAppCacheAndStorage();
        unbinder.unbind();
        super.onDestroyView();
    }

    private class JsResultInterface {
        public JsResultInterface() {
        }

        @JavascriptInterface
        public void jsCallResult(String object) {
            Observable.just(object)
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onCompleted() {
                            showShortToast("completed");
                        }

                        @Override
                        public void onError(Throwable e) {

                            showShortToast(e.getMessage());
                        }

                        @Override
                        public void onNext(String s) {
                            showShortToast(s);
                        }
                    });
        }
    }
}
