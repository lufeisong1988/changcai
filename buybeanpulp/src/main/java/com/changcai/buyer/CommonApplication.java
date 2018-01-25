package com.changcai.buyer;

import android.content.Context;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;

import com.changcai.buyer.bean.GetUserLevelBean;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.im.DemoCache;
import com.changcai.buyer.im.NIMInitManager;
import com.changcai.buyer.im.NimSDKOptionConfig;
import com.changcai.buyer.im.chatroom.ChatRoomSessionHelper;
import com.changcai.buyer.im.common.util.crash.AppCrashHandler;
import com.changcai.buyer.im.config.preference.Preferences;
import com.changcai.buyer.im.config.preference.UserPreferences;
import com.changcai.buyer.im.contact.ContactHelper;
import com.changcai.buyer.im.event.DemoOnlineStateContentProvider;
import com.changcai.buyer.im.mixpush.DemoMixPushMessageHandler;
import com.changcai.buyer.im.redpacket.NIMRedPacketClient;
import com.changcai.buyer.im.session.NimDemoLocationProvider;
import com.changcai.buyer.im.session.SessionHelper;
import com.changcai.buyer.listener.ForegroundListener;
import com.changcai.buyer.rx.RxBus;
import com.changcai.buyer.util.AndroidUtil;
import com.changcai.buyer.util.AppInfo;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.TeamMemberOnlineProviderImp;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.UIKitOptions;
import com.netease.nim.uikit.business.contact.core.query.PinYin;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.mixpush.NIMPushClient;
import com.netease.nimlib.sdk.util.NIMUtil;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.socialize.PlatformConfig;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import cn.jpush.android.api.JPushInterface;

//import cn.jpush.android.api.JPushInterface;

//import com.squareup.leakcanary.LeakCanary;

/**
 * 应用全局变量
 *
 * @author Zhoujun
 *         说明：	1、可以缓存一些应用全局变量。比如数据库操作对象
 */
public class CommonApplication extends MultiDexApplication {
    /**
     * Singleton pattern
     */
    private static CommonApplication instance;

    public static CommonApplication getInstance() {
        return instance;
    }


    public Vector<String> articleId = new Vector<>(100);
    //服务会员等级列表
    private GetUserLevelBean userLevelBean;

    public GetUserLevelBean getUserLevelBean() {
        return userLevelBean;
    }

    public void setUserLevelBean(GetUserLevelBean userLevelBean) {
        this.userLevelBean = userLevelBean;
    }

    public long enterTime;
//    public LocationClient mLocationClient = null;
//    public BDLocationListener myListener = new MyLocationListener();


    private Map<String, String> baseRequestParameters = new HashMap<>();


    {
        PlatformConfig.setWeixin("wxa60d98389150dbdb", "021e5238b8cb7cfb6f497400063efe9e");
        PlatformConfig.setQQZone("1105295555", "YIXOnmEDnwZQKu8W");
        PlatformConfig.setSinaWeibo("635533034", "46b96768d36d2f57037e0071c30fa00d", "http://www.maidoupo.com");
    }

//    public class MyLocationListener implements BDLocationListener {
//
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//            //Receive Location
//            StringBuffer sb = new StringBuffer(256);
//            sb.append("time : ");
//            sb.append(location.getTime());
//            sb.append("\nerror code : ");
//            sb.append(location.getLocType());
//            sb.append("\nlatitude : ");
//            sb.append(location.getLatitude());
//            sb.append("\nlontitude : ");
//            sb.append(location.getLongitude());
//            sb.append("\nradius : ");
//            sb.append(location.getRadius());
//            SPUtil.saveObjectToShare(Constants.LOCATION_LAT, location.getLatitude());
//            SPUtil.saveObjectToShare(Constants.LOCATION_LON, location.getLongitude());
//            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
//                sb.append("\nspeed : ");
//                sb.append(location.getSpeed());// 单位：公里每小时
//                sb.append("\nsatellite : ");
//                sb.append(location.getSatelliteNumber());
//                sb.append("\nheight : ");
//                sb.append(location.getAltitude());// 单位：米
//                sb.append("\ndirection : ");
//                sb.append(location.getDirection());// 单位度
//                sb.append("\naddr : ");
//                sb.append(location.getAddrStr());
//                sb.append("\ndescribe : ");
//                sb.append("gps定位成功");
//                //定位成功关掉gps - -
//                mLocationClient.stop();
//
//
//            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
//                sb.append("\naddr : ");
//                sb.append(location.getAddrStr());
//                //运营商信息
//                sb.append("\noperationers : ");
//                sb.append(location.getOperators());
//                sb.append("\ndescribe : ");
//                sb.append("网络定位成功");
//                mLocationClient.stop();
//            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
//                sb.append("\ndescribe : ");
//                sb.append("离线定位成功，离线定位结果也是有效的");
//            } else if (location.getLocType() == BDLocation.TypeServerError) {
//                sb.append("\ndescribe : ");
//                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
//            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
//                sb.append("\ndescribe : ");
//                sb.append("网络不同导致定位失败，请检查网络是否通畅");
//            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
//                sb.append("\ndescribe : ");
//                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
//            }
//            sb.append("\nlocationdescribe : ");
//            sb.append(location.getLocationDescribe());// 位置语义化信息
//            List<Poi> list = location.getPoiList();// POI数据
//            if (list != null) {
//                sb.append("\npoilist size = : ");
//                sb.append(list.size());
//                for (Poi p : list) {
//                    sb.append("\npoi= : ");
//                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
//                }
//            }
//        }
//
//    }

    /**
     * 判断当前版本号是否和 上一个版本号 一致 不一致则添加参数 clientinfo_deviceid
     * 同是更新版本号 和 是否是第一次安装 应对覆盖安装
     */
    private void initBaseParams() {
        if (null == SPUtil.getObjectFromShare(Constants.REQUEST_BASE_PARAMETERS)) {
            baseRequestParameters.put("clientinfo_appname", "买豆粕");//app名称
            baseRequestParameters.put("clientinfo_appversion", AndroidUtil.getAppVersionName(this));//app版本号
            baseRequestParameters.put("clientinfo_os", "Android");
            baseRequestParameters.put("clientinfo_osversion", Build.VERSION.RELEASE + "");//操作系统名称
            baseRequestParameters.put("clientinfo_channel", AndroidUtil.getMetaValue(CommonApplication.getInstance(), "UMENG_CHANNEL"));//渠道号
            baseRequestParameters.put("clientinfo_devicevendor", Build.BRAND);//手机品牌
            baseRequestParameters.put("clientinfo_devicename", Build.MODEL + "/" + Build.VERSION.RELEASE);
            baseRequestParameters.put("clientinfo_deviceid", AndroidUtil.getIMEI(this));
            SPUtil.saveObjectToShare(Constants.REQUEST_BASE_PARAMETERS, baseRequestParameters);
        } else {
            baseRequestParameters = SPUtil.getObjectFromShare(Constants.REQUEST_BASE_PARAMETERS);
            if (!baseRequestParameters.get("clientinfo_appversion").contentEquals(AndroidUtil.getAppVersionName(this))) {
                baseRequestParameters.put("clientinfo_appversion", AndroidUtil.getAppVersionName(this));//app版本号
                baseRequestParameters.put("clientinfo_deviceid", AndroidUtil.getIMEI(this));
                SPUtil.saveObjectToShare(Constants.REQUEST_BASE_PARAMETERS, baseRequestParameters);
                SPUtil.clearObjectFromShare(Constants.KEY_INCREMENT_H5);
                SPUtil.clearObjectFromShare(Constants.KEY_H5_RESOURCE);
                SPUtil.clearObjectFromShare(Constants.KEY_NOT_FIRST_OPEN_APPLICATION);
                SPUtil.clearObjectFromShare(Constants.KEY_NOT_FIRST_GUIDE);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        enterTime = System.currentTimeMillis();
        //捕获系统异常
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());
        instance = this;
//        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        //初始化地图

        AppInfo.createInstance(getApplicationContext());
        if (AppInfo.getInstance().isDebuggable()) {
//            LeakCanary.install(this);
        }

        ForegroundListener.init(this);
        ForegroundListener.get().addListener(new ForegroundListener.Listener() {
            @Override
            public void onBecameForeground() {
                LogUtil.d("CommonApplication", "从后台回到前台");
                RxBus.get().post("backgroundToForeground", new Boolean(true));
            }

            @Override
            public void onBecameBackground() {
                LogUtil.d("CommonApplication", "从前台回到后台");
            }
        });
        getArticleId();
        initInternalStorage(getFilesDir().getPath() + Constants.internal_storage_h5);
        initBaseParams();
//        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
//        mLocationClient.registerLocationListener(myListener);    //注册监听函数
//        initLocation();
//        mLocationClient.start();

        //初始化X5内核
        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                //x5内核初始化完成回调接口，此接口回调并表示已经加载起来了x5，有可能特殊情况下x5内核加载失败，切换到系统内核。

            }

            @Override
            public void onViewInitFinished(boolean b) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.e("@@","加载内核是否成功:"+b);
            }
        });
        /*初始化im*/

        DemoCache.setContext(this);
////        // 注册小米推送，参数：小米推送证书名称（需要在云信管理后台配置）、appID 、appKey，该逻辑放在 NIMClient init 之前
//        NIMPushClient.registerMiPush(this, "DEMO_MI_PUSH", "2882303761517502883", "5671750254883");
////        // 注册华为推送，参数：华为推送证书名称（需要在云信管理后台配置）
//        NIMPushClient.registerHWPush(this, "DEMO_HW_PUSH");

        // 注册自定义推送消息处理，这个是可选项
        NIMPushClient.registerMixPushMessageHandler(new DemoMixPushMessageHandler());
        NIMClient.init(this, getLoginInfo(), NimSDKOptionConfig.getSDKOptions(this));

        // crash handler
        AppCrashHandler.getInstance(this);

        // 以下逻辑只在主进程初始化时执行
        if (NIMUtil.isMainProcess(this)) {
            // 初始化红包模块，在初始化UIKit模块之前执行
            NIMRedPacketClient.init(this);
            // init pinyin
            PinYin.init(this);
            PinYin.validate();
            // 初始化UIKit模块
            initUIKit();
            // 初始化消息提醒
            NIMClient.toggleNotification(UserPreferences.getNotificationToggle());
            // 云信sdk相关业务初始化
            NIMInitManager.getInstance().init(true);
        }

}



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


//    private void initLocation() {
//        LocationClientOption option = new LocationClientOption();
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
//        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
//        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
//        int span = 3000;
//        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
//        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
//        option.setOpenGps(true);//可选，默认false,设置是否使用gps
//        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
//        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
//        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
//        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
//        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
//        mLocationClient.setLocOption(option);
//    }

    private void initInternalStorage(String makeDirPath) {
        LogUtil.d("CommonApplication", getFilesDir().getPath());

        File workDir = new File(makeDirPath);
        if (!workDir.exists()) {
            workDir.mkdirs();
        }
    }


    private void getArticleId() {
        String articleIdString = SPUtil.getString(Constants.KEY_ARTICLE_IDS);
        String[] ids = articleIdString.split("|");
        for (int i = 0; i < ids.length; i++) {
            String id = ids[i];
            if (!TextUtils.isEmpty(id)) {
                articleId.add(id);
            }
        }
    }


    private LoginInfo getLoginInfo() {
        String account = Preferences.getUserAccount();
        String token = Preferences.getUserToken();

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            DemoCache.setAccount(account.toLowerCase());
            return new LoginInfo(account, token);
        } else {
            return null;
        }
    }

    private void initUIKit() {
        // 初始化
        NimUIKit.init(this, buildUIKitOptions());

        // 设置地理位置提供者。如果需要发送地理位置消息，该参数必须提供。如果不需要，可以忽略。
        NimUIKit.setLocationProvider(new NimDemoLocationProvider());

        // IM 会话窗口的定制初始化。
        SessionHelper.init();

        // 聊天室聊天窗口的定制初始化。
        ChatRoomSessionHelper.init();

        // 通讯录列表定制初始化
        ContactHelper.init();

        // 添加自定义推送文案以及选项，请开发者在各端（Android、IOS、PC、Web）消息发送时保持一致，以免出现通知不一致的情况
        //NimUIKit.setCustomPushContentProvider(new DemoPushContentProvider());

        NimUIKit.setOnlineStateContentProvider(new DemoOnlineStateContentProvider());
        NimUIKit.setTeamMemberOnlineProvider(TeamMemberOnlineProviderImp.getInstance());
    }

    private UIKitOptions buildUIKitOptions() {
        UIKitOptions options = new UIKitOptions();
        // 设置app图片/音频/日志等缓存目录
        options.appCacheDir = NimSDKOptionConfig.getAppCacheDir(this) + "/app";
        return options;
    }


}
