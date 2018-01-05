package com.changcai.buyer.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.changcai.buyer.R;
import com.changcai.buyer.ui.introduction.MemberShipIntroductionActivity;
import com.changcai.buyer.util.PicassoImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by liuxingwei on 2017/7/19.
 */

public class AdvertisementDialog extends BaseBottomDialog {
    @BindView(R.id.iv_advertisement)
    ImageView ivAdvertisement;
    @BindView(R.id.iv_cancel)
    ImageView ivCancel;
    Unbinder unbinder;

    @Override
    public int getLayoutRes() {
        return R.layout.advertisement;
    }

    @Override
    public void bindView(View v) {
        unbinder = ButterKnife.bind(this,v);
        PicassoImageLoader.getInstance().displayNetImage(getActivity(),getArguments().getString("advertisement"),ivAdvertisement);
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

    @OnClick({R.id.iv_advertisement, R.id.iv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_advertisement:
                dismiss();
                gotoActivity(MemberShipIntroductionActivity.class,getArguments());
                break;
            case R.id.iv_cancel:
                dismiss();
                break;
        }
    }
}
