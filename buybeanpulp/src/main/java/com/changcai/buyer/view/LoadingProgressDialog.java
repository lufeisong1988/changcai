package com.changcai.buyer.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.changcai.buyer.CommonApplication;
import com.changcai.buyer.R;
import com.changcai.buyer.interface_api.ApiCodeErrorException;
import com.changcai.buyer.interface_api.ErrorCode;

/**
 * @author zhoujun
 * @version 1.0
 * @description 加载动画dialog
 * @date 2014年9月5日 上午11:08:34
 */
public class LoadingProgressDialog {

    public static Dialog createLoadingDialog(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.common_loading_dialog, null);// 得到加载view
        /*ImageView bg_loading = (ImageView) v.findViewById(R.id.bg_loading);
        ImageView ivLoading_l = (ImageView) v.findViewById(R.id.ivLoading_l);
		ImageView ivLoading_b = (ImageView) v.findViewById(R.id.ivLoading_b);
		AnimationUtil.lockWise(bg_loading,2500);
		AnimationUtil.antilockWise(ivLoading_l, 2500);
		AnimationUtil.antilockWise(ivLoading_b,2500);*/

        Dialog loadingDialog = new Dialog(context, R.style.dialog);// 创建自定义样式dialog
        loadingDialog.setCanceledOnTouchOutside(false);// 不可以触摸取消
        loadingDialog.setContentView(v);
        return loadingDialog;

    }

    /**
     * 充值界面加载框
     *
     * @param context
     * @return
     */
    public static Dialog rechargeLoadingDialog(Context context) throws NullPointerException {
        if (context == null) throw new NullPointerException();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.recharge_dialog, null);// 得到加载view
        Dialog loadingDialog = new Dialog(context, R.style.dialog);// 创建自定义样式dialog
        loadingDialog.setCanceledOnTouchOutside(false);// 不可以触摸取消
        loadingDialog.setContentView(v);
        return loadingDialog;

    }


}
