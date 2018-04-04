package com.changcai.buyer.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.changcai.buyer.R;
import com.juggist.commonlibrary.rx.RxBus;
import com.changcai.buyer.util.AndroidUtil;
import com.changcai.buyer.util.RxUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by liuxingwei on 2017/6/26.
 */

public class ShareFragmentDialog extends BaseBottomDialog {
    @BindView(R.id.ll_share_to_qq)
    LinearLayout llShareToQq;
    @BindView(R.id.ll_share_to_wechat)
    LinearLayout llShareToWechat;
    @BindView(R.id.ll_share_to_friends)
    LinearLayout llShareToFriends;
    @BindView(R.id.ll_share_to_sina)
    LinearLayout llShareToSina;
    Unbinder unbinder;
    private onDismissListener dismissListener;

    public interface  onDismissListener{
        public void shareDismiss();
    }
    @Override
    public int getLayoutRes() {
        return R.layout.share_item;
    }

    @Override
    public void bindView(View rootView) {
        unbinder = ButterKnife.bind(this, rootView);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        params.dimAmount = getDimAmount();
        //此处不要用view的宽高去决定参数，因为view的生命周期方法和fragment什么周期方法不同步 view.width或者view.getmesureHeight都为0
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = getResources().getDimensionPixelOffset(R.dimen.dim300);
        params.gravity = Gravity.BOTTOM;
        params.y = getResources().getDimensionPixelOffset(R.dimen.dim101);


        window.setAttributes(params);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dismissListener = (onDismissListener) context;
    }



    @Override
    public float getDimAmount() {
        return 0;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ll_share_to_qq, R.id.ll_share_to_wechat, R.id.ll_share_to_friends, R.id.ll_share_to_sina})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_share_to_qq:
                RxBus.get().post("share", "qq");
                dismiss();
                break;
            case R.id.ll_share_to_wechat:
                RxBus.get().post("share", "wechat");
                dismiss();
                break;
            case R.id.ll_share_to_friends:
                RxBus.get().post("share", "friends");
                dismiss();
                break;
            case R.id.ll_share_to_sina:
                RxBus.get().post("share", "sina");
                dismiss();
                break;
        }
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (dismissListener!=null){
            dismissListener.shareDismiss();
        }
    }
}
