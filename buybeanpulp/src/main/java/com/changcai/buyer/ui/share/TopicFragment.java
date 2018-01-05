package com.changcai.buyer.ui.share;

import com.changcai.buyer.R;
import com.changcai.buyer.ui.base.BaseAbstraceFragment;
import com.changcai.buyer.ui.share.adapter.TopicAdapter;
import com.changcai.buyer.view.XListView;

import butterknife.BindView;

/**
 * Created by lufeisong on 2017/9/30.
 */

public class TopicFragment extends BaseAbstraceFragment {

    @BindView(R.id.mListView)
    XListView mListView;

    private TopicAdapter adapter;
    @Override
    public int setResId() {
        return R.layout.fragment_share_topic;
    }

    @Override
    protected void initSystemStatusBar() {
        super.initSystemStatusBar();
        immersionBar.statusBarDarkFont(true, 0.2f).statusBarColor(R.color.white).fitsSystemWindows(true).init();
    }

    @Override
    protected void initData() {
        adapter = new TopicAdapter(activity);
        mListView.setAdapter(adapter);
//        super.initData();
    }
}
