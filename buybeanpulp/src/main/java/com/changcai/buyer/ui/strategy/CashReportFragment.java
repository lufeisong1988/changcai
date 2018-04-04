package com.changcai.buyer.ui.strategy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.changcai.buyer.BaseFragment;
import com.changcai.buyer.R;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.interface_api.ApiServiceGenerator;
import com.changcai.buyer.interface_api.BaseApiModel;
import com.juggist.commonlibrary.rx.RxBus;
import com.changcai.buyer.ui.strategy.api.StrategyService;
import com.changcai.buyer.ui.strategy.bean.IsExistBean;
import com.changcai.buyer.ui.strategy.bean.SalesAmountBean;
import com.changcai.buyer.util.AndroidUtil;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.ServerErrorCodeDispatch;
import com.changcai.buyer.view.AlertDialog;
import com.changcai.buyer.view.FontCache;
import com.changcai.buyer.view.RotateDotsProgressView;
import com.changcai.buyer.view.indicator.FragmentContainerHelper;
import com.changcai.buyer.view.indicator.MagicIndicator;
import com.changcai.buyer.view.indicator.commonnavigator.CommonNavigator;
import com.changcai.buyer.view.indicator.commonnavigator.abs.CommonNavigatorAdapter;
import com.changcai.buyer.view.indicator.commonnavigator.abs.IPagerIndicator;
import com.changcai.buyer.view.indicator.commonnavigator.abs.IPagerTitleView;
import com.changcai.buyer.view.indicator.commonnavigator.indicators.WrapPagerIndicator;
import com.changcai.buyer.view.indicator.commonnavigator.titles.EnterTextChangedPagerTitleView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by lufeisong on 2017/9/2.
 * 头寸报表
 */

@SuppressLint("ParcelCreator")
public class CashReportFragment extends BaseFragment implements OnClickListener {

    @BindView(R.id.prompt_indicator)
    MagicIndicator promptIndicator;
    @BindView(R.id.fl_cashreport_container)
    FrameLayout flCashreportContainer;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.progress)
    RotateDotsProgressView progress;
    private FragmentContainerHelper containerHelper = new FragmentContainerHelper();

    String[] items = new String[3];
    CommonNavigatorAdapter commonNavigatorAdapter;

    FragmentManager fm;

    CashReportOneFragment cashReportOneFragment;
    CashReportTwoFragment cashReportTwoFragment;
    CashReportThreeFragment cashReportThreeFragment;
    private int currentIndex= 0;
    /**
     * 头寸报表的数据
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public static int currentMonthPosition = -1;//当前选中的月份位置 （-1 初始化）
    public static IsExistBean isExistBean;
    public static List<String> XList = new ArrayList<>();//x轴数据
    public static List<String> YList = new ArrayList<>();//Y轴数据
    public static SalesAmountBean.DateStrBean dateStr;//可选月份

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        LogUtil.d("TAG", "three onResume");
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cashreport, container, false);

        baseUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtil.d("TAG", "three created");
        init();
        initListViewHeader();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void init() {
        fm = getChildFragmentManager();

    }

    public void initName() {
        if (isExistBean != null && isExistBean.isIsExist()) {
            tvName.setText(isExistBean.getEnterName());
        }
    }

    private void initListViewHeader() {
        items = getResources().getStringArray(R.array.cash_report);
        initIndicator(items);
    }


    public void initIndicator(final String[] items) {


        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        commonNavigatorAdapter = new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return items.length;
            }

            @SuppressWarnings("deprecation")
            @Override
            public IPagerTitleView getTitleView(final Context context, final int index) {
                LogUtil.d("TAG", "index = " + index);
                EnterTextChangedPagerTitleView clipPagerTitleView = new EnterTextChangedPagerTitleView(context);
                clipPagerTitleView.setSelectedColor(getResources().getColor(R.color.white));
                clipPagerTitleView.setNormalColor(getResources().getColor(R.color.gray_chateau));
                clipPagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                clipPagerTitleView.setText(items[index]);
                clipPagerTitleView.setPadding(getResources().getDimensionPixelSize(R.dimen.dim49));
                clipPagerTitleView.setTypeface(FontCache.getTypeface("ping_fang_light.ttf", context));
                clipPagerTitleView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        containerHelper.handlePageSelected(index);
                        LogUtil.d("CashReportFragment","show1");
                        showFragment(index,true);
                    }

                });


                return clipPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                WrapPagerIndicator wrapPagerIndicator = new WrapPagerIndicator(context);
                wrapPagerIndicator.setHorizontalPadding(AndroidUtil.dip2px(context, 18));
                wrapPagerIndicator.setVerticalPadding(AndroidUtil.dip2px(context, 14));
                wrapPagerIndicator.setDrawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bg_nav_fuc_shadow));
                return wrapPagerIndicator;
            }
        };
        commonNavigator.setAdapter(commonNavigatorAdapter);
        promptIndicator.setNavigator(commonNavigator);
        containerHelper.attachMagicIndicator(promptIndicator);
    }


    @Override
    public void onClick(View v) {

    }

    public void initData() {
        if(isExistBean == null){
            FragmentTransaction ft = fm.beginTransaction();
            if(cashReportOneFragment != null){
                ft.remove(cashReportOneFragment);
            }
            if(cashReportTwoFragment != null){
                ft.remove(cashReportTwoFragment);
            }
            if(cashReportThreeFragment != null){
                ft.remove(cashReportThreeFragment);
            }
            ft.commit();
            cashReportOneFragment = null;
            cashReportTwoFragment = null;
            cashReportThreeFragment = null;
            progress.setVisibility(View.VISIBLE);
            progress.showAnimation(true);
            StrategyService strategyService = ApiServiceGenerator.createService(StrategyService.class);
            Map<String, String> map = SPUtil.getObjectFromShare(Constants.REQUEST_BASE_PARAMETERS);
            map.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
            strategyService.getIsExist(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<BaseApiModel<IsExistBean>>() {

                        @Override
                        public void call(BaseApiModel<IsExistBean> isExistBeanBaseApiModel) {
                            progress.setVisibility(View.GONE);
                            progress.refreshDone(true);
                            isExistBean = isExistBeanBaseApiModel.getResultObject();
                            if (!isExistBean.isIsExist()) {
                                if (activity != null) {
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            new AlertDialog(activity).builder()
                                                    .setTitle("不存在风险管理系统账号,请开通后再试!")
                                                    .show();
                                        }
                                    });
                                    RxBus.get().post("recall", true);
                                }
                            } else {
                                RxBus.get().post("recall", false);
                                initName();
                                LogUtil.d("CashReportFragment","show3");
                                showFragment(0,false);
                            }

                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            if (activity != null) {
                                ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.network_unavailable));
                                RxBus.get().post("recall", true);
                                progress.setVisibility(View.GONE);
                                progress.refreshDone(true);
                            }

                        }
                    });
        }else{
            if(!isExistBean.isIsExist()){
                new AlertDialog(activity).builder()
                        .setTitle("不存在风险管理系统账号,请开通后再试!")
                        .show();
                RxBus.get().post("recall", true);
            }else{
                RxBus.get().post("recall", false);
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        initName();
                        LogUtil.d("TAG","currentIndex = " + currentIndex);
                        containerHelper.handlePageSelected(0);
                        LogUtil.d("CashReportFragment","show2");
                        showFragment(0,false);
                    }
                });

            }
        }

    }
    void  showFragment(int index,boolean oneRefresh){
        currentIndex = index;
        FragmentTransaction ft = fm.beginTransaction();
        if(index == 0){
            if(cashReportOneFragment == null){
                cashReportOneFragment = CashReportOneFragment.getInstance();
                ft.add(R.id.fl_cashreport_container, cashReportOneFragment);
            }else{
                ft.show(cashReportOneFragment);
                if(oneRefresh){
                    cashReportOneFragment.refresh();
                }
            }
            if(cashReportTwoFragment != null){
                ft.hide(cashReportTwoFragment);
            }
            if(cashReportThreeFragment != null){
                ft.hide(cashReportThreeFragment);
            }
            ft.commit();
        }else if(index == 1){
            if(cashReportTwoFragment == null){
                cashReportTwoFragment = CashReportTwoFragment.getInstance();
                ft.add(R.id.fl_cashreport_container, cashReportTwoFragment);
            }else{
                cashReportTwoFragment.refresh();
                ft.show(cashReportTwoFragment);
            }
            if(cashReportOneFragment != null){
                ft.hide(cashReportOneFragment);
            }
            if(cashReportThreeFragment != null){
                ft.hide(cashReportThreeFragment);
            }
            ft.commit();
        }else if(index ==2 ){
            if(cashReportThreeFragment == null){
                cashReportThreeFragment = new CashReportThreeFragment();
                ft.add(R.id.fl_cashreport_container,cashReportThreeFragment);
            }else{
                ft.show(cashReportThreeFragment);
            }
            if(cashReportOneFragment != null){
                ft.hide(cashReportOneFragment);
            }
            if(cashReportTwoFragment != null){
                ft.hide(cashReportTwoFragment);
            }
            ft.commit();

        }
    }
    public void clearCache(){
        LogUtil.d("TAG","Clear cache");
        currentMonthPosition = -1;
        isExistBean = null;
        XList.clear();
        YList.clear();
        dateStr = null;

    }
}
