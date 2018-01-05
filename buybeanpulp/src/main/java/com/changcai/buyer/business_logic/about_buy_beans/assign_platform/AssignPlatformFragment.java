package com.changcai.buyer.business_logic.about_buy_beans.assign_platform;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.changcai.buyer.util.AndroidUtil;
import com.changcai.buyer.util.SPUtil;

import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.changcai.buyer.BaseFragment;
import com.changcai.buyer.R;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.http.VolleyUtil;
import com.changcai.buyer.rx.RefreshOrderEvent;
import com.changcai.buyer.view.ConfirmDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by liuxingwei on 2016/11/22.
 */

public class AssignPlatformFragment extends BaseFragment implements AssignPlatformContract.View {

    AssignPlatformContract.Presenter assignPlatformPresenter;
    @BindView(R.id.web_assign)
    WebView webAssign;
    @BindView(R.id.tv_agree_sign)
    TextView tvAgreeSign;


    private String assignWebLink = "http://cms.maidoupo.com/article/70.htm";
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
            cookieManager.setCookie(eachEveryUrl, "MAIDOUPO_TOKEN" + "=" + AndroidUtil.getAppVersionName(getActivityContext()));
        } else {
            cookieManager.setCookie(eachEveryUrl, "MAIDOUPO_TOKEN" + "=" + SPUtil.getString(Constants.KEY_TOKEN_ID));
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_assign_platform_agreeement, container, false);
        baseUnbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assignPlatformPresenter.start();
        webSetting();
//        Bundle b = getArguments();
//        if (null != b && b.getBoolean("button_gone")) {
//            tvAgreeSign.setVisibility(View.GONE);
//        } else {
//            tvAgreeSign.setVisibility(View.VISIBLE);
//        }
    }

    private void webSetting() {
        WebSettings webSettings = webAssign.getSettings();
        // webSettings.setBuiltInZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
//        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(false);
        webAssign.setScrollbarFadingEnabled(true);
        webAssign.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webAssign.requestFocus();// 使WebView内的输入框等获得焦点
        //获取标题
        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String htmlTitle) {
                super.onReceivedTitle(view, htmlTitle);

            }

        };
        webAssign.getSettings().setJavaScriptEnabled(true);
        webAssign.setWebChromeClient(wvcc);

        webAssign.setWebViewClient(new WebViewClient() {
            // 点击网页里面的链接还是在当前的webView内部跳转，不跳转外部浏览器

            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http:") || url.startsWith("https:")) {
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

            // 可以让webView处理https请求

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
//                super.onReceivedSslError(webView, sslErrorHandler, sslError);
                sslErrorHandler.proceed();
            }


            public void onLoadResource(WebView view, String url) {

            }



            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //修改3.1.0crash android.view.WindowManager$BadTokenException: Unable to add window -- token android.os.BinderProxy@435a85c8 is not valid; is your activity running?
                try {
                    if (activity.isFinishing())
                        return;
                    if (!VolleyUtil.getInstance().isShowPd()) {
                        VolleyUtil.getInstance().showPd(activity);
                    }
                    webAssign.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //修改3.1.0crash android.view.WindowManager$BadTokenException: Unable to add window -- token android.os.BinderProxy@435a85c8 is not valid; is your activity running?
                try {
                    if (activity.isFinishing())
                        return;
                    VolleyUtil.getInstance().cancelProgressDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // mErrorView.setVisibility(View.VISIBLE);
                webAssign.setVisibility(View.GONE);
                showErrorDialog(getString(R.string.net_work_exception), new ConfirmDialog.OnBtnConfirmListener() {
                    @Override
                    public void onConfirmListener() {
                        webViewLoadUrl();
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        assignPlatformPresenter.detach();
    }

    public AssignPlatformFragment() {

    }

    public static AssignPlatformFragment newInstance() {
        return new AssignPlatformFragment();
    }


    @Override
    public void showSuccessDialog(int message) {
        ConfirmDialog.createConfirmDialog(activity, getString(message), new ConfirmDialog.OnBtnConfirmListener() {
            @Override
            public void onConfirmListener() {
                if (activity != null) {
                    activity.finish();
                }
                RefreshOrderEvent.publish(true);
            }
        });
    }

    @Override
    public Context getActivityContext() {
        return activity;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void webViewLoadUrl() {
        webAssign.loadUrl(assignWebLink);
    }

    @Override
    public void setPresenter(AssignPlatformContract.Presenter presenter) {
        this.assignPlatformPresenter = presenter;
    }

    private void showErrorDialog(String message, ConfirmDialog.OnBtnConfirmListener onBtnConfirmListener) {
        ConfirmDialog.createConfirmDialog(activity, message, onBtnConfirmListener);
    }

    @Override
    public void showErrorDialog(String message) {
        ConfirmDialog.createConfirmDialog(activity, message);
    }

    @OnClick(R.id.tv_agree_sign)
    public void onClick() {
        assignPlatformPresenter.commitAssignPlatformContract(SPUtil.getString(Constants.KEY_TOKEN_ID));
    }
}
