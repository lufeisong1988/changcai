package com.changcai.buyer.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.changcai.buyer.R;
import com.changcai.buyer.common.Constants;
import com.juggist.commonlibrary.rx.RxBus;
import com.changcai.buyer.ui.base.BaseActivity;
import com.changcai.buyer.util.AndroidUtil;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.util.NetUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.SwichLayoutInterFace;
import com.changcai.buyer.util.SwitchLayout;
import com.changcai.buyer.util.ToastUtil;
import com.changcai.buyer.util.UserDataUtil;
import com.changcai.buyer.view.RotateDotsProgressView;
import com.changcai.buyer.view.ShareFragmentDialog;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

//import android.webkit.JavascriptInterface;
//import android.webkit.WebView;

//import android.webkit.WebChromeClient;

//import android.webkit.CookieManager;

//import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.AppIndex;
//import com.google.android.gms.appindexing.Thing;
//import com.google.android.gms.common.api.GoogleApiClient;

/**
 * 共有的webview
 * Created by zhoujun on 15/6/23.
 */
public class CommonWebViewActivity extends BaseActivity implements ShareFragmentDialog.onDismissListener, View.OnClickListener ,SwichLayoutInterFace {
    private com.tencent.smtt.sdk.WebView webView;
    private LinearLayout ll_empty_view;
    private String title;
    private String url;
    private String info;
    private ShareFragmentDialog shareFragmentDialog;
    private String summary;
    private boolean isError;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
//    private GoogleApiClient client;

    //手指上下滑动时的最小速度
    private static final int YSPEED_MIN = 1000;

    //手指向右滑动时的最小距离
    private static final int XDISTANCE_MIN = 250;

    //手指向上滑或下滑时的最小距离
    private static final int YDISTANCE_MIN = 300;

    //记录手指按下时的横坐标。
    private float xDown;

    //记录手指按下时的纵坐标。
    private float yDown;

    //记录手指移动时的横坐标。
    private float xMove;

    //记录手指移动时的纵坐标。
    private float yMove;

    //用于计算手指滑动的速度。
    private VelocityTracker mVelocityTracker;

    private uiHandler hander;
    private Bundle b;

    private Observable<String> shareObservable;
    private UMWeb umWeb;
    private RotateDotsProgressView progressView;
    private long startBroseTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_webview);
        initTitle();
        hander = new uiHandler(CommonWebViewActivity.this);
        initView();
        initSubscriber();
        setEnterSwichLayout();
        startBroseTime = System.currentTimeMillis();
    }


    private void initSubscriber() {
        shareObservable = RxBus.get().register("share", String.class);
        shareObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {

                switch (s) {
                    case "qq":
                        new ShareAction(CommonWebViewActivity.this).setPlatform(SHARE_MEDIA.QQ).withMedia(umWeb).setCallback(umShareListener).share();
                        break;
                    case "wechat":
                        new ShareAction(CommonWebViewActivity.this).setPlatform(SHARE_MEDIA.WEIXIN).withMedia(umWeb).setCallback(umShareListener).share();

                        break;
                    case "friends":
                        new ShareAction(CommonWebViewActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).withMedia(umWeb).setCallback(umShareListener).share();
                        break;
                    case "sina":
                        new ShareAction(CommonWebViewActivity.this).setPlatform(SHARE_MEDIA.SINA).withMedia(umWeb).setCallback(umShareListener).share();
                        break;
                }

            }
        });
    }



    private void initView() {
        progressView = (RotateDotsProgressView) findViewById(R.id.progress);
        findViewById(R.id.tv_empty_action).setOnClickListener(this);
        titleView.setBackgroundResource(R.color.white);
        b = getIntent().getExtras();
        tvTitle.setVisibility(View.GONE);
        ll_empty_view = (LinearLayout) findViewById(R.id.ll_empty_view);
        ll_empty_view.setOnClickListener(this);
        webView = (com.tencent.smtt.sdk.WebView) findViewById(R.id.webview);
        getBundleInfo(b);
        synCookies(url);
        initWebview();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.appicon_186);
//        final UMImage image = new UMImage(CommonWebViewActivity.this, "http://www.umeng.com/images/pic/social/integrated_3.png");
        final UMImage image = new UMImage(this, bitmap);
        umWeb = new UMWeb(url);
        umWeb.setThumb(image);
        umWeb.setTitle(b.getString("bannerTitle") == null ? title : b.getString("bannerTitle"));
        btnLeft.setBackgroundResource(R.drawable.icon_nav_clos);
        btnLeft.setVisibility(View.INVISIBLE);
        btnLeft.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        iv_btn_right.setVisibility(View.VISIBLE);
        iv_btn_right.setImageResource(R.drawable.share_image_drawable);
        iv_btn_right.setOnClickListener(this);
    }

    private void getBundleInfo(Bundle bundle) {
        if (bundle != null) {
            url = bundle.getString("url","");
            info = bundle.getString("info","");
            if (bundle.getBoolean("isRequestData",false)) {
            } else {
                loadUrl(bundle.getString("url",""));
            }
            title = getIntent().getStringExtra("title");
        }
    }


    private UMShareListener umShareListener = new UMShareListener() {

        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }


        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(CommonWebViewActivity.this, platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CommonWebViewActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (t.getMessage().contains("没有安装应用")) {
                if (platform.compareTo(SHARE_MEDIA.WEIXIN) == 0 || platform.compareTo(SHARE_MEDIA.WEIXIN_CIRCLE) == 0) {
                    ToastUtil.showShort("您没有安装微信");
                } else if (platform.compareTo(SHARE_MEDIA.QQ) == 0) {
                    ToastUtil.showShort("您没有安装QQ");
                }
            } else {
                Toast.makeText(CommonWebViewActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(CommonWebViewActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        Log.d("result", "onActivityResult");
    }


    /**
     * 加载url
     *
     * @param url
     */
    private void loadUrl(String url) {
        webView.loadUrl(url);
    }


    @SuppressWarnings("deprecation")
    public void synCookies(String eachEveryUrl) {
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeAllCookies(new com.tencent.smtt.sdk.ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean aBoolean) {

                }
            });
        } else {
            cookieManager.removeAllCookie();
        }
        if (TextUtils.isEmpty(SPUtil.getString(Constants.KEY_TOKEN_ID))) {
            cookieManager.setCookie(eachEveryUrl, "MAIDOUPO_TOKEN" + "=" + AndroidUtil.getAppVersionName(this));
        } else {
            cookieManager.setCookie(eachEveryUrl, "MAIDOUPO_TOKEN" + "=" + SPUtil.getString(Constants.KEY_TOKEN_ID));
        }
        String CookieStr = cookieManager.getCookie(url);
    }

    /**
     * 初试化webview
     */
    @SuppressLint("JavascriptInterface")
    private void initWebview() {
        com.tencent.smtt.sdk.WebSettings webSettings = webView.getSettings();
        // webSettings.setBuiltInZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setDomStorageEnabled(true);
//        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        String   model= android.os.Build.MODEL;
        String carrier= android.os.Build.MANUFACTURER;
        LogUtil.d("MobilePhoneInfo",carrier+": "+model);
        if (model.contains("MI 3")){
            webSettings.setTextZoom(300);
        }
        webView.setScrollbarFadingEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.requestFocus();// 使WebView内的输入框等获得焦点
        //获取标题
        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(com.tencent.smtt.sdk.WebView view, String htmlTitle) {
                super.onReceivedTitle(view, htmlTitle);
//                if (!TextUtils.isEmpty(title)) {
//                    tvTitle.setText(title);
//                } else {
//                    tvTitle.setText(htmlTitle);
//                }
            }


        };
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JsResultInterface(hander), "ResultModel");
//        webView.setWebChromeClient(wvcc);

//        webView.setWebViewClient(new WebViewClient() {
//            // 点击网页里面的链接还是在当前的webView内部跳转，不跳转外部浏览器
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                if (url.startsWith("http:") || url.startsWith("https:")) {
//                    synCookies(url);
//                    CommonWebViewActivity.this.url = url;
//                    view.loadUrl(url);
//                    return true;
//                } else {
//                    // Otherwise allow the OS to handle things like tel, mailto, etc.
//                    try {
//                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                        startActivity(intent);
//                        return true;
//                    } catch (ActivityNotFoundException e) {
//                        MobclickAgent.reportError(CommonWebViewActivity.this, e);
//                    }
//
//                }
//                return false;
//            }
//            // 可以让webView处理https请求
//
//            @Override
//            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
////                super.onReceivedSslError(webView, sslErrorHandler, sslError);
//                sslErrorHandler.proceed();
//
//            }
//
//            public void onLoadResource(WebView view, String url) {
//
//            }
//
//
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//                //修改3.1.0crash android.view.WindowManager$BadTokenException: Unable to add window -- token android.os.BinderProxy@435a85c8 is not valid; is your activity running?
//                try {
//                    isError =false;
//                    if (CommonWebViewActivity.this.isFinishing())
//                        return;
//                    showProgress();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                //修改3.1.0crash android.view.WindowManager$BadTokenException: Unable to add window -- token android.os.BinderProxy@435a85c8 is not valid; is your activity running?
//                try {
//                    if (view.canGoBack())
//                    btnLeft.setVisibility(View.VISIBLE);
//                    if (CommonWebViewActivity.this.isFinishing())
//                        return;
//                    dismissProgress();
//                    if (isError){
////                        view.removeAllViews();
////                        view.addView(ll_empty_view);
////                        ll_empty_view.setVisibility(View.VISIBLE);
//                        view.setVisibility(View.GONE);
//                        ll_empty_view.setVisibility(View.VISIBLE);
//                    }else{
//                        view.setVisibility(View.VISIBLE);
//                        ll_empty_view.setVisibility(View.GONE);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                // mErrorView.setVisibility(View.VISIBLE);
//                isError = true;
//                dismissProgress();
//                showShortToast(getString(R.string.net_work_exception));
//            }
//        });
        webView.setWebViewClient(new com.tencent.smtt.sdk.WebViewClient(){
            // 点击网页里面的链接还是在当前的webView内部跳转，不跳转外部浏览器

            @Override
            public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView view, String url) {
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    synCookies(url);
                    CommonWebViewActivity.this.url = url;
                    view.loadUrl(url);
                    return true;
                } else {
                    // Otherwise allow the OS to handle things like tel, mailto, etc.
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    } catch (ActivityNotFoundException e) {
                        MobclickAgent.reportError(CommonWebViewActivity.this, e);
                    }

                }
                return false;
            }
            // 可以让webView处理https请求


            @Override
            public void onReceivedSslError(com.tencent.smtt.sdk.WebView webView, com.tencent.smtt.export.external.interfaces.SslErrorHandler sslErrorHandler, com.tencent.smtt.export.external.interfaces.SslError sslError) {
                sslErrorHandler.proceed();
                LogUtil.d("webView","onReceivedSslError");
            }

            public void onLoadResource(com.tencent.smtt.sdk.WebView view, String url) {

            }


            @Override
            public void onPageStarted(com.tencent.smtt.sdk.WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //修改3.1.0crash android.view.WindowManager$BadTokenException: Unable to add window -- token android.os.BinderProxy@435a85c8 is not valid; is your activity running?
                try {
                    isError =false;
                    if (CommonWebViewActivity.this.isFinishing())
                        return;
                    showProgress();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageFinished(com.tencent.smtt.sdk.WebView view, String url) {
                super.onPageFinished(view, url);
                //修改3.1.0crash android.view.WindowManager$BadTokenException: Unable to add window -- token android.os.BinderProxy@435a85c8 is not valid; is your activity running?
                try {
                    if (view.canGoBack())
                        btnLeft.setVisibility(View.VISIBLE);
                    if (CommonWebViewActivity.this.isFinishing())
                        return;
                    dismissProgress();
                    if (isError){
//                        view.removeAllViews();
//                        view.addView(ll_empty_view);
//                        ll_empty_view.setVisibility(View.VISIBLE);
                        view.setVisibility(View.GONE);
                        ll_empty_view.setVisibility(View.VISIBLE);
                    }else{
                        view.setVisibility(View.VISIBLE);
                        ll_empty_view.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onReceivedError(com.tencent.smtt.sdk.WebView view, int errorCode, String description, String failingUrl) {
                // mErrorView.setVisibility(View.VISIBLE);
                isError = true;
                dismissProgress();
                showShortToast(getString(R.string.net_work_exception));
            }
        });
    }

    private void showProgress() {
        progressView.setVisibility(View.VISIBLE);
        progressView.showAnimation(true);
        webView.setVisibility(View.GONE);
        ll_empty_view.setVisibility(View.GONE);
    }

    private void dismissProgress() {
        progressView.setVisibility(View.GONE);
        progressView.refreshDone(true);
        SwitchLayout.getFadingIn(this);
        webView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null){
            getBundleInfo(intent.getExtras());
            synCookies(url);
            loadUrl(url);
        }
    }

    @Override
    public void onBackPressed() {
        if (webView != null) {
            if (webView.canGoBack()) {
                webView.goBack();
                return;
            }
        }
        super.onBackPressed();
    }

    protected void overridePendingTransitionExit() {
        overridePendingTransition(0, R.anim.slide_to_right);
    }

    @Override
    public void finish() {
        //解决webviewbug start
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        //解决webviewbug end
        if (webView != null) {
            if (webView.canGoBack()) {
                webView.goBack();
                return;
            }
        }
        super.finish();
        setExitSwichLayout();
    }

    @Override
    public void shareDismiss() {
        iv_btn_right.setSelected(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_empty_view:
            case R.id.tv_empty_action:
                if (b != null) {
                    url = b.getString("url");
                    info = b.getString("info");
                    if (b.getBoolean("isRequestData")) {
                        getHtmlData(b.getString("url"));
                    } else {
                        loadUrl(b.getString("url"));
                    }
                }
                break;

            case R.id.btnLeft:
                CommonWebViewActivity.this.finish();
                break;
            case R.id.btnBack:
                onBackPressed();
                break;
            case R.id.iv_btn_right:
                iv_btn_right.setSelected(true);
                if (shareFragmentDialog == null) {
                    shareFragmentDialog = new ShareFragmentDialog();
                }
                shareFragmentDialog.show(getSupportFragmentManager(), "share");
                break;

        }
    }

    @Override
    public void setEnterSwichLayout() {
//        if (getIntent().getExtras().containsKey("percentY")){
//            Float percentY = getIntent().getExtras().getFloat("percentY");
//            Logger.d(percentY+"percentY");
//            SwitchLayout.ScaleToBigVerticalIn(this, false, new LinearInterpolator(),getIntent().getFloatExtra("percentY",0.5f));
//        }else {
//            SwitchLayout.ScaleToBigVerticalIn(this, false, new LinearInterpolator());
//        }

    }

    @Override
    public void setExitSwichLayout() {
//        SwitchLayout.getSlideToRight(this,true,null);
        overridePendingTransitionExit();
    }



    //    activityCloseExitAnimation

    private static class uiHandler extends Handler {
        private WeakReference<CommonWebViewActivity> activityWeakReference;

        public uiHandler(CommonWebViewActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CommonWebViewActivity activity = activityWeakReference.get();
            if (null != activity) {
                switch (msg.what) {
                    case 3:
                        Bundle bundle = new Bundle();
                        bundle.putString("picUrl", msg.obj.toString());
                        activity.gotoActivity(BigPhotoActivity.class, bundle);
                        break;
                }
            }
        }
    }

    /**
     * 获取html数据加载
     */
    private void getHtmlData(String url) {
    }

    private BroadcastReceiver connectivityChangeReceiver = new BroadcastReceiver() {

        @Override
        @TargetApi(Build.VERSION_CODES.CUPCAKE)
        public void onReceive(Context context, Intent intent) {
            // 将当前的网络状态发送给webView，使WebView支持HTML5的网络监测，如: navigator.onLine
            // document.addEventListener("online", fn, false)
            // document.addEventListener("offline", fn, false)
            webView.setNetworkAvailable(NetUtil.checkNet(context));
            if (!NetUtil.checkNet(context)) {
                webView.setVisibility(View.GONE);
                ll_empty_view.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if (connectivityChangeReceiver != null) {
            unregisterReceiver(connectivityChangeReceiver);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(
                "android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(connectivityChangeReceiver, intentFilter);
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
//    public Action getIndexApiAction() {
//        Thing object = new Thing.Builder()
//                .setName("CommonWebView Page") // TODO: Define a title for the content shown.
//                // TODO: Make sure this auto-generated URL is correct.
//                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
//                .build();
//        return new Action.Builder(Action.TYPE_VIEW)
//                .setObject(object)
//                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
//                .build();
//    }
    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        AppIndex.AppIndexApi.end(client, getIndexApiAction());
//        client.disconnect();
    }


    /**
     * 创建VelocityTracker对象，并将触摸content界面的滑动事件加入到VelocityTracker当中。
     *
     * @param event
     */
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 回收VelocityTracker对象。
     */
    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    /**
     * 获取手指在content界面滑动的速度。
     *
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        return Math.abs(velocity);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        createVelocityTracker(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = ev.getRawX();
                yDown = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                xMove = ev.getRawX();
                yMove = ev.getRawY();
                //滑动的距离
                int distanceX = (int) (xMove - xDown);
                int distanceY = (int) (yMove - yDown);
                //获取顺时速度
                int ySpeed = getScrollVelocity();
                //关闭Activity需满足以下条件：
                //1.x轴滑动的距离>XDISTANCE_MIN
                //2.y轴滑动的距离在YDISTANCE_MIN范围内
                //3.y轴上（即上下滑动的速度）<XSPEED_MIN，如果大于，则认为用户意图是在上下滑动而非左滑结束Activity
                if (distanceX > XDISTANCE_MIN && (distanceY < YDISTANCE_MIN && distanceY > -YDISTANCE_MIN) && ySpeed < YSPEED_MIN) {
                    finish();
                }
                break;
            case MotionEvent.ACTION_UP:
                recycleVelocityTracker();
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private class JsResultInterface {

        private uiHandler uiHandler;

        public JsResultInterface(uiHandler handler) {
            this.uiHandler = handler;
        }

        /**
         * Show a toast from the web page
         */
        @JavascriptInterface
        public void jsCallResult(String object) {
            Message message = Message.obtain();
            message.what = 3;
            message.obj = object;
            uiHandler.sendMessage(message);
        }

        @JavascriptInterface
        public void receivedUrlInfo(String object) {
            LogUtil.d("CommonWebViewActivity", object.toString() + "-----receivedUrlInfo");
            try {
                JSONObject jsonObject = new JSONObject(object);
                Observable.just(jsonObject)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<JSONObject>() {
                            @Override
                            public void call(JSONObject jsonObject) {
//                                tvTitle.setText(jsonObject.optString("folderName"));
                                summary = jsonObject.optString("summary");
                                title = jsonObject.optString("title");
                                umWeb.setTitle(title);
                                umWeb.setDescription(summary);

                            }
                        });
            } catch (JSONException e) {
                e.printStackTrace();

            }

        }

    }

   

    @Override
    protected void onDestroy() {
        Map<String,String> stringStringMap = new HashMap<>();
        stringStringMap.put("account", UserDataUtil.isLogin()?UserDataUtil.userMobile():"未登录用户");
        stringStringMap.put("articelId", getIntent().getExtras().getString("articleId","id"));
        stringStringMap.put("memberGrade",UserDataUtil.isLogin()?UserDataUtil.userGradeName():"未登录用户");
        stringStringMap.put("BrowsingTime ", System.currentTimeMillis()-startBroseTime+"(毫秒)");
        MobclickAgent.onEvent(this,"dailyInfo",stringStringMap);
        UMShareAPI.get(this).release();
        if(webView != null){
//            //1.解决魅族审核不通过
//            webView.getSettings().setBuiltInZoomControls(true);
//            webView.setVisibility(View.GONE);// 把destroy()延后
//            long timeout = ViewConfiguration.getZoomControlsTimeout();
//            new Timer().schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    // TODO Auto-generated method stub
//                    webView.destroy();
//                }
//            }, timeout);
            //2.一般方法
            webView.destroy();
        }
//        webView.loadUrl("http://www.maidoupo.com");//防止关闭页面，h5页面的音乐仍在播放
//        if (webView != null) {
//            if (webView.canGoBack()) {
//                webView.goBack();
//                return;
//            }
//        }
        RxBus.get().unregister("share",shareObservable);
        super.onDestroy();
    }


}
