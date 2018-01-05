package com.changcai.buyer.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.changcai.buyer.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by liuxingwei on 2017/6/13.
 */

public class MyProgressDialog extends BaseBottomDialog {
    @BindView(R.id.progress)
    RotateDotsProgressView progress;
    Unbinder unbinder;

    @Override
    public int getLayoutRes() {
        return R.layout.my_progress_dialog;
    }

    @Override
    public void bindView(View v) {
        unbinder = ButterKnife.bind(this, v);
        progress.showAnimation(true);
    }

    @Override
    public boolean getCancelOutside() {
        return false;
    }

    @Override
    public float getDimAmount() {
        return 0f;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        progress.refreshDone(true);
        unbinder.unbind();
    }
}
