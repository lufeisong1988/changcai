package com.changcai.buyer.interface_api;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.changcai.buyer.CommonApplication;
import com.changcai.buyer.bean.H5Resources;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.rx.DownloadInstance;
import com.changcai.buyer.util.FileUtil;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.util.RxUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.asop.ZipUtil;


import java.io.File;
import java.util.ArrayList;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import zlc.season.rxdownload.RxDownload;
import zlc.season.rxdownload.entity.DownloadStatus;

/**
 * Created by liuxingwei on 2016/12/26.
 */

public class DownloadH5Service extends Service {

    private DownloadBinder downloadBinder;

    private RxDownload rxDownload;
    private Subscription subscription;
    private ArrayList<H5Resources> h5Resources;


    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d("DownloadH5Service", "onCreate()");
        downloadBinder = new DownloadBinder();
        rxDownload = DownloadInstance.getInstance()
                .maxDownloadNumber(5)
                .maxThread(5)
                .maxRetryCount(5)
                .defaultSavePath(getApplication().getFilesDir().getPath() + Constants.internal_storage_h5);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            h5Resources = intent.getParcelableArrayListExtra("downUrlList");
            if (h5Resources !=null)
            startDown(h5Resources);
        }
        LogUtil.d("DownloadH5Service", "onStartCommand()");
        return START_STICKY;
    }


    private void startDown(final ArrayList<H5Resources> h5Resources) {
        subscription = rx.Observable.from(h5Resources)
                .flatMap(new Func1<H5Resources, rx.Observable<DownloadStatus>>() {
                    @Override
                    public rx.Observable<DownloadStatus> call(H5Resources h5Resources) {
                        LogUtil.d("DownloadH5Service", h5Resources.getUrl());
                        return rxDownload.download(h5Resources.getUrl(), h5Resources.getName() + ".zip", getApplication().getFilesDir().getPath() + Constants.internal_storage_h5);
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<DownloadStatus>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.d("DownloadH5Service", "onCompleted");
                        FileUtil.deleteFile(new File(getApplication().getFilesDir().getPath() + Constants.internal_storage_h5 + File.separator + ".cache"));
                        rx.Observable.from(h5Resources)
                                .observeOn(Schedulers.immediate())
                                .subscribeOn(Schedulers.immediate())
                                .subscribe(new Action1<H5Resources>() {
                                    @Override
                                    public void call(H5Resources h5Resources) {
                                        LogUtil.d("DownloadH5Service", h5Resources.getUrl());
                                        ZipUtil.unpack(new File(getApplication().getFilesDir().getPath() + Constants.internal_storage_h5 + File.separator + h5Resources.getName() + ".zip"), new File(getApplication().getFilesDir().getPath() + Constants.internal_storage_h5));
                                        FileUtil.deleteFile(new File(getApplication().getFilesDir().getPath() + Constants.internal_storage_h5 + File.separator + h5Resources.getName() + ".zip"));
                                    }
                                }, new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        SPUtil.saveboolean(Constants.KEY_INCREMENT_H5, false);
                                        stopSelf();
                                    }
                                }, new Action0() {
                                    @Override
                                    public void call() {
                                        SPUtil.saveObjectToShare(Constants.KEY_H5_RESOURCE, h5Resources);
                                        stopSelf();
                                        SPUtil.saveboolean(Constants.KEY_INCREMENT_H5, true);
                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {
                        SPUtil.saveboolean(Constants.KEY_INCREMENT_H5, false);
                        stopSelf();
                    }

                    @Override
                    public void onNext(DownloadStatus downloadStatus) {
                        LogUtil.d("DownloadH5Service", downloadStatus.getFormatStatusString());
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxUtil.remove(subscription);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return downloadBinder;
    }

    public class DownloadBinder extends Binder {
        public DownloadH5Service getService() {
            return DownloadH5Service.this;
        }
    }
}
