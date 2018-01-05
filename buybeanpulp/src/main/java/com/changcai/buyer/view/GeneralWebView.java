package com.changcai.buyer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.changcai.buyer.common.CommonJsInterface;
import com.changcai.buyer.common.Constants;

import java.io.File;

/**
 * Created by liuxingwei on 2017/3/23.
 */

public class GeneralWebView extends WebView {
    public GeneralWebView(Context context) {
        super(context);
        initAndAddJsInterface();
    }

    public GeneralWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAndAddJsInterface();
    }

    public GeneralWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAndAddJsInterface();
    }

    @SuppressWarnings("deprecation")
    private void initAndAddJsInterface() {
        WebSettings webSettings = this.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSaveFormData(false);
//        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDomStorageEnabled(true);// support local storage
        webSettings.setDatabaseEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        File fileDir = new File(Constants.WEB_CACHE);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        webSettings.setDatabasePath(Constants.WEB_CACHE);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        this.setScrollbarFadingEnabled(true);
        this.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        this.requestFocus();// 使WebView内的输入框等获得焦点
        //获取标题

        this.addJavascriptInterface(new CommonJsInterface(getContext()), "ResultModel");
    }
}
