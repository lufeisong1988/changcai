package com.changcai.buyer.business_logic.about_buy_beans.bind_bank;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.changcai.buyer.BaseFragment;
import com.changcai.buyer.R;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.http.VolleyUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.view.ConfirmDialog;
import com.changcai.buyer.view.GeneralWebView;
import com.juggist.commonlibrary.rx.RefreshOrderEvent;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liuxingwei on 2017/3/23.
 */

public class BindBankFragment extends BaseFragment implements BindBankContract.View {


    BindBankContract.Presenter presenter;
    @BindView(R.id.web_view)
    GeneralWebView webView;
    @BindView(R.id.iv_empty_view)
    ImageView ivEmptyView;
    @BindView(R.id.ll_empty_view)
    LinearLayout llEmptyView;

    public boolean bindBankSuccess;

    String preViewUrl;

    private final static String assetsFilePath = "file:///android_asset/pinganEPay/";
    private TextView viewTitle;
    private ImageView ivBack;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_function_introduce, container, false);
        baseUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewTitle = (TextView) getActivity().findViewById(R.id.tvTitle);
        ivBack = (ImageView) getActivity().findViewById(R.id.btnBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    if (bindBankSuccess) {
                        getActivity().finish();
                    } else {

                        if (TextUtils.isEmpty(preViewUrl) || preViewUrl.equalsIgnoreCase("none")) {
                            getActivity().finish();
                        } else {
                            loadWebUrl(preViewUrl);
                        }
                    }
                } else {
                    getActivity().finish();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.start();
    }


    @Override
    public void setPresenter(BindBankContract.Presenter presenter) {

        this.presenter = presenter;
    }


    @Override
    public void showErrorDialog(String message) {
        if (isAdded()) {
            ConfirmDialog.createConfirmDialog(getContext(), message);
        }
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

    @Override
    public void webViewSetting() {
        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                ConfirmDialog.createConfirmDialog(getContext(), message);
                result.confirm();
                return true;
            }

        };
        webView.setWebChromeClient(wvcc);

        webView.addJavascriptInterface(new JsInterfaceDoneEvent(), "bank");

        webView.setWebViewClient(new WebViewClient() {
            // 点击网页里面的链接还是在当前的webView内部跳转，不跳转外部浏览器


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http:") || url.startsWith("https:") || url.startsWith("file:")) {
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
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            public void onLoadResource(WebView view, String url) {

            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //修改3.1.0crash android.view.WindowManager$BadTokenException: Unable to add window -- token android.os.BinderProxy@435a85c8 is not valid; is your activity running?
                try {
                    if (!isAdded())
                        return;
                    if (!VolleyUtil.getInstance().isShowPd()) {
                        VolleyUtil.getInstance().showPd(getActivity());
                    }
                    webView.setVisibility(View.VISIBLE);
                    llEmptyView.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //修改3.1.0crash android.view.WindowManager$BadTokenException: Unable to add window -- token android.os.BinderProxy@435a85c8 is not valid; is your activity running?
                try {
                    if (!webView.getTitle().isEmpty()) {
                        String[] arrayTitle = view.getTitle().split("@");
                        if (arrayTitle.length > 1) {
                            viewTitle.setText(arrayTitle[0]);
                            preViewUrl = arrayTitle[1];
                        }
                    }
                    if (!isAdded())
                        return;
                    VolleyUtil.getInstance().cancelProgressDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // mErrorView.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
                llEmptyView.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), R.string.net_work_exception, Toast.LENGTH_LONG).show();
            }
        });

        presenter.loadLink();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("preViewUrl",preViewUrl);
    }

    @Override
    public void loadWebUrl(String fileName) {
        if (TextUtils.isEmpty(fileName))return;
        if (SPUtil.getBoolean(Constants.KEY_INCREMENT_H5)) {
            String storageHtmlFile = getActivity().getApplication().getFilesDir().getPath().concat(Constants.internal_storage_h5).concat("pinganEPay/").concat(fileName);
            File file = new File(storageHtmlFile);
            if (file.exists()) {
                file = null;
                webView.loadUrl("file:///".concat(storageHtmlFile));
            } else {
                webView.loadUrl(assetsFilePath.concat(fileName));
            }
        } else {
            webView.loadUrl(assetsFilePath.concat(fileName));
        }
    }

    private class JsInterfaceDoneEvent {

        public JsInterfaceDoneEvent() {

        }

        @JavascriptInterface
        public void bindBankSuccess(String status) {
            bindBankSuccess = Boolean.parseBoolean(status);
            RefreshOrderEvent.publish(true);
        }

        @JavascriptInterface
        public void bindBankDone() {

            if (getActivity().isFinishing()) return;
            else getActivity().finish();
        }
    }
}
