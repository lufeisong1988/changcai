package com.changcai.buyer.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.changcai.buyer.ui.login.LoginActivity;
import com.changcai.buyer.view.ConfirmDialog;
import com.changcai.buyer.view.MyAlertDialog;

/**
 * Created by liuxingwei on 2017/1/24.
 */

public class ServerErrorCodeDispatch {


    private  static volatile ServerErrorCodeDispatch serverErrorCodeDispatch;

    public static ServerErrorCodeDispatch getInstance() {
        if (serverErrorCodeDispatch == null) {
            synchronized (ServerErrorCodeDispatch.class) {
                serverErrorCodeDispatch = new ServerErrorCodeDispatch();
            }
        }
        return serverErrorCodeDispatch;
    }

    public void checkErrorCode(Context context, String errorCode, String message) {
        if (TextUtils.isEmpty(errorCode)) {
            ConfirmDialog.createConfirmDialog(context, TextUtils.isEmpty(message) ? "未知错误" : message);
            return;
        }
        switch (errorCode) {
            case "101":
                if (!TextUtils.isEmpty(message)) {
                    ToastUtil.showShort(message);
                }
                UserDataUtil.clearUserData();
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setClass(context, LoginActivity.class);
                context.startActivity(intent);
                break;
            default:
                if (!TextUtils.isEmpty(message)) {
                    ConfirmDialog.createConfirmDialog(context, message);
                } else {
                    ConfirmDialog.createConfirmDialog(context, "未知错误");
                }
                break;
        }
    }

    public void checkHttpServerError(Context context, String message) {
        ToastUtil.showShort(context, message);
    }


    public void showNetErrorDialog(Context context, String errorString,@Nullable String errorCode){
        if (tokenEfficacy(context, errorCode)) return;
        showAlert((FragmentActivity) context, errorString);
    }

    private void showAlert(FragmentActivity context, String errorString) {
        if (context.isFinishing())return;
        MyAlertDialog myAlertDialog = new MyAlertDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title",errorString);
        myAlertDialog.setArguments(bundle);
        myAlertDialog.show( context.getSupportFragmentManager());
    }

    public void showNetErrorDialog(Context context, String errorString){
        showAlert((FragmentActivity) context, errorString);
    }

    private boolean tokenEfficacy(Context context, String errorCode) {
        if (!TextUtils.isEmpty(errorCode) && errorCode.contentEquals("101")){
            UserDataUtil.clearUserData();
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setClass(context, LoginActivity.class);
            context.startActivity(intent);
            return true;
        }
        return false;
    }
}
