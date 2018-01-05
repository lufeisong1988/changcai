package com.changcai.buyer.ui.share;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.changcai.buyer.R;
import com.changcai.buyer.ui.base.BaseAbstraceFragment;
import com.changcai.buyer.view.NavigationTabStrip;

import butterknife.BindView;

/**
 * Created by lufeisong on 2017/9/30.
 * 问问
 */

public class ShareMainFragment extends BaseAbstraceFragment {
    @BindView(R.id.navigation_indicator)
    NavigationTabStrip navigationIndicator;
    @BindView(R.id.fl_share)
    FrameLayout flShare;

    private FragmentManager fm;

    @Override
    public int setResId() {
        return R.layout.fragment_sharemain;
    }

    @Override
    protected void initSystemStatusBar() {
        super.initSystemStatusBar();
        immersionBar.statusBarDarkFont(true, 0.2f).statusBarColor(R.color.white).fitsSystemWindows(true).init();
    }
    @Override
    protected void initListener(){
        navigationIndicator.setOnTabStripSelectedIndexListener(new NavigationTabStrip.OnTabStripSelectedIndexListener() {
            @Override
            public void onStartTabSelected(String title, int index) {
            }

            @Override
            public void onEndTabSelected(String title, int index) {
                showFragment(index);
            }
        });
    }
    @Override
    protected void initData(){
        fm = getChildFragmentManager();
        navigationIndicator.setTabIndex(0);
        showFragment(0);
    }
    void showFragment(int index){
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = null;
        switch (index){
            case 0:
                fragment = new SpecialistFragment();
                break;
            case 1:
                fragment = new TopicFragment();
                break;
            case 2:
                fragment = new QuestionFragment();
                break;
            default:
                fragment = new SpecialistFragment();
                break;
        }
        ft.replace(R.id.fl_share,fragment);
        ft.commitAllowingStateLoss();
    }
}
