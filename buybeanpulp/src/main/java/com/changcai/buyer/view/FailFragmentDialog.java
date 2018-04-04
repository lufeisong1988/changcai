package com.changcai.buyer.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.juggist.commonlibrary.rx.RxBus;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by liuxingwei on 2017/6/13.
 */

public class FailFragmentDialog extends BaseBottomDialog {
    Unbinder unbinder;
    @BindView(R.id.tv_refresh)
    TextView tvRefresh;

    @Override
    public int getLayoutRes() {
        return R.layout.empty_error;
    }

    @Override
    public void bindView(View v) {
        unbinder = ButterKnife.bind(this, v);
        RxView.clicks(tvRefresh).throttleFirst(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                RxBus.get().post("errorRefresh",new Boolean(true));
            }
        });
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




}
