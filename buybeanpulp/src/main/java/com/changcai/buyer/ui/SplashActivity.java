package com.changcai.buyer.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.H5Resources;
import com.changcai.buyer.bean.InitModel;
import com.changcai.buyer.business_logic.about_buy_beans.guidance.GuidanceActivity;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.interface_api.ApiCodeErrorException;
import com.changcai.buyer.interface_api.ApiServiceGenerator;
import com.changcai.buyer.interface_api.DownloadH5Service;
import com.changcai.buyer.interface_api.InitService;
import com.changcai.buyer.interface_api.NetworkResultFunc1;
import com.changcai.buyer.ui.base.BaseActivity;
import com.changcai.buyer.ui.main.MainActivity;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.util.RxUtil;
import com.changcai.buyer.util.SPUtil;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author zhoujun
 * @version 1.0
 * @description 启动页
 * @date 2014年7月28日 下午5:46:40
 */
public class SplashActivity extends BaseActivity {
    private View viewSplash;

    private LocationManager locationManager;
    private String locationProvider;
    private Subscription initSubscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
//            finish();
//            return;
//        }

        setContentView(R.layout.common_splash_activity);
        viewSplash = findViewById(R.id.ivBg);
        checkNewVersion(readH5ResourceProperties());
        PermissionGen.needPermission(SplashActivity.this, 100,
                new String[] {
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                }
        );
        String s = "\\\"";
//        Uri uri = getIntent().getData();
//        if(uri != null){
//            LogUtil.d("TAG","uri:" + uri.toString());
//            if(uri.getBooleanQueryParameter("arg0",false)){
//                String arg0= uri.getQueryParameter("arg0");
//                arg0 = arg0.replaceAll("9","\"");
//                arg0 = arg0.replaceAll("8","/");
//                LogUtil.d("TAG","arg0 = " + arg0);
//                PushManager.getInstance().onNotificationClick(this,arg0);
//            }else{
//                LogUtil.d("TAG","arg0 is empty");
//            }
//        }
    }
    public void initLocation(){
        //获取地理位置管理器
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {

            return;
        }
        //获取Location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            //不为空,显示地理位置经纬度
            SPUtil.saveString(Constants.LOCATION_X,String.valueOf(location.getLatitude()));
            SPUtil.saveString(Constants.LOCATION_Y,String.valueOf(location.getLongitude()));

        }
        //监视地理位置变化
        Log.i("TAG","enter");
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, mLocationListener01);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, mLocationListener02);
    }
    public void checkNewVersion(List<H5Resources> h5ResourcesList) {
        Map<String, String> map = new HashMap<>();
        map.put("h5Resources", new Gson().toJson(h5ResourcesList));
        map.put("registrationId", JPushInterface.getRegistrationID(this));
        LogUtil.d("registrationId", JPushInterface.getRegistrationID(this));

        if (SPUtil.getString(Constants.LOCATION_X) != null && SPUtil.getString(Constants.LOCATION_Y) != null) {
            Map<String, String> locationMap = new HashMap<>();
            locationMap.put("latitude", SPUtil.getString(Constants.LOCATION_X));
            locationMap.put("longitude", SPUtil.getString(Constants.LOCATION_Y));
            map.put("locationInfo", new Gson().toJson(locationMap));
        }
        InitService initService = ApiServiceGenerator.createService(InitService.class);
        initSubscription = initService
                .appInit(map)
                .subscribeOn(Schedulers.io())
                .map(new NetworkResultFunc1<InitModel>())
                .flatMap(new Func1<InitModel, Observable<List<H5Resources>>>() {
                    @Override
                    public Observable<List<H5Resources>> call(InitModel initModel) {
                        SPUtil.saveString(Constants.KEY_CONTACT_PHONE, initModel.getCustomerCenter());
                        SPUtil.saveString(Constants.KEY_UPDATE_LOG, initModel.getUpdateLog());
                        SPUtil.saveboolean(Constants.KEY_NEED_UPDATE, Boolean.parseBoolean(initModel.getNeedUpdate()));
                        if (null != initModel.getAdvertisement()) {
                            String localUrl = SPUtil.getString("advertisement");
                            String netUrl = initModel.getAdvertisement().getImageUrl();
                            if (!netUrl.equalsIgnoreCase(localUrl)) {
                                SPUtil.saveString("advertisement", initModel.getAdvertisement().getImageUrl());
                                SPUtil.saveboolean("needShow",true);
                                SPUtil.saveString("activeUrl",initModel.getAdvertisement().getButUrl());
                            }else{
                                SPUtil.saveboolean("needShow",false);
                            }
                        }
                        if (null != initModel.getH5Resources() && initModel.getH5Resources().size() > 0) {
                            return Observable.just(initModel.getH5Resources());
                        } else {
                            return Observable.error(new ApiCodeErrorException("", "no_update_zip_package"));
                        }
                    }

                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<H5Resources>>() {
                    @Override
                    public void call(List<H5Resources> h5Resources) {
                        ArrayList<H5Resources> h5ResourcesArrayList = (ArrayList<H5Resources>) h5Resources;
                        downZipFile(h5ResourcesArrayList);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        animation();
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        animation();
                    }
                });
        RxUtil.addSubscription(initSubscription);
    }

    private void downZipFile(ArrayList<H5Resources> h5Resources) {
        Intent intent = new Intent(SplashActivity.this, DownloadH5Service.class);
        intent.putParcelableArrayListExtra("downUrlList", h5Resources);
        startService(intent);
    }

    private List<H5Resources> readH5ResourceProperties() {
        List<H5Resources> h5ResourcesList = SPUtil.getObjectFromShare(Constants.KEY_H5_RESOURCE);
        if (null == h5ResourcesList) {
            h5ResourcesList = new ArrayList<>();
            H5Resources h5Resources = new H5Resources();
            H5Resources h5Resources2 = new H5Resources();
            H5Resources h5Resources3 = new H5Resources();
            h5Resources.setName("common");
            h5Resources.setVersion("0.0");
            h5Resources2.setName("calendar");
            h5Resources2.setVersion("0.0");
            h5Resources3.setName("assets");
            h5Resources3.setVersion("0.0");
            h5Resources3.setName("pinganEPay");
            h5Resources3.setVersion("0.1");
            h5ResourcesList.add(h5Resources);
            h5ResourcesList.add(h5Resources2);
            h5ResourcesList.add(h5Resources3);
        } else {
            h5ResourcesList = SPUtil.getObjectFromShare(Constants.KEY_H5_RESOURCE);
        }
        return h5ResourcesList;
    }


    /**
     * 跳转；
     */
    private void animation() {
        Observable.empty().delay(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
                if (SPUtil.getBoolean(Constants.KEY_NOT_FIRST_OPEN_APPLICATION)) {
                    if (getIntent().getExtras() != null) {
                        gotoActivity(MainActivity.class, true, getIntent().getExtras());
//                        gotoActivity(TestActivity.class);
                        overridePendingTransition(R.anim.umeng_socialize_fade_in, R.anim.splash_set);
//
                    } else {
                        gotoActivity(MainActivity.class, true);
//                        gotoActivity(TestActivity.class);
                        overridePendingTransition(R.anim.umeng_socialize_fade_in, R.anim.splash_set);
                    }
                } else {
                    gotoActivity(GuidanceActivity.class, true);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxUtil.remove(initSubscription);
        LogUtil.d("TAG","ondestory");
        if (locationManager != null) {
            //移除监听器
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.removeUpdates(mLocationListener01);
            locationManager.removeUpdates(mLocationListener02);
        }
    }

    public final LocationListener mLocationListener01 = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            SPUtil.saveString(Constants.LOCATION_X,String.valueOf(location.getLatitude()));
            SPUtil.saveString(Constants.LOCATION_Y,String.valueOf(location.getLongitude()));

            Log.i("TAG","x = " + location.getLatitude() + ";y = " + location.getLongitude());
        }


        @Override
        public void onProviderDisabled(String provider) {
            Log.i("TAG","onProviderDisabled:" +provider);
//            updateToNewLocation(null);
        }

        @Override
        public void onProviderEnabled(String provider) {

            Log.i("TAG","onProviderEnabled");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

            Log.i("TAG","onStatusChanged");
        }
    };
    public final LocationListener mLocationListener02 = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            SPUtil.saveString(Constants.LOCATION_X,String.valueOf(location.getLatitude()));
            SPUtil.saveString(Constants.LOCATION_Y,String.valueOf(location.getLongitude()));

            Log.i("TAG","x = " + location.getLatitude() + ";y = " + location.getLongitude());
        }


        @Override
        public void onProviderDisabled(String provider) {
            Log.i("TAG","onProviderDisabled:" +provider);
//            updateToNewLocation(null);
        }

        @Override
        public void onProviderEnabled(String provider) {

            Log.i("TAG","onProviderEnabled");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

            Log.i("TAG","onStatusChanged");
        }
    };

    @PermissionSuccess(requestCode = 100)
    public void doSomethingSucceed(){
        Log.d("TAG","doSomethingSucceed");
        initLocation();

    }
    @PermissionFail(requestCode = 100)
    public void doSomethingFail(){
        Log.d("TAG","doSomethingFail");
    }
}