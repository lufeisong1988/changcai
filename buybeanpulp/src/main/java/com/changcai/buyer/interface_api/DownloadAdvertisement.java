package com.changcai.buyer.interface_api;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.juggist.commonlibrary.rx.DownloadInstance;

import java.io.File;

import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import zlc.season.rxdownload.RxDownload;
import zlc.season.rxdownload.entity.DownloadStatus;

/**
 * Created by liuxingwei on 2017/7/19.
 */

public class DownloadAdvertisement extends Service {

    private RxDownload rxDownload;
    private Subscription subscription;

    private void initFolder(String makeDirPath) {
        File workDir = new File(makeDirPath);
        if (!workDir.exists()) {
            workDir.mkdirs();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (intent.getExtras().containsKey("advertisement")) {
                startDownload(intent.getExtras().getString("advertisement"));
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void startDownload(final String url) {
        subscription = rxDownload
                .download(url, url + ".png", getApplication().getFilesDir().getPath() + "advertisement")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<DownloadStatus>() {
                    @Override
                    public void call(DownloadStatus downloadStatus) {

                    }
                });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initFolder(getApplication().getFilesDir().getPath() + "advertisement");
        rxDownload = DownloadInstance.getInstance()
                .maxDownloadNumber(5)
                .maxThread(5)
                .maxRetryCount(5)
                .defaultSavePath(getApplication().getFilesDir().getPath() + "advertisement");

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
