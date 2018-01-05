package com.changcai.buyer.ui.main;

import android.Manifest;
import android.animation.AnimatorInflater;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.changcai.buyer.CommonApplication;
import com.changcai.buyer.R;
import com.changcai.buyer.bean.SpaceStatistic;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.common.Urls;
import com.changcai.buyer.http.HttpListener;
import com.changcai.buyer.http.VolleyUtil;
import com.changcai.buyer.im.config.preference.Preferences;
import com.changcai.buyer.im.login.LogoutHelper;
import com.changcai.buyer.permission.RuntimePermission;
import com.changcai.buyer.rx.RxBus;
import com.changcai.buyer.ui.CommonWebViewActivity;
import com.changcai.buyer.ui.login.LoginActivity;
import com.changcai.buyer.ui.news.AdvanceNewsMainFragment;
import com.changcai.buyer.ui.news.NewMainFragment;
import com.changcai.buyer.ui.resource.ResourceMainFragment;
import com.changcai.buyer.ui.strategy.StrategyFragment;
import com.changcai.buyer.util.AppManager;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.ServerErrorCodeDispatch;
import com.changcai.buyer.util.SwichLayoutInterFace;
import com.changcai.buyer.util.SwitchLayout;
import com.changcai.buyer.util.ToastUtil;
import com.changcai.buyer.util.UserDataUtil;
import com.changcai.buyer.view.AdvertisementDialog;
import com.changcai.buyer.view.FragmentTabHost;
import com.changcai.buyer.view.UpdateFragmentDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import cn.jpush.android.api.JPushInterface;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

//import com.changcai.buyer.util.JPushUtil;

//import cn.jpush.android.api.JPushInterface;

/**
 * @author wlv
 * @version 1.0
 * @description 主界面
 * @date 15-12-5 下午1:14
 */
public class MainActivity extends FragmentActivity implements AdvanceNewsMainFragment.Hidden,  RuntimePermission.PermissionCallbacks, SwichLayoutInterFace {

    private FragmentTabHost mTabHost;
    private FrameLayout mainLayout;
    private RelativeLayout rl_guide;
    private String[] mTextviewArray;
    private Class<?> fragmentArray[] = {NewMainFragment.class,StrategyFragment.class, ResourceMainFragment.class,
//            QuoteMainFragment.class,
            MeFragment.class};

    private int mImageViewArray[] = {
            R.drawable.home_bottom_news_bg,
            R.drawable.home_calendar_selector,
            R.drawable.home_bottom_resource_bg,
//            R.drawable.home_bottom_quote_bg,
            R.drawable.home_bottom_me_bg};

    private final static int PAGE_NEWS = 0;// 新闻
    private final static int PAGE_STRATEGY = 1;// 策略
    private final static int PAGE_RESOURCE = 2;// 资源报价
//    private final static int PAGE_QUOTED_PRICE = 3;// 豆粕商城
    private final static int PAGE_ME = 3;// 我


    private int currentTabIndex;
    private Observable<Boolean> logOrOutObservableEvent;
    private Observable<Bundle> receiveJpushObservable;
    private Observable<Integer> switchTabObservable;

    private boolean isCallScaleAnimation;
    private String advertisement;

    @Override
    protected void onStart() {
        super.onStart();

        pushBundle(getIntent());
//        toNews(getIntent());
        LogUtil.d("TAG","start");
    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            advertisementDialog();
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogUtil.d("TAG","onCreate");

        AppManager.getAppManager().addActivity(this);
        initView();
        if (!SPUtil.getBoolean(Constants.KEY_NOT_FIRST_GUIDE)) {
//           rl_guide.setVisibility(View.VISIBLE);
//            rl_guide.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    rl_guide.setVisibility(View.GONE);
//                    return false;
//                }
//            });
            SPUtil.saveboolean(Constants.KEY_NOT_FIRST_GUIDE,true);
        } else {
//           rl_guide.setVisibility(View.GONE);
        }

        checkGPSLocationPermission();
        if (UserDataUtil.isLogin()) {
            getMySpaceStatistic(true);
        }
        logOrOutObservableEvent = RxBus.get().register("inOrOutAction", Boolean.class);
        logOrOutObservableEvent.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (UserDataUtil.isLogin()) {
                            if (aBoolean.booleanValue()) getMySpaceStatistic(false);
                            if (aBoolean.booleanValue() && currentTabIndex == -1)
                                switchFragment(PAGE_STRATEGY);
                        } else {
                            setRedPotVisibility(View.GONE);
                        }
                    }
                });
        receiveJpushObservable = RxBus.get().register(Constants.PUSH_MESSAGE, Bundle.class);
        receiveJpushObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Bundle>() {
            @Override
            public void call(Bundle bundle) {
                Intent intent = new Intent(MainActivity.this, CommonWebViewActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        switchTabObservable = RxBus.get().register("switchTabObservable",Integer.class);
        switchTabObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        switchFragment(integer);
                    }
                });
        advertisement = SPUtil.getString("advertisement");
        boolean needShow = SPUtil.getBoolean("needShow");
        if (!TextUtils.isEmpty(advertisement) && needShow) {
            Picasso.with(this).load(advertisement).into(target);
        }

        toNews(getIntent());
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, true);
    }

    private void advertisementDialog() {
        AdvertisementDialog advertisementDialog = new AdvertisementDialog();
        Bundle bundle = new Bundle();
        bundle.putString("advertisement", advertisement);
        bundle.putString("activeUrl", SPUtil.getString("activeUrl"));
        advertisementDialog.setArguments(bundle);
        advertisementDialog.show(getSupportFragmentManager(), "advertisement");
    }

    private void showOrderFragmentDialog(String orderType, SpaceStatistic spaceStatistic) {
//        if (this.isFinishing()) return;
//        OrderFragmentDialog orderFragmentDialog = new OrderFragmentDialog();
//        Bundle bundle = new Bundle();
//        bundle.putString("orderType", orderType);
//        bundle.putSerializable("orderInfo", spaceStatistic);
//        orderFragmentDialog.setArguments(bundle);
//        orderFragmentDialog.show(getSupportFragmentManager(), "orderFragmentDialog");
    }

    private void checkGPSLocationPermission() {
        if (!RuntimePermission.hasPermissions(this, Manifest.permission.READ_PHONE_STATE
                , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO)) {
            RuntimePermission.requestPermissions(MainActivity.this, getString(R.string.permissions_phone_and_location_storage),
                    Constants.REQUEST_PERMISSION_GROUP,
                    Manifest.permission.READ_PHONE_STATE
                    , Manifest.permission.READ_EXTERNAL_STORAGE
                    , Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ,Manifest.permission.CAMERA
                    ,Manifest.permission.RECORD_AUDIO);
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        LogUtil.d("TAG","onNewIntent");
        super.onNewIntent(intent);
        toNews(intent);
    }

    /**
     * 跳转资讯详情页面
     * @param intent
     */
    private void toNews(Intent intent){
        if (intent != null) {
            int index = intent.getIntExtra("switchFragment", -1);
            LogUtil.d("TAG","index = " + index);
            if (index >= 0) {
                switchFragment(index);
            }
            if (intent.getExtras() != null && intent.getExtras().containsKey("minGrade")) {
                int minGrade = Integer.valueOf(intent.getExtras().getString("minGrade"));
                LogUtil.d("TAG","minGrade = " + minGrade);
                if (minGrade >= 0) {
                    if (UserDataUtil.isLogin()) {
                        RxBus.get().post(Constants.PUSH_MESSAGE, intent.getExtras());
                    } else {
                        Intent intent2 = new Intent(MainActivity.this, LoginActivity.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent2.putExtras(intent.getExtras());
                        intent2.putExtra("pushEvent", true);
                        startActivity(intent2);
                    }
                } else if (minGrade == -1) {
                    RxBus.get().post(Constants.PUSH_MESSAGE, intent.getExtras());
                }
            } else {
                LogUtil.d("TAG","pushBundle");
                pushBundle(intent);
            }
        }
    }
    private void pushBundle(Intent intent) {
        if (intent == null) return;
        if (intent.getExtras() != null && intent.getExtras().containsKey("folder")) {
            LogUtil.d("TAG", "onNewIntent");
            if (intent.getExtras().containsKey("tabIndex")) {
                Constants.TABINDEX = Integer.parseInt(intent.getExtras().getString("tabIndex"));
            }
            if (intent.getExtras().containsKey("folder")) {
                Constants.FOLDER = Integer.parseInt(intent.getExtras().getString("folder"));
            }
            switchFragment(PAGE_STRATEGY);
        }
    }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initView() {
        rl_guide = (RelativeLayout) findViewById(R.id.rl_guide);
        mainLayout = (FrameLayout) findViewById(R.id.mainLayout);
        mTextviewArray = getResources().getStringArray(R.array.main_item);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.mainLayout);
        mTabHost.getTabWidget().setDividerDrawable(R.color.white);
        int count = fragmentArray.length;
        for (int i = 0; i < count; i++) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i])
                    .setIndicator(getTabItemView(i));
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            final int j = i;
            mTabHost.getTabWidget().getChildAt(i)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switchFragment(j);
                        }
                    });
        }

        if (SPUtil.getBoolean(Constants.KEY_NEED_UPDATE)) {
            showNewUpdateVersion(SPUtil.getString(Constants.KEY_UPDATE_LOG));
        }

    }


    public void showNewUpdateVersion(String content) {

        if (this.isFinishing()) {
            return;
        }

        UpdateFragmentDialog updateFragmentDialog = new UpdateFragmentDialog();
        Bundle content2 = new Bundle();
        content2.putString("content", content);
        updateFragmentDialog.setArguments(content2);
        updateFragmentDialog.show(getSupportFragmentManager(), "update");
    }

    /**
     * 显示拨打电话的dialog
     */
    public void showChooseDialog(final String phone) {
        View view = getLayoutInflater().inflate(R.layout.layout_quoto_phone, null);
        final Dialog dialog = new Dialog(this, R.style.whiteFrameWindowStyle);
        dialog.setContentView(view);

        TextView tvPhone = (TextView) view.findViewById(R.id.phone);
        tvPhone.setText(getString(R.string.service_phone) + phone);
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });

        TextView tvCancel = (TextView) view.findViewById(R.id.cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.choosed_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        wl.gravity = Gravity.BOTTOM;
        dialog.onWindowAttributesChanged(wl);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public void switchFragment(int index) {
        setCurrentTab(index);
    }

    private void setCurrentTab(int target) {
        switch (target) {
            case PAGE_NEWS:
                mTabHost.setCurrentTab(target);
                currentTabIndex = target;
                NewMainFragment newMainFragment = (NewMainFragment) getSupportFragmentManager().findFragmentByTag(mTextviewArray[target]);
                newMainFragment.updateDot();
                break;
//            case PAGE_SHARE:
//                mTabHost.setCurrentTab(target);
//                currentTabIndex = target;
//                break;
            case PAGE_RESOURCE:
                mTabHost.setCurrentTab(target);
                currentTabIndex = target;
                break;
//            case PAGE_QUOTED_PRICE:
//                mTabHost.setCurrentTab(target);
//                currentTabIndex = target;
//                break;
            case PAGE_ME:
                mTabHost.setCurrentTab(target);
                currentTabIndex = target;
                if (UserDataUtil.isLogin()) {
                    MeFragment fragment = (MeFragment) getSupportFragmentManager()
                            .findFragmentByTag(mTextviewArray[target]);
                    if (fragment != null) {
                        fragment.getMySpaceStatistic();
                    }
                }
                break;
            case PAGE_STRATEGY:
                if (UserDataUtil.isLogin()) {
                    mTabHost.setCurrentTab(target);
                    currentTabIndex = target;
                    if (Constants.TABINDEX != -1) {
                        LogUtil.d("TAG","TABINDEX != -1");
                        StrategyFragment strategyFragment = (StrategyFragment) getSupportFragmentManager().findFragmentByTag("策略");
                        if (strategyFragment != null) {
                            strategyFragment.setCurrentPage(Constants.TABINDEX);
                        }
                    }else{
                        LogUtil.d("TAG","TABINDEX == -1");
                        StrategyFragment strategyFragment = (StrategyFragment) getSupportFragmentManager().findFragmentByTag("策略");
                        if (strategyFragment != null) {
                            strategyFragment.clearCache();
                        }
                    }
                } else {
                    currentTabIndex = -1;
                    Intent intent2 = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent2);
                }
                break;
            default:
                break;
        }
    }

    private View getTabItemView(int index) {
        ViewGroup viewx = null;
        View view = LayoutInflater.from(this).inflate(R.layout.home_tab_item_view, viewx);
        ImageView imageView = (ImageView) view
                .findViewById(R.id.iv_table_image);
        imageView.setImageResource(mImageViewArray[index]);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setStateListAnimator(AnimatorInflater.loadStateListAnimator(this, R.animator.animator_selector));
        }
        TextView textView = (TextView) view.findViewById(R.id.tv_table_info);
        textView.setText(mTextviewArray[index]);
        return view;
    }


    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.d("jpush_reId", JPushInterface.getRegistrationID(this));
        if (isCallScaleAnimation) {
            setEnterSwichLayout();
            isCallScaleAnimation = false;
        }
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private long exitTime;

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count <= 0) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtil.showShort(this, "再按一次退出" + getString(R.string.app_name));
                exitTime = System.currentTimeMillis();
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Map<String, String> stringStringMap = new HashMap<>();
                stringStringMap.put("account", UserDataUtil.isLogin() ? UserDataUtil.userMobile() : "未登录用户");
                stringStringMap.put("memberGrade", UserDataUtil.isLogin() ? UserDataUtil.userGradeName() : "未登录用户");
                stringStringMap.put("enterTime", simpleDateFormat.format(new Date(CommonApplication.getInstance().enterTime)));
                stringStringMap.put("exitTime", simpleDateFormat.format(new Date(exitTime)));
                stringStringMap.put("BrowsingTime ", System.currentTimeMillis() - CommonApplication.getInstance().enterTime + "(毫秒)");
                MobclickAgent.onEvent(this, "launchApp", stringStringMap);
                AppManager.getAppManager().AppExit(this);
            }
            return;
        } else if (onKeyBackListener != null) {
            onKeyBackListener.onKeyBack();
            return;
        }
        super.onBackPressed();
    }

    private String TAG = MainActivity.class.getSimpleName();

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.REQUEST_PERMISSION_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showChooseDialog(SPUtil.getString(Constants.KEY_CONTACT_PHONE));
                } else {
                    Toast.makeText(this, R.string.perssion_for_call, Toast.LENGTH_LONG).show();
                }
                break;

            case Constants.REQUEST_PERMISSION_GROUP:
                for (int i = 0; i < grantResults.length; i++) {
                    LogUtil.d(TAG, grantResults[i] + "");
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, R.string.perssion_for_group, Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                break;
        }
    }

    @Override
    public void onPermissionsGranted(List<String> perms) {
        showChooseDialog(SPUtil.getString(Constants.KEY_CONTACT_PHONE));
    }

    @Override
    public void onPermissionsDenied(List<String> perms) {
        Toast.makeText(this, R.string.perssion_for_call, Toast.LENGTH_LONG).show();
    }





    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_DOWN) {
//            if (slidingMenu.isMenuShowing()) {
//                toggle();
//            } else {
//                showMenu();
//            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private OnKeyBackListener onKeyBackListener;

    public void setOnKeyBackListener(OnKeyBackListener onKeyBackListener) {
        this.onKeyBackListener = onKeyBackListener;
    }

    @Override
    public void hiddenTab() {
        mTabHost.setVisibility(View.INVISIBLE);
    }
    @Override
    public void setEnterSwichLayout() {
        SwitchLayout.ScaleBig(this, false, new LinearInterpolator(), 300);
    }

    @Override
    public void setExitSwichLayout() {
        isCallScaleAnimation = true;
        SwitchLayout.ScaleSmall(this, false, new LinearInterpolator(), 150);
    }



    public interface OnKeyBackListener {
        void onKeyBack();
    }


    @Override
    protected void onDestroy() {
        AppManager.getAppManager().finishActivity(this);
        saveArticleId(CommonApplication.getInstance().articleId);
        RxBus.get().unregister("inOrOutAction", logOrOutObservableEvent);
        RxBus.get().unregister(Constants.PUSH_MESSAGE, receiveJpushObservable);
        RxBus.get().unregister("switchTabObservable", switchTabObservable);
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, false);
        super.onDestroy();

    }

    private void saveArticleId(Vector<String> v) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < v.size(); i++) {
            stringBuffer.append(v.get(i));
            if (i != v.size() - 1) {
                stringBuffer.append("|");
            }
        }
        SPUtil.saveString(Constants.KEY_ARTICLE_IDS, stringBuffer.toString());
    }

    public void getMySpaceStatistic(final boolean isApplicationStart) {
        HashMap<String, String> params = new HashMap<>();
        params.put("tokenId", UserDataUtil.getTokenId());
        VolleyUtil.getInstance().httpPost(this, Urls.GET_MY_SPACE_STATISTIC, params, new HttpListener() {
            @Override
            public void onResponseJson(JsonObject response) {
                super.onResponseJson(response);
                String errorCode = response.get(Constants.RESPONSE_CODE).getAsString();
                VolleyUtil.getInstance().cancelProgressDialog();
                if (errorCode.equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {
                    SpaceStatistic spaceStatistic = new Gson().fromJson(response.get(Constants.RESPONSE_CONTENT).getAsJsonObject(), new TypeToken<SpaceStatistic>() {
                    }.getType());
                    setMeTabHostRedPot(spaceStatistic, isApplicationStart);
                } else {
                    ServerErrorCodeDispatch.getInstance().checkErrorCode(MainActivity.this, errorCode, response.get(Constants.RESPONSE_DESCRIPTION).getAsString());
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                VolleyUtil.getInstance().cancelProgressDialog();
            }
        }, false);
    }

    public void setMeTabHostRedPot(SpaceStatistic spaceStatistic, boolean isApplicationStart) {
        if (spaceStatistic == null) return;
        if ((!TextUtils.isEmpty(spaceStatistic.getNewBuyConfirming()) && Integer.parseInt(spaceStatistic.getNewBuyConfirming()) > 0)
                || (!TextUtils.isEmpty(spaceStatistic.getNewBuyDepositing()) && Integer.parseInt(spaceStatistic.getNewBuyDepositing()) > 0)
                || (!TextUtils.isEmpty(spaceStatistic.getOpenBuyPick()) && Integer.parseInt(spaceStatistic.getOpenBuyPick()) > 0)
                || (!TextUtils.isEmpty(spaceStatistic.getBuyDeliveryUnpay()) && Integer.parseInt(spaceStatistic.getBuyDeliveryUnpay()) > 0)
                || (!TextUtils.isEmpty(spaceStatistic.getBuyDeliveryPayed()) && Integer.parseInt(spaceStatistic.getBuyDeliveryPayed()) > 0)
                ) {
            setRedPotVisibility(View.VISIBLE);
            //签署合同优先级最高
            if (isApplicationStart) {
                if (!TextUtils.isEmpty(spaceStatistic.getNewBuyConfirming()) && Integer.parseInt(spaceStatistic.getNewBuyConfirming()) > 0) {
                    showOrderFragmentDialog("newBuyConfirming", spaceStatistic);//待签署合同 newBuyConfirming
                } else if (!TextUtils.isEmpty(spaceStatistic.getAllPayOrder()) && Integer.parseInt(spaceStatistic.getAllPayOrder()) > 0) {
                    showOrderFragmentDialog("allPayOrder", spaceStatistic);//待确认收货
                } else if (!TextUtils.isEmpty(spaceStatistic.getFastPayOrder()) && Integer.parseInt(spaceStatistic.getFastPayOrder()) > 0) {
                    showOrderFragmentDialog("fastPayOrder", spaceStatistic);//提货单 未支付
                }
//                else if (!TextUtils.isEmpty(spaceStatistic.getNewBuyDepositing())&& Integer.parseInt(spaceStatistic.getNewBuyDepositing()) > 0) {
//                    showOrderFragmentDialog("newBuyDepositing",spaceStatistic);//待支付定金 newBuyDepositing
//                }
                else if (!TextUtils.isEmpty(spaceStatistic.getReserveDepositPayOrder()) && Integer.parseInt(spaceStatistic.getReserveDepositPayOrder()) > 0) {
                    showOrderFragmentDialog("reserveDepositPayOrder", spaceStatistic);//
                } else if (!TextUtils.isEmpty(spaceStatistic.getDeliveryOrder()) && Integer.parseInt(spaceStatistic.getDeliveryOrder()) > 0) {
                    showOrderFragmentDialog("deliveryOrder", spaceStatistic);//支付货款
                }
            }
        } else {
            setRedPotVisibility(View.GONE);
        }
    }

    private void setRedPotVisibility(int visibility) {
        View redView2 = mTabHost.getTabWidget().getChildTabViewAt(mImageViewArray.length - 1);
        redView2.findViewById(R.id.iv_red_dot).setVisibility(visibility);
    }

/**
 * 网易云信 账号 状态监听
 */
    /**
     * 用户状态变化
     */
    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {

        @Override
        public void onEvent(StatusCode code) {
            if (code.wontAutoLogin()) {
                kickOut(code);
            } else {
                if (code == StatusCode.NET_BROKEN) {

                } else if (code == StatusCode.UNLOGIN) {

                } else if (code == StatusCode.CONNECTING) {

                } else if (code == StatusCode.LOGINING) {

                } else {
                }
            }
        }
    };

    private void kickOut(StatusCode code) {
        Preferences.saveUserToken("");

        if (code == StatusCode.PWD_ERROR) {
            LogUtil.e("Auth", "user password error");
        } else {
            LogUtil.i("Auth", "Kicked!");
        }
        onLogout();
    }

    // 注销
    private void onLogout() {
        // 清理缓存&注销监听&清除状态
        LogoutHelper.logout();

    }

    public void change(View view){
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
}
