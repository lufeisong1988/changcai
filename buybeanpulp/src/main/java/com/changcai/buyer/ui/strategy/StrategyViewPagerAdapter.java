package com.changcai.buyer.ui.strategy;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by liuxingwei on 2017/7/27.
 */

public class StrategyViewPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private List<Fragment> fragmentList;

    public StrategyViewPagerAdapter(FragmentManager fm, Context mContext, List<Fragment> fragmentList) {
        super(fm);
        this.mContext = mContext;
        this.fragmentList = fragmentList;
    }



    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }


    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }


}
