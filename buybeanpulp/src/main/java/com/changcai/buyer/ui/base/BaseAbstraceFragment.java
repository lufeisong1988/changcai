package com.changcai.buyer.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by lufeisong on 2017/10/9.
 */

public abstract class BaseAbstraceFragment extends BaseFragment{
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(setResId(), null);
        ButterKnife.bind(this, view);
        initView();
        initListener();
        initConfig();
        initData();
        return view;
    }
    public abstract int setResId();

    protected void initView(){

    }
    protected void initListener(){

    }
    protected void initConfig(){

    }
    protected void initData(){

    }
}
