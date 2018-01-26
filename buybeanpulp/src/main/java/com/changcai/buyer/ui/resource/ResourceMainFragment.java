package com.changcai.buyer.ui.resource;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.changcai.buyer.R;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.rx.RxBus;
import com.changcai.buyer.ui.base.BaseFragment;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.view.NavigationTabStrip;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by lufeisong on 2017/8/30.
 * 资源报价
 */

public class ResourceMainFragment extends BaseFragment {

    @BindView(R.id.navigation_resource_indicator)
    NavigationTabStrip navigationResourceIndicator;
    @BindView(R.id.iv_resource_phone)
    ImageView ivResourcePhone;
    @BindView(R.id.fl_resource_container)
    FrameLayout flResourceContainer;

    private final int PAGE_QUOTEMAP = 0;
    private final int PAGE_QUOTREND = 1;

    private final String TGA_PAGE_ONE = "PAGE_ONE";

    private android.support.v4.app.FragmentManager fm;
    private Unbinder unbinder;
    private Activity activity;
    private Observable<Boolean> backgroundToForegroundEvent;//从后台回到前台

    private Fragment fragment_quotemap, fragment_quoteindex;
    private QuoteTrendFragment fragment_quotetrend;
//    private int CALL_PHONE = 991;

    @Override
    public void onAttach(Activity activity) {
        this.activity = activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backgroundToForegroundEvent = RxBus.get().register("backgroundToForeground", Boolean.class);
        backgroundToForegroundEvent.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_resourcemain, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    protected void initSystemStatusBar() {
        super.initSystemStatusBar();
        immersionBar.statusBarDarkFont(true, 0.2f).statusBarColor(R.color.white).fitsSystemWindows(true).init();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initListener();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

//        RxUtil.remove(moreFiltrateSubscription);
        RxBus.get().unregister("backgroundToForeground", backgroundToForegroundEvent);
    }

    private void init() {
        fm = getChildFragmentManager();
        switchFragment(0);
        navigationResourceIndicator.setTabIndex(0);
        ivResourcePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionGen.needPermission(ResourceMainFragment.this, 100,
                        new String[] {
                                Manifest.permission.CALL_PHONE,
                        }
                );
            }
        });

    }
    @PermissionSuccess(requestCode = 100)
    public void doSomethingSucceed(){
        showChooseDialog(SPUtil.getString(Constants.KEY_CONTACT_PHONE));

    }
    @PermissionFail(requestCode = 100)
    public void doSomethingFail(){
        Toast.makeText(getActivity(), R.string.perssion_for_call, Toast.LENGTH_LONG).show();
    }


    private void initListener() {
        navigationResourceIndicator.setOnTabStripSelectedIndexListener(new NavigationTabStrip.OnTabStripSelectedIndexListener() {
            @Override
            public void onStartTabSelected(String title, int index) {
                LogUtil.d("TAG", "start title = " + title + ";index = " + index);
            }

            @Override
            public void onEndTabSelected(String title, int index) {
                switchFragment(index);
                LogUtil.d("TAG", "end title = " + title + ";index = " + index);
            }
        });
    }

    private void switchFragment(int index) {
        FragmentTransaction ft = fm.beginTransaction();
        switch (index) {
            case PAGE_QUOTEMAP:
                if (fragment_quotemap == null) {
                    fragment_quotemap = new QuoteMapFragment();
                    ft.add(R.id.fl_resource_container, fragment_quotemap);
                } else {
                    ft.show(fragment_quotemap);

                }
                if (fragment_quotetrend != null) {
                    ft.hide(fragment_quotetrend);
                }
                if (fragment_quoteindex != null) {
                    ft.hide(fragment_quoteindex);
                }
                ft.commit();
                break;
            case PAGE_QUOTREND:
                if (fragment_quotetrend == null) {
                    fragment_quotetrend = new QuoteTrendFragment();
                    ft.add(R.id.fl_resource_container, fragment_quotetrend);
                } else {
                    ft.show(fragment_quotetrend);
                    fragment_quotetrend.initData();
                }
                if (fragment_quotemap != null) {
                    ft.hide(fragment_quotemap);
                }
                if(fragment_quoteindex != null){
                    ft.hide(fragment_quoteindex);
                }
                ft.commit();
                break;
//            case PAGE_QUOTEINDEX:
//                if(fragment_quoteindex == null){
//                    fragment_quoteindex = new QuoteIndexFragment();
//                    ft.add(R.id.fl_resource_container, fragment_quoteindex);
//                }else{
//                    ft.show(fragment_quoteindex);
//
//                }
//
//                if (fragment_quotemap != null) {
//                    ft.hide(fragment_quotemap);
//                }
//                if(fragment_quotetrend != null){
//                    ft.hide(fragment_quotetrend);
//                }
//                ft.commit();
//                break;
        }
    }

}
