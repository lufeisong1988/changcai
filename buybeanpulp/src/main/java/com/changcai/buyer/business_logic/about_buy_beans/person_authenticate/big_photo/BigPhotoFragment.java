package com.changcai.buyer.business_logic.about_buy_beans.person_authenticate.big_photo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.changcai.buyer.BaseFragment;
import com.changcai.buyer.R;
import com.changcai.buyer.util.PicassoImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by liuxingwei on 2016/11/30.
 */

public class BigPhotoFragment extends BaseFragment implements BigPhotoContract.View {

    BigPhotoContract.Presenter presenter;

    Unbinder unbinder;
    @BindView(R.id.iv_big_photo)
    ImageView ivBigPhoto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_upload_big_photo, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PicassoImageLoader.getInstance().displayImage(activity,getArguments().getString("path"),ivBigPhoto);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public BigPhotoFragment() {
    }

    public static BigPhotoFragment getInstance() {
        return new BigPhotoFragment();
    }

    @Override
    public void setPresenter(BigPhotoContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showErrorDialog(String message) {

    }
}
