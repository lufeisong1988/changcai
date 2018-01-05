package com.changcai.buyer.interface_api;

import android.app.DownloadManager;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.changcai.buyer.util.SPUtil;

import java.io.File;

/**
 * Created by liuxingwei on 2016/12/2.
 */

public class DownloadAPKService extends Service{

    private static final String DOWNLOAD_FOLDER_NAME = "maidoupo";
    private DownloadManager dm;
    private long enqueue;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private DownloadServiceBinder binder = new DownloadServiceBinder();

    private class DownloadServiceBinder extends Binder {
        public DownloadAPKService getBinder() {
            return DownloadAPKService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        downLoadAPKFile(intent.getStringExtra("download_url"));
        return START_STICKY;


    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void downLoadAPKFile(String url) {

        File folder = Environment.getExternalStoragePublicDirectory(DOWNLOAD_FOLDER_NAME);
        if (!folder.exists() || !folder.isDirectory()) {
            folder.mkdirs();
        }
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);//wifi环境下下载
        request.setTitle("买豆粕APK下载");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);//通知栏可见
        request.setMimeType("application/vnd.android.package-archive");
        request.setDestinationInExternalPublicDir(DOWNLOAD_FOLDER_NAME, "maidoupo.apk");
        enqueue = dm.enqueue(request);
        SPUtil.saveLong("enqueue",enqueue);

    }
}
