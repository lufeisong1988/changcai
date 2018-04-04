package com.changcai.buyer.ui.resource;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.changcai.buyer.R;
import com.changcai.buyer.common.Constants;
import com.juggist.commonlibrary.rx.RxBus;
import com.changcai.buyer.ui.base.BaseFragment;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.util.NetUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.ServerErrorCodeDispatch;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by lufeisong on 2017/8/30.
 */

public class QuoteMapFragment extends BaseFragment {
    private final static String assetsFilePath = "file:///android_asset/priceMap/priceMap.html";
    @BindView(R.id.wb_quotemap)
    WebView wbQuotemap;
    @BindView(R.id.iv_refresh)
    ImageView ivRefresh;
    private Unbinder unbinder;
    private Activity activity;
    private Observable<Boolean> backgroundToForegroundEvent;//从后台回到前台

    private Animation animation;
    private Observable<Boolean> loginRx;

    @Override
    public void onAttach(Activity activity) {
        this.activity = activity;
        super.onAttach(activity);
//        activity.getWindow().setFlags(
//                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
//                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginRx = RxBus.get().register("inOrOutAction",Boolean.class);
        loginRx.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (activity != null){
                    wbQuotemap.loadUrl("javascript:loginStatusChanged()");
                }
            }
        });
        backgroundToForegroundEvent = RxBus.get().register("backgroundToForeground", Boolean.class);
        backgroundToForegroundEvent.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_quotemap, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    protected void initSystemStatusBar() {
        super.initSystemStatusBar();
        immersionBar.statusBarDarkFont(true, 0.2f).statusBarColor(R.color.white).fitsSystemWindows(true).init();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initListener();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister("loginRx",loginRx);
//        RxUtil.remove(moreFiltrateSubscription);
        RxBus.get().unregister("backgroundToForeground", backgroundToForegroundEvent);
    }

    private void init() {
        animation = AnimationUtils.loadAnimation(activity,R.anim.anim_rotate);
        LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
        animation.setInterpolator(interpolator);
        wbQuotemap.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                view.loadUrl(url);
                return false;
            }

        });
        wbQuotemap.addJavascriptInterface(new JavaScriptinterface(SPUtil.getString(Constants.KEY_TOKEN_ID)),
                "OfferMapModel");
        initWebSettings(wbQuotemap);
//        wbCashreportOne.loadUrl("https://www.baidu.com");
        wbQuotemap.loadUrl(assetsFilePath);
    }

    private void initListener() {
        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!NetUtil.checkNet(activity)){
                    ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.network_unavailable));
                   return;
                }
                ivRefresh.clearAnimation();
                ivRefresh.startAnimation(animation);
                init();
            }
        });
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
            LogUtil.d("TAG","finish");
            if(activity != null)
                ivRefresh.clearAnimation();

        }

        /**
         * 加载错误的时候会回调，在其中可做错误处理，比如再请求加载一次，或者提示404的错误页面
         */
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            LogUtil.d("TAG","onReceivedError");
            if(activity != null)
                ivRefresh.clearAnimation();
        }

        /**
         * 当接收到https错误时，会回调此函数，在其中可以做错误处理
         */
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            LogUtil.d("TAG","onReceivedSslError");
            if(activity != null)
                ivRefresh.clearAnimation();
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

    class JavaScriptinterface {
        String str;

        public JavaScriptinterface(String str) {
            this.str = str;
        }

        @JavascriptInterface
        public String jsCallObjcAcquireOfferMapBaseParameter() {
            return str;
        }

        @JavascriptInterface
        public void jsCallOcShowCurrentMonthData(String str) {
            LogUtil.d("TAG", "str = " + str);
            if (str.equals("请求成功")) {

            } else if (str.equals("暂无数据")) {
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            rlBottom.addView(emptyview);
                        }
                    });
                }
            } else if (str.equals("您的网络情况不稳定")) {
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.network_unavailable));
//                            rlBottom.addView(emptyview);
                        }
                    });
                }
            }
        }

    }
}
