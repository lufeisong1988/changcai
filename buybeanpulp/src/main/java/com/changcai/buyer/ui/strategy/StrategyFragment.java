package com.changcai.buyer.ui.strategy;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.AuthsBean;
import com.changcai.buyer.bean.StrategyInitModel;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.interface_api.service_model.StratgyServiceInterface;
import com.changcai.buyer.interface_api.service_model.base.ServiceRequestCallback;
import com.changcai.buyer.interface_api.service_model.imp.StratgyServiceImp;
import com.changcai.buyer.rx.RxBus;
import com.changcai.buyer.ui.base.BaseFragment;
import com.changcai.buyer.util.RxUtil;
import com.changcai.buyer.view.CustomViewPager;
import com.changcai.buyer.view.FontCache;
import com.changcai.buyer.view.MyAlertDialog;
import com.changcai.buyer.view.RotateDotsProgressView;
import com.changcai.buyer.view.indicator.MagicIndicator;
import com.changcai.buyer.view.indicator.ViewPagerHelper;
import com.changcai.buyer.view.indicator.commonnavigator.CommonNavigator;
import com.changcai.buyer.view.indicator.commonnavigator.abs.CommonNavigatorAdapter;
import com.changcai.buyer.view.indicator.commonnavigator.abs.IPagerIndicator;
import com.changcai.buyer.view.indicator.commonnavigator.abs.IPagerTitleView;
import com.changcai.buyer.view.indicator.commonnavigator.indicators.LinePagerIndicator;
import com.changcai.buyer.view.indicator.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by liuxingwei on 2017/7/26.
 * 头寸管理
 */

public class StrategyFragment extends BaseFragment {


    @BindView(R.id.navigation_indicator)
    MagicIndicator navigationIndicator;
    @BindView(R.id.mViewPager)
    CustomViewPager mViewPager;
    @BindView(R.id.progress)
    RotateDotsProgressView progress;


    private CommonNavigator tabCommonNavigator;
    private CommonNavigatorAdapter tabCommonNavigatorAdapter;
    private PromptGoodsFragment promptgoodsFragment;
    private StraddleFragment straddleFragment;
    private CashReportFragment cashReportFragment;
    private Unbinder baseUnbinder;
    protected List<Fragment> fragments;
    private Subscription subscription;
    private int lastPage = 0;
    public static int viewPagerCurrentItem = 0;//当前页卡位置

    private Observable<Boolean> recallObserable;
    private Observable<Boolean> rxBusReprotClear;
    private Observable<Void> rxBusStrategyRefresh;
    private Observable<Boolean> logOrOutObservableEvent;

    private StratgyServiceInterface service;
    boolean requestAble = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.strategy_layout, container, false);
        baseUnbinder = ButterKnife.bind(this, view);
        recallObserable = RxBus.get().register("recall", Boolean.class);
        recallObserable.subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            mViewPager.setCurrentItem(lastPage);
                        }
                    });
                }
            }
        });
        rxBusReprotClear = RxBus.get().register(Constants.REPORT_CLEAR, Boolean.class);
        rxBusReprotClear.subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            mViewPager.setCurrentItem(0);
                        }
                    });
                    cashReportFragment.clearCache();
                }
            }
        });
        rxBusStrategyRefresh = RxBus.get().register(Constants.STRATEGY_REFRESH,Void.class);
        rxBusStrategyRefresh.subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                verifyPermission();
            }
        });
        logOrOutObservableEvent = RxBus.get().register("inOrOutAction", Boolean.class);
        logOrOutObservableEvent.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        verifyPermission();
                    }
                });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initFragments();
        initViewPager();
        initIndicator();
        initData();
    }

    @Override
    public void onStart() {
        super.onStart();
        verifyPermission();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && requestAble) {
            verifyPermission();
        }
    }

    public void showErrorDialog(String message) {
        MyAlertDialog alertDialog = new MyAlertDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", message);
        bundle.putInt("icon", R.drawable.icon_alt_fail);
        alertDialog.setArguments(bundle);
        alertDialog.show(getChildFragmentManager(), "alert");
    }

    private void initFragments() {
        fragments = new ArrayList<>(3);
        promptgoodsFragment = new PromptGoodsFragment();
        new PromptGoodsPresenter(promptgoodsFragment);
        straddleFragment = new StraddleFragment();
        new PromptGoodsPresenter(straddleFragment);
        cashReportFragment = new CashReportFragment();
        fragments.add(promptgoodsFragment);
        fragments.add(straddleFragment);
        fragments.add(cashReportFragment);
    }

    private void initIndicator() {
        tabCommonNavigator = new CommonNavigator(getContext());
        tabCommonNavigatorAdapter = new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return getTabItemArrays().length;
            }

            @SuppressWarnings("deprecation")
            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(getContext());
                simplePagerTitleView.setNormalTypeface();
                simplePagerTitleView.setSelectedTypeface(FontCache.getTypeface("ping_fang_light.ttf", getActivity()));
                simplePagerTitleView.setText(getTabItemArrays()[index]);
                simplePagerTitleView.setPadding(getResources().getDimensionPixelSize(R.dimen.dim15));
                simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.black));
                simplePagerTitleView.setNormalColor(getResources().getColor(R.color.storm_gray));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @SuppressWarnings("deprecation")
            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(getContext());
                linePagerIndicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                linePagerIndicator.setLineWidth(getResources().getDimension(R.dimen.dim20));
                linePagerIndicator.setLineHeight(getResources().getDimension(R.dimen.dim5));
                linePagerIndicator.setStartInterpolator(new AccelerateInterpolator(2.5f));
                linePagerIndicator.setEndInterpolator(new DecelerateInterpolator(2.5f));
                linePagerIndicator.setColors(getResources().getColor(R.color.color_26272A));
                return linePagerIndicator;
            }
        };

        RelativeLayout.LayoutParams navigationIndicatorLayoutParams = (RelativeLayout.LayoutParams) navigationIndicator.getLayoutParams();
        navigationIndicator.setLayoutParams(navigationIndicatorLayoutParams);
        tabCommonNavigator.setAdapter(tabCommonNavigatorAdapter);
        navigationIndicator.setNavigator(tabCommonNavigator);
        ViewPagerHelper.bind(navigationIndicator, mViewPager);
    }


    private void initViewPager() {
        RelativeLayout.LayoutParams mViewPagerLayoutParams = (RelativeLayout.LayoutParams) mViewPager.getLayoutParams();
        mViewPager.setScanScroll(false);
        mViewPager.setLayoutParams(mViewPagerLayoutParams);
        mViewPager.setAdapter(new StrategyViewPagerAdapter(getChildFragmentManager(), getContext(), fragments));
        mViewPager.setCurrentItem(0);
        mViewPager.setPageTransformer(true, new StereoPagerTransformer());
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                viewPagerCurrentItem = position;
                if (position == 2) {
                    cashReportFragment.initData();
                } else {
                    lastPage = position;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void initData() {
        service = new StratgyServiceImp();
    }

    @Override
    protected void initSystemStatusBar() {
        super.initSystemStatusBar();
        immersionBar.statusBarDarkFont(true, 0.2f).statusBarColor(R.color.white).fitsSystemWindows(true).init();
    }

    private String[] getTabItemArrays() {
        return getResources().getStringArray(R.array.strategy_item);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        activity = null;
        baseUnbinder.unbind();
        RxUtil.remove(subscription);
        RxBus.get().unregister("recall", recallObserable);
        RxBus.get().unregister(Constants.REPORT_CLEAR, rxBusReprotClear);
        RxBus.get().unregister(Constants.STRATEGY_REFRESH, rxBusStrategyRefresh);
        RxBus.get().unregister("inOrOutAction", logOrOutObservableEvent);
    }

    public void setCurrentPage(int index) {
        if (index < 0 || index > getTabItemArrays().length - 1) return;
        mViewPager.setCurrentItem(index);
        verifyPermission();

    }

    public void clearCache() {

    }

    private void verifyPermission() {
        service.strategyInit(new ServiceRequestCallback<StrategyInitModel>() {
            @Override
            public void onSucceed(StrategyInitModel strategyInitModel) {
                requestAble = false;
                Bundle bundle = new Bundle();
                bundle.putSerializable("StrategyInitModel", strategyInitModel);
                promptgoodsFragment.setAuthenticationBundle(bundle);
                straddleFragment.setAuthenticationBundle(bundle);
                for (AuthsBean authsBean : strategyInitModel.getAuths()) {
                    if (authsBean.getMenu().equalsIgnoreCase("spot")) {
                        promptgoodsFragment.requestPermissionFromServer(authsBean.isAuthority());
                    }
                    if (authsBean.getMenu().equalsIgnoreCase("arbitrage")) {
                        straddleFragment.requestPermissionFromServer(authsBean.isAuthority());
                    }
                }
            }

            @Override
            public void onFail(String error) {
                requestAble = true;
                showErrorDialog(getString(R.string.net_error));
                promptgoodsFragment.requestNetError();
                straddleFragment.requestNetError();
            }

            @Override
            public void onError() {
                requestAble = true;
                showErrorDialog(getString(R.string.net_error));
                promptgoodsFragment.requestNetError();
                straddleFragment.requestNetError();
            }
        });
    }
}


