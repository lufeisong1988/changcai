package com.changcai.buyer.business_logic.about_buy_beans.app_function_introduce;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.changcai.buyer.BaseFragment;
import com.changcai.buyer.R;
import com.changcai.buyer.http.VolleyUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by liuxingwei on 2016/11/23.
 */

public class AppFunctionIntroduceFragment extends BaseFragment implements AppFunctionIntroduceContract.View {
    AppFunctionIntroduceContract.Presenter presenter;

    Unbinder unbinder;
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.iv_empty_view)
    ImageView ivEmptyView;
    @BindView(R.id.ll_empty_view)
    LinearLayout llEmptyView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_function_introduce, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.detach();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void webViewSetting() {
        WebSettings webSettings = webView.getSettings();
        // webSettings.setBuiltInZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setDisplayZoomControls(false);
//        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setTextSize(WebSettings.TextSize.LARGEST);
        webView.setScrollbarFadingEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.requestFocus();// 使WebView内的输入框等获得焦点


        webView.setWebViewClient(new WebViewClient() {
            // 点击网页里面的链接还是在当前的webView内部跳转，不跳转外部浏览器
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (activity.isFinishing()) {
                    return;
                }

                if (!VolleyUtil.getInstance().isShowPd()) {
                    VolleyUtil.getInstance().showPd(activity);
                }


            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                webView.setVisibility(View.GONE);
                llEmptyView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                try {
                    if (activity.isFinishing())
                        return;
                    VolleyUtil.getInstance().cancelProgressDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
//                if (url.startsWith("http:") || url.startsWith("https:")) {
//                    view.loadUrl(url);
//                    return true;
//                } else {
//                    // Otherwise allow the OS to handle things like tel, mailto, etc.
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    startActivity(intent);
//                    return true;
//                }


            }
        });
        presenter.loadLink();
    }

    @Override
    public void loadWebUrl() {
        if (isActive()) {
            webView.loadDataWithBaseURL(null, "<html>\n" +
                    "    <head>\n" +
                    "        <meta charset=utf-8>\n" +
                    "            <title></title>\n" +
                    "            \n" +
                    "            <style type=\"text/css\">\n" +
                    "                \n" +
                    "            .content {\n" +
                    "                \n" +
                    "                    padding:10px 10px 0px 10px;\n" +
                    "                    \n" +
                    "                }\n" +
                    "            .content h3{\n" +
                    "                \n" +
                    "                font-size:24px;\n" +
                    "                color:#333333;\n" +
                    "            }\n" +
                    "            .content p\n" +
                    "            {\n" +
                    "                \n" +
                    "                font-size:22px;\n" +
                    "                color:#030303;\n" +
                    "            }\n" +
                    "            </style>\n" +
                    "    </head>\n" +
                    "    \n" +
                    "    <body>\n" +
                    "        <body style=\"background-color:#eaeaea;\">\n" +
                    "        </body>\n" +
                    "        <div class=\"content\">\n" +
                    "            <h3> 本次更新：</h3>\n" +
                    "            <p>\n" +
                    "            1）“消息中心”新增“产业联盟”群;<br>\n" +
                    "            2）优化“会员顾问”;<br>\n" +
                    "            </p>\n" +
                    "            <h3> 最近更新：</h3>\n" +
                    "            <p>\n" +
                    "             1）合并资源报价和商城报价相关功能;<br>\n" +
                    "            </p>\n" +
                    "        </div>\n" +
                    "    </body>\n" +
                    "</html>", "text/html", "utf-8", null);
        }

    }


    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(AppFunctionIntroduceContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showErrorDialog(String message) {

    }

    @OnClick({R.id.iv_empty_view, R.id.ll_empty_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_empty_view:
            case R.id.ll_empty_view:
                presenter.loadLink();
                break;
        }
    }
}
