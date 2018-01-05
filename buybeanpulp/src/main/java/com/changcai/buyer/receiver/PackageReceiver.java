package com.changcai.buyer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.changcai.buyer.util.LogUtil;

/**
 * Created by liuxingwei on 2017/1/22.
 */

public class PackageReceiver extends BroadcastReceiver {
    private String TAG = PackageReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {

        LogUtil.d(TAG,intent.getDataString()+"");
        LogUtil.d(TAG,intent.getAction().toString()+"");
    }
}
