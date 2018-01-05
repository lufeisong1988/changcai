package com.changcai.buyer.ui.share;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.changcai.buyer.R;
import com.changcai.buyer.ui.base.BaseFragment;
import com.changcai.buyer.view.XListView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lufeisong on 2017/9/30.
 */

public class QuestionFragment extends BaseFragment {
    @BindView(R.id.mListView)
    XListView mListView;
    private Activity activity;

    @Override
    public void onAttach(Activity activity) {
        this.activity = activity;
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share_question, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initSystemStatusBar() {
        super.initSystemStatusBar();
        immersionBar.statusBarDarkFont(true, 0.2f).statusBarColor(R.color.white).fitsSystemWindows(true).init();
    }
}
