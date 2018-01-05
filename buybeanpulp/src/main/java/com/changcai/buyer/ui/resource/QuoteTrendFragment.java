package com.changcai.buyer.ui.resource;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.changcai.buyer.R;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.rx.RxBus;
import com.changcai.buyer.ui.base.BaseFragment;
import com.changcai.buyer.ui.login.LoginActivity;
import com.changcai.buyer.ui.quote.QuoteDetailActivity;
import com.changcai.buyer.ui.resource.adapter.QuoteTrendAdapter;
import com.changcai.buyer.ui.resource.adapter.TrendAreaAdapter;
import com.changcai.buyer.ui.resource.adapter.TrendPriceAdapter;
import com.changcai.buyer.ui.resource.bean.DomainsAndTypesBean;
import com.changcai.buyer.ui.resource.bean.QuoteBean;
import com.changcai.buyer.ui.resource.present.QuoteTrendPresent;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.ServerErrorCodeDispatch;
import com.changcai.buyer.view.ActionSheetDialog;
import com.changcai.buyer.view.CustomFontTextView;
import com.changcai.buyer.view.PinnedSectionPullToRefreshFooter;
import com.changcai.buyer.view.PinnedSectionPullToRefreshHeader;
import com.changcai.buyer.view.RotateDotsProgressView;
import com.jingchen.pulltorefresh.PinnedSectionListView;
import com.jingchen.pulltorefresh.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

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
 * 报价趋势
 */

public class QuoteTrendFragment extends BaseFragment implements View.OnClickListener, QuoteTrendViewModel, PullToRefreshLayout.OnPullListener, QuoteTrendAdapter.AdviceListener {
    @BindView(R.id.deliveryPlaceText)//价格text
            CustomFontTextView deliveryPlaceText;
    @BindView(R.id.deliveryPlaceArrow)
    ImageView deliveryPlaceArrow;
    @BindView(R.id.deliveryPlace)//价格ll
            LinearLayout deliveryPlace;
    @BindView(R.id.proteinPriceText)//区域text
            CustomFontTextView proteinPriceText;
    @BindView(R.id.proteinPriceArrow)
    ImageView proteinPriceArrow;
    @BindView(R.id.proteinPrice)//区域ll
            LinearLayout proteinPrice;
    @BindView(R.id.lv_quotertrend_price)
    ListView lvQuotertrendPrice;
    @BindView(R.id.lv_quotertrend_area)
    ListView lvQuotertrendArea;
    @BindView(R.id.rl_quotertrend_bg)
    RelativeLayout rlQuotertrendBg;
    @BindView(R.id.lv_quoterend)
    PinnedSectionListView lvQuoterend;
    @BindView(R.id.layout_quoterend)
    PullToRefreshLayout layoutQuoterend;
    @BindView(R.id.dots_progress)
    RotateDotsProgressView dotsProgress;
    @BindView(R.id.btn_resource_login)
    Button btnResourceLogin;
    @BindView(R.id.rl_resource_login_bg)
    RelativeLayout rlResourceLoginBg;
    @BindView(R.id.ll_empty_view)
    RelativeLayout llEmptyView;


    private Unbinder unbinder;
    private Activity activity;
    private Observable<Boolean> backgroundToForegroundEvent;//从后台回到前台
    private Observable<Boolean> logOrOutObservableEvent;

    private TrendPriceAdapter priceAdapter;
    private TrendAreaAdapter areaAdapter;
    private QuoteTrendAdapter quoteTrendAdapter;
    private List<DomainsAndTypesBean.DomainsBean> domains = new ArrayList<>();
    private List<DomainsAndTypesBean.ProductTypeBean> productType = new ArrayList<>();
    private List<QuoteBean.AllQuoteBean.ResultBean> items = new ArrayList<>();

    private QuoteTrendPresent quoteTrendPresent;

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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_quotetrend, container, false);
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
        logOrOutObservableEvent = RxBus.get().register("inOrOutAction", Boolean.class);
        logOrOutObservableEvent.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (llEmptyView.getVisibility() == View.VISIBLE) {
                            llEmptyView.setVisibility(View.GONE);
                        }
                        quoteTrendPresent.init();
                    }
                });
        init();
        initListener();
    }

    public void initData() {
        if (llEmptyView.getVisibility() == View.VISIBLE) {
            llEmptyView.setVisibility(View.GONE);
        }
        quoteTrendPresent.initQuoteData();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        quoteTrendPresent.onDestory();
        RxBus.get().unregister("inOrOutAction", logOrOutObservableEvent);
        RxBus.get().unregister("backgroundToForeground", backgroundToForegroundEvent);
    }

    private void init() {
        dotsProgress.setBackgroundColor(getResources().getColor(R.color.transparent));
        deliveryPlaceText.setText("定价方式");
        proteinPriceText.setText("地区");
        quoteTrendPresent = new QuoteTrendPresent(this);
        lvQuoterend.setSmoothScrollbarEnabled(false);

        priceAdapter = new TrendPriceAdapter(activity, productType);
        areaAdapter = new TrendAreaAdapter(activity, domains);
        quoteTrendAdapter = new QuoteTrendAdapter(activity, items, this);
        lvQuotertrendPrice.setAdapter(priceAdapter);
        lvQuotertrendArea.setAdapter(areaAdapter);
        lvQuoterend.setAdapter(quoteTrendAdapter);

        View hearderView = new PinnedSectionPullToRefreshHeader(activity);
        View footerView = new PinnedSectionPullToRefreshFooter(activity);
        layoutQuoterend.setCustomRefreshView(hearderView);
        layoutQuoterend.setOnRefreshProcessListener((PullToRefreshLayout.OnPullProcessListener) hearderView);
        layoutQuoterend.setCustomLoadmoreView(footerView);
        layoutQuoterend.setOnLoadmoreProcessListener((PullToRefreshLayout.OnPullProcessListener) footerView);
        layoutQuoterend.setOnPullListener(this);
        if (llEmptyView.getVisibility() == View.VISIBLE) {
            llEmptyView.setVisibility(View.GONE);
        }
        quoteTrendPresent.init();


    }

    private void initListener() {
        deliveryPlace.setOnClickListener(this);
        proteinPrice.setOnClickListener(this);
        rlQuotertrendBg.setOnClickListener(this);
        btnResourceLogin.setOnClickListener(this);
        lvQuotertrendPrice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                priceAdapter.setSelectItem(position);
                if (llEmptyView.getVisibility() == View.VISIBLE) {
                    llEmptyView.setVisibility(View.GONE);
                }
                quoteTrendPresent.initQuoteData(position, -1);

            }
        });
        lvQuotertrendArea.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                areaAdapter.setSelectItem(position);
                if (llEmptyView.getVisibility() == View.VISIBLE) {
                    llEmptyView.setVisibility(View.GONE);
                }
                quoteTrendPresent.initQuoteData(-1, position);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.deliveryPlace://价格
                quoteTrendPresent.clickPriceList(lvQuotertrendPrice);
                break;
            case R.id.proteinPrice://区域
                quoteTrendPresent.clickAreaList(lvQuotertrendArea);
                break;
            case R.id.rl_quotertrend_bg:
                quoteTrendPresent.clickListBg();
                break;
            case R.id.btn_resource_login:
                Intent intent2 = new Intent(activity, LoginActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }

    @Override
    public void showLoginView() {
        rlResourceLoginBg.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissLoginView() {
        rlResourceLoginBg.setVisibility(View.GONE);
    }

    @Override
    public void showRlQuotertrendBg() {
        rlQuotertrendBg.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissRlQuotertrendBg() {
        rlQuotertrendBg.setVisibility(View.GONE);
    }

    @Override
    public void showLvQuotertrendPrice() {
        lvQuotertrendPrice.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissLvQuotertrendPrice() {
        lvQuotertrendPrice.setVisibility(View.GONE);
    }

    @Override
    public void showLvQuotertrendArea() {
        lvQuotertrendArea.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissLvQuotertrendArea() {
        lvQuotertrendArea.setVisibility(View.GONE);
    }

    @Override
    public void quoterendPriceArrowUp() {
        deliveryPlaceArrow.setImageResource(R.drawable.icon_inline_up);
    }

    @Override
    public void quoterendPriceArrowDown() {
        deliveryPlaceArrow.setImageResource(R.drawable.icon_inline_down);
    }

    @Override
    public void quoterendAraeArrowUp() {
        proteinPriceArrow.setImageResource(R.drawable.icon_inline_up);
    }

    @Override
    public void quoterendAreaArrowDown() {
        proteinPriceArrow.setImageResource(R.drawable.icon_inline_down);
    }

    @Override
    public void showLoading() {
        dotsProgress.setVisibility(View.VISIBLE);
        dotsProgress.showAnimation(true);
    }

    @Override
    public void dismissLoaidng() {
        dotsProgress.setVisibility(View.GONE);
        dotsProgress.refreshDone(true);
    }

    @Override
    public void updatePriceData(List<DomainsAndTypesBean.ProductTypeBean> productType) {
        priceAdapter.setData(productType);
    }

    @Override
    public void updateAreaData(List<DomainsAndTypesBean.DomainsBean> domains) {
        areaAdapter.setData(domains);
    }

    @Override
    public void showNetErrorDialog() {
        ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.network_unavailable));
    }

    @Override
    public void refreshQuoteData(List<QuoteBean.AllQuoteBean.ResultBean> resultBeens) {
        items.clear();
        items.addAll(resultBeens);
        if (resultBeens.size() == 2 && llEmptyView.getVisibility() == View.GONE) {
            llEmptyView.setVisibility(View.VISIBLE);
        }
        quoteTrendAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadMoreQuoteData(List<QuoteBean.AllQuoteBean.ResultBean> resultBeens) {
        items.addAll(resultBeens);
        quoteTrendAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshFinish() {
        layoutQuoterend.refreshFinish(PullToRefreshLayout.SUCCEED);
    }

    @Override
    public void loadMoreFinish() {
        layoutQuoterend.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }

    @Override
    public void refreshEnable(boolean refreshEnable) {
        layoutQuoterend.setPullDownEnable(refreshEnable);
    }

    @Override
    public void loadMoreEnable(boolean loadMoreEnable) {
        layoutQuoterend.setPullUpEnable(loadMoreEnable);
    }

    /**
     * 加载webview
     *
     * @param parmarsStr
     */

    @Override
    public void loadWebview(String parmarsStr) {

    }

    @Override
    public void updateDomainName(String name) {
        proteinPriceText.setText(name);
    }

    @Override
    public void updatePriceName(String name) {
        deliveryPlaceText.setText(name);
    }


    /**
     * 刷新数据
     *
     * @param pullToRefreshLayout
     */
    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        if (llEmptyView.getVisibility() == View.VISIBLE) {
            llEmptyView.setVisibility(View.GONE);
        }
        quoteTrendPresent.refreshQuoteData();
    }

    /**
     * 加载更多数据
     *
     * @param pullToRefreshLayout
     */
    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        quoteTrendPresent.loadMoreQuoteData();
    }

    @Override
    public void showAdviceDialog(final int position) {
        if (isAdded() && isVisible() && position >= 0 && position < items.size()) {
            final QuoteBean.AllQuoteBean.ResultBean item = items.get(position);
            boolean orderAble = true;
            ActionSheetDialog.SheetItemColor orderColor = ActionSheetDialog.SheetItemColor.Blue;
            if (item.getSource() == 0) {
                orderAble = false;
                orderColor = ActionSheetDialog.SheetItemColor.GRAY;
            }
            final boolean finalOrderAble = orderAble;
            new ActionSheetDialog(getActivity())
                    .builder()
                    .setTitle("请直接下单或拨打联系电话\n" + SPUtil.getString(Constants.KEY_CONTACT_PHONE))
                    .setCancelable(true)
                    .setCanceledOnTouchOutside(false)
                    .addSheetItem("下单", orderColor,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    if (finalOrderAble) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("detailId", String.valueOf(item.getSourceId()));
                                        openActivity(QuoteDetailActivity.class, bundle);
                                    }

                                }
                            })
                    .addSheetItem("拨打电话", ActionSheetDialog.SheetItemColor.Blue,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    PermissionGen.needPermission(QuoteTrendFragment.this, 100,
                                            new String[]{
                                                    Manifest.permission.CALL_PHONE,
                                            }
                                    );
                                }
                            })
                    .show();
        }

    }

    @PermissionSuccess(requestCode = 100)
    public void doSomethingSucceed() {
        showChooseDialog(SPUtil.getString(Constants.KEY_CONTACT_PHONE));

    }

    @PermissionFail(requestCode = 100)
    public void doSomethingFail() {
        Toast.makeText(getActivity(), R.string.perssion_for_call, Toast.LENGTH_LONG).show();
    }
}
