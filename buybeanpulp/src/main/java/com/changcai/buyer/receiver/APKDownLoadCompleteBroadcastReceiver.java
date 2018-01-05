package com.changcai.buyer.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

/**
 * Created by liuxingwei on 2016/12/2.
 */

public class APKDownLoadCompleteBroadcastReceiver extends BroadcastReceiver{

    DownloadManager downloadManager;

    private static final String DOWNLOAD_FOLDER_NAME = "maidoupo";
    @Override
    public void onReceive(Context context, Intent intent) {
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        long completeDownLoadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);


        DownloadManager.Query query = new DownloadManager.Query();

        query.setFilterById(completeDownLoadId);


        Cursor cursor =

                downloadManager.query(query);


        if (cursor.moveToFirst()) {
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));

            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                String apkFilePath = new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath())
                        .append(File.separator).append(DOWNLOAD_FOLDER_NAME).append(File.separator)
                        .append("maidoupo.apk").toString();
                install(context, apkFilePath);
            }
        }
    }

    /**
     * install app
     *
     * @param context
     * @param filePath
     * @return whether apk exist
     */
    public static boolean install(Context context, String filePath) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        File file = new File(filePath);
        if (file != null && file.length() > 0 && file.exists() && file.isFile()) {
            i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            return true;
        }
        return false;
    }
}
