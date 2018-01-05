package com.changcai.buyer.ui.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.ui.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lufeisong on 2017/9/30.
 */

public class SpecialistFragment extends BaseFragment {

    @BindView(R.id.tv_daka)
    TextView tvDaka;
    private Activity activity;

    @Override
    public void onAttach(Activity activity) {
        this.activity = activity;
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share_specialist, null);
        ButterKnife.bind(this, view);
        initView();
        initListener();
        initData();
        return view;
    }

    @Override
    protected void initSystemStatusBar() {
        super.initSystemStatusBar();
        immersionBar.statusBarDarkFont(true, 0.2f).statusBarColor(R.color.white).fitsSystemWindows(true).init();
    }
    void initView(){

    }
    void initListener(){
        tvDaka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity,PublishActivity.class));
            }
        });
    }
    void initData(){

    }
}
