package com.changcai.buyer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.Unbinder;

/**
 * Created by liuxingwei on 2016/11/22.
 */

public class BaseFragment extends Fragment {

    protected Activity activity;
    protected Unbinder baseUnbinder;


    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    protected void gotoActivity(Class<? extends Activity> clazz) {
        gotoActivity(clazz, false, null);
    }

    protected void gotoActivity(Class<? extends Activity> clazz, Bundle bundle) {
        gotoActivity(clazz, false, bundle);
    }

    protected void gotoActivity(Class<? extends Activity> clazz, boolean finish, Bundle pBundle) {
        if (!isAdded()) return;
        Intent intent = new Intent(getContext(), clazz);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }

        startActivity(intent);
        if (finish) {
            activity.finish();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (baseUnbinder != null) {
            baseUnbinder.unbind();
        }
        activity = null;
    }

    WebViewClient getWebViewClient = new WebViewClient() {
        /**
         * 在开始加载网页时会回调
         */
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

        }

        /**
         * 在结束加载网页时会回调
         */
        public void onPageFinished(WebView view, String url) {

        }

        /**
         * 加载错误的时候会回调，在其中可做错误处理，比如再请求加载一次，或者提示404的错误页面
         */
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

        }

        /**
         * 当接收到https错误时，会回调此函数，在其中可以做错误处理
         */
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {

        }

    };

    protected void initWebSettings(WebView wb) {
        wb.getSettings().setJavaScriptEnabled(true);
        wb.setWebChromeClient(new WebChromeClient());
        wb.setWebViewClient(getWebViewClient);

        WebSettings webSettings = wb.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity == 240) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if (mDensity == 120) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        } else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == DisplayMetrics.DENSITY_TV) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
//        webSettings.setDatabasePath(BaseActivity.this.getApplicationContext().getCacheDir().getAbsolutePath());
    }
}
