package com.changcai.buyer.ui.introduction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.changcai.buyer.R;
import com.changcai.buyer.http.VolleyUtil;
import com.changcai.buyer.ui.base.BaseTouchBackActivity;



/**
 * Created by liuxingwei on 2016/11/9.
 */

public class MemberShipIntroductionActivity extends BaseTouchBackActivity {

    private LinearLayout emptyView;
    private String advertisementUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_ship_introduction);
        if (getIntent().getExtras()!=null && getIntent().getExtras().containsKey("activeUrl")){
            advertisementUrl = getIntent().getExtras().getString("activeUrl");
        }
        iniView();
        final WebView webView = (WebView) findViewById(R.id.fullWebView);
        emptyView = (LinearLayout) findViewById(R.id.ll_empty_view);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl(TextUtils.isEmpty(advertisementUrl)?"http://cms.maidoupo.com/vipLevel":advertisementUrl);
                emptyView.setVisibility(View.GONE);
                showShortToast("网络异常");
            }
        });
        WebSettings webSettings = webView.getSettings();
        // webSettings.setBuiltInZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setDisplayZoomControls(false);
//        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webView.setScrollbarFadingEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.requestFocus();// 使WebView内的输入框等获得焦点

        webView.setWebViewClient(new WebViewClient() {
            // 点击网页里面的链接还是在当前的webView内部跳转，不跳转外部浏览器
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (MemberShipIntroductionActivity.this.isFinishing()){
                    return;
                }

                if (!VolleyUtil.getInstance().isShowPd()) {
                    VolleyUtil.getInstance().showPd(MemberShipIntroductionActivity.this);
                }


            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                webView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                try {
                    if (MemberShipIntroductionActivity.this.isFinishing())
                        return;
                    VolleyUtil.getInstance().cancelProgressDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url);
                    return true;
                } else {
                    // Otherwise allow the OS to handle things like tel, mailto, etc.
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
            }
        });
        webView.loadUrl(TextUtils.isEmpty(advertisementUrl)?"http://cms.maidoupo.com/vipLevel":advertisementUrl);
    }

    private void iniView() {
        titleView = findViewById(R.id.titleView);
        titleView.setBackgroundColor(getResources().getColor(R.color.white));
        titleView.getBackground().setAlpha(0);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setImageResource(R.drawable.icon_back_white);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        tvTitle.setText("会员等级说明");
    }


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
