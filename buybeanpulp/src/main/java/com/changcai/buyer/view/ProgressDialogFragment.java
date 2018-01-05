package com.changcai.buyer.view;

import android.view.View;

import com.changcai.buyer.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by liuxingwei on 2017/4/24.
 */

public class ProgressDialogFragment extends BaseBottomDialog {
    Unbinder unbinder;

    @Override
    public int getLayoutRes() {
        return R.layout.recharge_dialog_center;
    }

    @Override
    public void bindView(View v) {
        unbinder = ButterKnife.bind(this, v);
    }

    @Override
    public float getDimAmount() {
        return 0.5f;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
