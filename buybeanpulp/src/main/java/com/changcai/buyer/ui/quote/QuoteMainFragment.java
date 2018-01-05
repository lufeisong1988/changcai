package com.changcai.buyer.ui.quote;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.FiltrateDetailModel;
import com.changcai.buyer.bean.NewPriceInfo;
import com.changcai.buyer.bean.PriceInfo;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.common.Urls;
import com.changcai.buyer.http.HttpListener;
import com.changcai.buyer.http.VolleyUtil;
import com.changcai.buyer.interface_api.ApiServiceGenerator;
import com.changcai.buyer.interface_api.BaseApiModel;
import com.changcai.buyer.interface_api.GetMoreFiltrateService;
import com.changcai.buyer.interface_api.ThrowableFiltrateFunc;
import com.changcai.buyer.listener.CustomParentWithChildListener;
import com.changcai.buyer.rx.RxBus;
import com.changcai.buyer.ui.base.BaseFragment;
import com.changcai.buyer.ui.quote.bean.Condition;
import com.changcai.buyer.ui.quote.bean.ConditionValue;
import com.changcai.buyer.ui.quote.bean.LocationValue;
import com.changcai.buyer.ui.quote.bean.RegionValue;
import com.changcai.buyer.util.JsonFormat;
import com.changcai.buyer.util.RxUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.ServerErrorCodeDispatch;
import com.changcai.buyer.view.NavigationTabStrip;
import com.changcai.buyer.view.RotateDotsProgressView;
import com.changcai.buyer.view.XListView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class QuoteMainFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.navigation_indicator)
    NavigationTabStrip navigationIndicator;
    Unbinder unbinder;
    @BindView(R.id.dots_progress)
    RotateDotsProgressView dotsProgress;
    private Activity activity;

    private XListView mListView;
    // 更多筛选条件
    private ListView mProteinPriceListView;

    private LinearLayout llSelectPlace;
    private ListView mPlaceMainListView;
    private ListView mPlaceMoreListView;


    private LinearLayout llEmptyView;

    private LinearLayout llDeliveryPlace;
    private ImageView ivDeliveryPlaceArrow;       //交货地点
    private TextView tvDeliveryPlaceText;


    private LinearLayout llProteinPrice;
    private ImageView ivProteinPriceArrow;       //蛋白价格
    private TextView tvProteinPriceText;


    private List<Condition> conditions;
    private List<RegionValue> regions;
    private List<String> conditionSelect;

    private PriceListAdapter priceAdapter;
    private List<PriceInfo> priceInfos = null;

    private MoreFiltrateAdapter moreFiltrateAdapter;  //蛋白价格 - 变更-->更多筛选

    //    private List<ConditionValue> selectProteinPriceData = new ArrayList<>();
    private List<ConditionValue> selectPriceTypeData = new ArrayList<>();
    private List<ConditionValue> selectTimeData = new ArrayList<>();

    private PlaceClassifyMainAdapter placeClassifyMainAdapter;
    private PlaceClassifyMoreAdapter placeClassifyMoreAdapter;
    private List<RegionValue> mainPlaceList = new ArrayList<>();
    private List<LocationValue> morePlaceList = new ArrayList<>();

    private long lastClickViewID;


    private int currentPage = 0;

    private HashMap<String, String> selectPriceParams = new HashMap<>();


    private String regionId;
    private String locationId;
    private String productType;

    private int mainPlacePosition = 0;
    private int morePlacePosition = 0;
    private int productTypePosition = 0;

    private String lastRegionId = "";

    private int lastPlaceMorePosition = 0;

    private LinearLayout llNoQuoteHeader;

    private LinearLayout llMoreFiltrate;

    private TextView tvEmptyAction;
    private Subscription moreFiltrateSubscription;
    private int CALL_PHONE = 991;
    private TextView tvMoreFiltrateCancel, tvMoreFiltrateConfirm;
    private HashMap<String, String> moreFiltrateMap = new HashMap<>();

    private Observable<Boolean> backgroundToForegroundEvent;//从后台回到前台


    private ImageView ivEmptyImageView;
    private final static String[] pricePatternType = new String[]{Constants.SPOT, Constants.BASIS};

    private int nowIndex;

    @Override
    public void onAttach(Activity activity) {
        this.activity = activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reloadLastSelectPriceMethod();
        backgroundToForegroundEvent = RxBus.get().register("backgroundToForeground", Boolean.class);
        backgroundToForegroundEvent.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                currentPage = 0;
                getProductList(currentPage, false);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.price_fragment_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    protected void initSystemStatusBar() {
        super.initSystemStatusBar();
        immersionBar.statusBarDarkFont(true,0.2f).statusBarColor(R.color.white).fitsSystemWindows(true).init();
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
        initView(view);
        init();
        initPriceAdapter();
        initSelectProteinPriceAdapter();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        getProductFilter();
        getMoreProductFilter();
        getMetaRegion();
        getProductList(currentPage,false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Iterator<Map.Entry<String, String>> iter = selectPriceParams.entrySet().iterator();
        JsonObject jsonObject = new JsonObject();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            jsonObject.addProperty(key, value);
        }
        SPUtil.saveString("conditions", jsonObject.toString());
        RxUtil.remove(moreFiltrateSubscription);
        RxBus.get().unregister("backgroundToForeground", backgroundToForegroundEvent);
    }


    private void initView(View view) {
        llMoreFiltrate = (LinearLayout) view.findViewById(R.id.ll_more_filtrate);
        llMoreFiltrate.setOnClickListener(this);
        llNoQuoteHeader = (LinearLayout) view.findViewById(R.id.layout_no_quote_data);
        llEmptyView = (LinearLayout) view.findViewById(R.id.ll_empty_view);
        tvMoreFiltrateCancel = (TextView) view.findViewById(R.id.tv_more_filtrate_cancel);
        tvMoreFiltrateConfirm = (TextView) view.findViewById(R.id.tv_more_filtrate_confirm);
        tvEmptyAction = (TextView) view.findViewById(R.id.tv_empty_action);
        tvEmptyAction.setOnClickListener(this);
        ivEmptyImageView = (ImageView) view.findViewById(R.id.emptyView);
        tvMoreFiltrateCancel.setOnClickListener(this);
        tvMoreFiltrateConfirm.setOnClickListener(this);
        mListView = (XListView) view.findViewById(R.id.mListView);
        mListView.setDivider(null);
        mListView.setRefreshTime(null);
        mListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onPullRefresh() {
                currentPage = 0;
                // revertSelectPriceMethod();
                getProductList(currentPage, false);
            }

            @Override
            public void onPullLoadMore() {
                getProductList(++currentPage, true);
            }
        });
        llDeliveryPlace = (LinearLayout) view.findViewById(R.id.deliveryPlace);
        llDeliveryPlace.setOnClickListener(this);
        ivDeliveryPlaceArrow = (ImageView) view.findViewById(R.id.deliveryPlaceArrow);
        tvDeliveryPlaceText = (TextView) view.findViewById(R.id.deliveryPlaceText);


        llProteinPrice = (LinearLayout) view.findViewById(R.id.proteinPrice);
        llProteinPrice.setOnClickListener(this);
        ivProteinPriceArrow = (ImageView) view.findViewById(R.id.proteinPriceArrow);
        tvProteinPriceText = (TextView) view.findViewById(R.id.proteinPriceText);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (priceInfos != null && position <= priceInfos.size()) {
                    int index = position - 1;
                    if (index >= 0) {
                        PriceInfo priceInfo = priceInfos.get(index);
//                        if ("invalid".equalsIgnoreCase(priceInfo.getPriceStatus())) {
//                            ConfirmDialog.createConfirmDialog(getActivity(), "该报价超过有效时间／库存不足\n已失效，请查看其他报价", "查看其他报价");
//                        } else {
//                            openActivity(FullPayActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("detailId", priceInfo.getId());
                        openActivity(QuoteDetailActivity.class, bundle);
//                        }
                    }
                }
            }
        });

        mListView.setPullLoadEnable(false);

        llSelectPlace = (LinearLayout) view.findViewById(R.id.place_layout);
        llSelectPlace.setOnClickListener(this);
        mPlaceMainListView = (ListView) view.findViewById(R.id.place_main_list);
        mPlaceMoreListView = (ListView) view.findViewById(R.id.place_more_list);

        mPlaceMainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                placeClassifyMainAdapter.setSelectItem(position);
                RegionValue regionValue = regions.get(position);
                lastRegionId = regionValue.getId();
                currentPage = 0;
                morePlaceList.clear();
                morePlaceList.addAll(regionValue.getLstMetaLocationInfo());
                int selectionIndex = 0;
                int moreSelectionIndex = -1;
                if (regionValue.getId().equals(selectPriceParams.get("regionId"))) {
                    selectionIndex = lastPlaceMorePosition;
                    moreSelectionIndex = lastPlaceMorePosition;
                }
                placeClassifyMoreAdapter.setData(morePlaceList);
                setSelectMainPlaceAndVicePlace();
                mPlaceMoreListView.setSelection(selectionIndex);
                placeClassifyMoreAdapter.setSelectItem(moreSelectionIndex);
                if ("".equals(regionValue.getId()) || "全国".equals(regionValue.getName())) {
                    llSelectPlace.setVisibility(View.GONE);
                    revertView(true);
                    selectPriceParams.put("regionId", "");
                    selectPriceParams.put("locationId", "");
                    getProductList(currentPage, false);
                }

            }
        });

        mPlaceMoreListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                placeClassifyMoreAdapter.setSelectItem(position);
                LocationValue locationValue = morePlaceList.get(position);
                selectPriceParams.put("regionId", lastRegionId);
                selectPriceParams.put("locationId", locationValue.getId());
                lastPlaceMorePosition = position;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        llSelectPlace.setVisibility(View.GONE);
                        setSelectMainPlaceAndVicePlace();
                        currentPage = 0;
                        revertView(true);
                        getProductList(currentPage, false);
                    }
                }, 1000);

            }
        });


        mProteinPriceListView = (ListView) view.findViewById(R.id.proteinAndPriceLstView);


    }

    private void init() {
//        selectProteinPriceData = new ArrayList<>();
        priceInfos = new ArrayList<>();
        navigationIndicator.setTabIndex(nowIndex);
        navigationIndicator.setOnTabStripSelectedIndexListener(new NavigationTabStrip.OnTabStripSelectedIndexListener() {
            @Override
            public void onStartTabSelected(String title, int index) {

                Log.d("index", navigationIndicator.getTabIndex() + "");
                Log.d("indexStart", index + "");
            }

            @Override
            public void onEndTabSelected(String title, int index) {
                if (nowIndex != index) {
                    selectPriceParams.put("productType", pricePatternType[index]);
                    currentPage = 0;
                    getProductList(0, false);
                    nowIndex = index;
                }
            }
        });
    }


    private void initPriceAdapter() {
        if (priceAdapter == null) {
            priceAdapter = new PriceListAdapter(getActivity());
            mListView.setAdapter(priceAdapter);
        }
        priceAdapter.setData(priceInfos);
    }

    private void initSelectProteinPriceAdapter() {
        if (moreFiltrateAdapter == null) {
            moreFiltrateAdapter = new MoreFiltrateAdapter(getActivity());
            mProteinPriceListView.setAdapter(moreFiltrateAdapter);
        }
        moreFiltrateAdapter.setParentCustomListener(new CustomParentWithChildListener() {
            @Override
            public void onCustomerListener(View v, int parentPosition, int childPosition) {
                moreFiltrateMap.put(moreFiltrateAdapter.getGridType(parentPosition), moreFiltrateAdapter.getItemModel(parentPosition).getFilterList().get(childPosition).getId());
//                selectPriceParams.put(moreFiltrateAdapter.getGridType(parentPosition), moreFiltrateAdapter.getItemModel(parentPosition).getFilterList().get(childPosition).getId());
            }
        });

    }


    private void initPlaceClassifyMainAdapter() {
        if (placeClassifyMainAdapter == null) {
            placeClassifyMainAdapter = new PlaceClassifyMainAdapter(getActivity());
            mPlaceMainListView.setAdapter(placeClassifyMainAdapter);
            placeClassifyMainAdapter.setSelectItem(mainPlacePosition);
            mPlaceMainListView.setSelection(mainPlacePosition);
        }
        placeClassifyMainAdapter.setData(mainPlaceList);
    }

    private void initPlaceClassifyMoreAdapter() {
        if (placeClassifyMoreAdapter == null) {
            placeClassifyMoreAdapter = new PlaceClassifyMoreAdapter(getActivity());
            mPlaceMoreListView.setAdapter(placeClassifyMoreAdapter);
            placeClassifyMoreAdapter.setSelectItem(morePlacePosition);
            mPlaceMoreListView.setSelection(morePlacePosition);

        }
        placeClassifyMoreAdapter.setData(morePlaceList);
    }


    private void getMoreProductFilter() {
        HashMap<String, String> hashMap = new HashMap<>();
        GetMoreFiltrateService getMoreFiltrate = ApiServiceGenerator.createService(GetMoreFiltrateService.class);
        RxUtil.remove(moreFiltrateSubscription);
        moreFiltrateSubscription = getMoreFiltrate
                .moreFiltrate(hashMap)
                .subscribeOn(Schedulers.io())
                .onErrorResumeNext(new ThrowableFiltrateFunc<BaseApiModel<List<FiltrateDetailModel>>>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseApiModel<List<FiltrateDetailModel>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BaseApiModel<List<FiltrateDetailModel>> filtrateModelBaseApiModel) {
                        moreFiltrateAdapter.setFiltrateDetailModels(filtrateModelBaseApiModel.getResultObject());
                        moreFiltrateAdapter.notifyDataSetChanged();
                    }
                });
        RxUtil.remove(moreFiltrateSubscription);
    }

    /**
     * 获取报价筛选条件
     */
    private void getProductFilter() {
        HashMap<String, String> params = new HashMap<String, String>();
        VolleyUtil.getInstance().httpPost(activity, Urls.GET_PRODUCT_FILTER, params, new HttpListener() {
            @Override
            public void onResponseJson(JsonObject response) {
                super.onResponseJson(response);
                String errorCode = response.get(Constants.RESPONSE_CODE).getAsString();
                VolleyUtil.getInstance().cancelProgressDialog();
                if (errorCode.equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {
                    Gson gson = new Gson();
                    conditions = (List<Condition>) gson.fromJson(response.get(Constants.RESPONSE_CONTENT),
                            new TypeToken<List<Condition>>() {
                            }.getType());
                    conditionSelect = new ArrayList<String>();
                    for (int index = 0; index < conditions.size(); index++) {
                        Condition condition = conditions.get(index);
                        conditionSelect.add(condition.getFilterType());

                        if ("productType".equals(condition.getFilterType())) {
                            selectPriceTypeData.addAll(condition.getValue());
                            if (condition.getValue() != null) {
                                for (int i = 0; i < condition.getValue().size(); i++) {
                                    ConditionValue conditionValue = condition.getValue().get(i);
                                    if (i == 0) {
                                        if (Constants.BASIS.equalsIgnoreCase(conditionValue.getId())) {
//                                            isShowSelectPriceListView = true;
                                        } else {
//                                            isShowSelectPriceListView = false;
                                        }
                                        productTypePosition = i;
                                    }

                                    if (conditionValue.getId().equalsIgnoreCase(selectPriceParams.get("productType"))) {
                                        productTypePosition = i;
                                        if (Constants.BASIS.equals(conditionValue.getId())) {
//                                            isShowSelectPriceListView = true;
                                        } else {
//                                            isShowSelectPriceListView = false;
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        if ("basisTime".equals(condition.getFilterType())) {
                            selectTimeData.addAll(condition.getValue());
                            if (condition.getValue() != null) {
                                for (int i = 0; i < condition.getValue().size(); i++) {
                                    ConditionValue conditionValue = condition.getValue().get(i);
                                    if (conditionValue.getId().equalsIgnoreCase(selectPriceParams.get("basisTime"))) {
//                                        basisTimePosition = i;
                                        break;
                                    }
                                }
                            }
                        }
                    }



                } else {
                    ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.net_error));
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                VolleyUtil.getInstance().cancelProgressDialog();
                ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.network_unavailable));
            }
        }, false);
    }

    /**
     * 获取交货地点
     */
    private void getMetaRegion() {
        HashMap<String, String> params = new HashMap<String, String>();
        VolleyUtil.getInstance().httpPost(activity, Urls.GET_META_REGION, params, new HttpListener() {
            @Override
            public void onResponseJson(JsonObject response) {
                super.onResponseJson(response);
                String errorCode = response.get(Constants.RESPONSE_CODE).getAsString();
                VolleyUtil.getInstance().cancelProgressDialog();
                if (errorCode.equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {
                    Gson gson = new Gson();
                    mainPlaceList.clear();
                    regions = gson.fromJson(response.get(Constants.RESPONSE_CONTENT),
                            new TypeToken<List<RegionValue>>() {
                            }.getType());
                    mainPlaceList.addAll(regions);
                    if (regions.size() > 0) {
                        mainPlacePosition = 0;
                        morePlacePosition = 0;
                        if (regionId == null) {
                            morePlaceList.addAll(regions.get(0).getLstMetaLocationInfo());
                        } else {
                            for (int regionIndex = 0; regionIndex < regions.size(); regionIndex++) {
                                RegionValue regionValue = regions.get(regionIndex);
                                if (regionValue != null && regionValue.getId() != null
                                        && regionValue.getId().equalsIgnoreCase(regionId)) {
                                    mainPlacePosition = regionIndex;
                                    List<LocationValue> locationValues = regions.get(mainPlacePosition).getLstMetaLocationInfo();
                                    morePlaceList.addAll(locationValues);
                                    for (int locationIndex = 0; locationIndex < locationValues.size(); locationIndex++) {
                                        LocationValue locationValue = locationValues.get(locationIndex);
                                        if (locationValue != null && locationValue.getId().equalsIgnoreCase(locationId)) {
                                            morePlacePosition = locationIndex;
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
//                    if (conditions != null && regions != null) {
//                        currentPage = 0;
//                        getProductList(currentPage, false);
//                    }
                } else {
                    ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.net_error));
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.network_unavailable));
            }
        }, false);
    }

    /**
     * 获取报价列表
     * {
     * "regionId": "1",一级交货地点
     * "locationId": "22",二级交货地点
     * "deliveryStartTime ": , "2016-04-13 00:00:00"交货开始时间
     * "deliveryEndTime："2016-04-13 00:00:00"交货结束时间
     * "factoryId": ”73”,厂商品牌
     * "spec": ”PERCENT42”,蛋白规格
     * basisTime
     * productType
     * "currentPage": ”0”,从0开始，第几页
     * }
     */
    private void getProductList(final int page, final boolean loadMore) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("currentPage", "" + page);
        if (TextUtils.isEmpty(selectPriceParams.get("productType"))) {
            selectPriceParams.put("productType", Constants.SPOT);
        }
        params.putAll(selectPriceParams);
        if (page == 0 && !mListView.ismPullRefreshing()) {
            showProgress();
        }
        VolleyUtil.getInstance().httpPost(activity, Urls.GET_PRODUCT_LIST, params, new HttpListener() {
            @Override
            public void onResponseJson(JsonObject response) {
                super.onResponseJson(response);
                if (page == 0 && !mListView.ismPullRefreshing()) {
                    disMissProgress();
                }
                if (loadMore) {
                    mListView.stopLoadMore();
                } else {
                    mListView.stopRefresh("报价已刷新");
                }
                String errorCode = response.get(Constants.RESPONSE_CODE).getAsString();
                List<PriceInfo> tempList = null;

                if (errorCode.equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {
                    Gson gson = new Gson();

                    if (response.get(Constants.RESPONSE_CONTENT).isJsonObject()) {
                        NewPriceInfo newPriceInfo = gson.fromJson(response.get(Constants.RESPONSE_CONTENT),
                                new TypeToken<NewPriceInfo>() {
                                }.getType());
                        tempList = newPriceInfo.getProductList();
                        if (page == 0) {
                            if (newPriceInfo.getIsShowYellowTip() != null) {
                                if (newPriceInfo.getIsShowYellowTip().contentEquals("true")) {
                                    llNoQuoteHeader.setVisibility(View.VISIBLE);
                                } else {
                                    llNoQuoteHeader.setVisibility(View.GONE);
                                }
                            }
                        }
                        if (page == 0) {
                            priceInfos.clear();
                            currentPage = 0;
                        }
                        priceInfos.addAll(tempList);
                    }
                    priceAdapter.setData(priceInfos);
                    if (page == 0) {
                        if (tempList != null) {
                            if (tempList.size() < 10) {
                                if (tempList.size() == 0) {
                                    mListView.setPullRefreshEnable(true);
                                    emptyPriceList(0, "暂无商家报价");
                                    mListView.setPullLoadEnable(false);
                                } else {
                                    fullPriceList();
                                    mListView.setPullLoadEnable(false, R.string.listview_footer_hint_no_more_price, false);
                                }
                            } else {
                                fullPriceList();
                                mListView.setPullLoadEnable(true);
                            }

                        } else {
                            if (priceInfos.size() == 0) {
                                mListView.setPullRefreshEnable(true);
                            }
                        }
                    } else {

                        if (tempList != null && tempList.size() < 10)
                            mListView.setPullLoadEnable(false, R.string.listview_footer_hint_no_more_price, false);
                        else
                            mListView.setPullLoadEnable(true);
                    }
                } else {
                    if (page != 0 && tempList.size() <10) {
                        mListView.setPullLoadEnable(false, R.string.listview_footer_hint_no_more_price, true);
                    }
                    ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.net_error));
                }

            }

            @Override
            public void onError(String error) {
                super.onError(error);

                if (page == 0 && !mListView.ismPullRefreshing()) {
                    disMissProgress();
                }
                if (page == 0){
                    priceInfos.clear();
                    priceAdapter.notifyDataSetChanged();
                    mListView.setPullLoadEnable(false);
                }
                ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.network_unavailable));
                if (loadMore) {
                    mListView.stopLoadMore();
                    mListView.setPullLoadEnable(false, R.string.xlistview_footer_hint_no_more_fail, true);
                } else {
                    mListView.stopRefresh();
                }
            }
        }, false);
    }

    private void showProgress() {
        dotsProgress.setVisibility(View.VISIBLE);
        dotsProgress.showAnimation(true);
    }

    private void disMissProgress() {
        dotsProgress.setVisibility(View.GONE);
        dotsProgress.refreshDone(true);
    }

    @Override
    public void onClick(View v) {

        changeSelectTextStatus(v.getId());

        switch (v.getId()) {
            case R.id.deliveryPlace:    //交货地点
                if (llSelectPlace.getVisibility() == View.GONE) {
                    lastClickViewID = R.id.deliveryPlace;
                    initPlaceClassifyMainAdapter();
                    initPlaceClassifyMoreAdapter();
                    dismissSelectView(llSelectPlace);
                } else {
                    lastClickViewID = -1;
                    llSelectPlace.setVisibility(View.GONE);
                }
                break;
            case R.id.proteinPrice:    //蛋白价格
                if (llMoreFiltrate.getVisibility() == View.GONE) {
                    lastClickViewID = R.id.proteinPrice;
//                    initSelectProteinPriceAdapter(lastSelectPosition);
                    llMoreFiltrate.setVisibility(View.VISIBLE);
                    llMoreFiltrate.requestFocus();
                    dismissSelectView(mProteinPriceListView);
                } else {
                    lastClickViewID = -1;
                    llMoreFiltrate.setVisibility(View.GONE);
                }

                break;
            case R.id.tv_empty_action:
                showChooseDialog_two(SPUtil.getString(Constants.KEY_CONTACT_PHONE));
                break;
            case R.id.tv_more_filtrate_cancel:
                for (int i = 0; i < moreFiltrateAdapter.getCount(); i++) {
                    moreFiltrateAdapter.setResetBooleanArray(true);
                    moreFiltrateAdapter.notifyDataSetChanged();
                }
                for (String s : moreFiltrateMap.keySet()) {
                    if (selectPriceParams.containsKey(s)) {
                        selectPriceParams.remove(s);
                    }
                }
                moreFiltrateMap.clear();
                break;
            case R.id.tv_more_filtrate_confirm:
                selectPriceParams.putAll(moreFiltrateMap);
                currentPage = 0;
                llMoreFiltrate.setVisibility(View.GONE);
                getProductList(0, false);
                break;

            case R.id.ll_more_filtrate:
                if (llMoreFiltrate.isShown()) {
                    lastClickViewID = -1;
                    llMoreFiltrate.setVisibility(View.GONE);
                }
                break;

            case R.id.place_layout:
                if (llSelectPlace.isShown()) {
                    lastClickViewID = -1;
                    llSelectPlace.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 显示拨打电话的dialog
     */
    private void showChooseDialog_two(final String phone) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.layout_quoto_phone, null);
        final Dialog dialog = new Dialog(getActivity(), R.style.whiteFrameWindowStyle);
        dialog.setContentView(view);

        TextView tvPhone = (TextView) view.findViewById(R.id.phone);
        tvPhone.setText(phone);
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE);
                    return;
                }
                try {
                    callPhone(SPUtil.getString(Constants.KEY_CONTACT_PHONE));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });

        TextView tvCancel = (TextView) view.findViewById(R.id.cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.choosed_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        wl.gravity = Gravity.BOTTOM;
        dialog.onWindowAttributesChanged(wl);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void changeSelectTextStatus(int id) {
        revertView(false);
        switch (id) {
            case R.id.deliveryPlace:
                if (lastClickViewID == R.id.deliveryPlace) {
                    tvDeliveryPlaceText.setEnabled(true);
                    ivDeliveryPlaceArrow.setImageResource(R.drawable.icon_inline_down);
                } else {
                    tvDeliveryPlaceText.setEnabled(false);
                    ivDeliveryPlaceArrow.setImageResource(R.drawable.icon_inline_up);
                    lastClickViewID = -1;
                }
                break;
            case R.id.deliveryTime:
                break;
            case R.id.proteinPrice:
                if (lastClickViewID == R.id.proteinPrice) {
                    tvProteinPriceText.setEnabled(true);
                    ivProteinPriceArrow.setImageResource(R.drawable.icon_inline_down);
                } else {
                    setTextEnabled();
                    tvProteinPriceText.setEnabled(false);
                    ivProteinPriceArrow.setImageResource(R.drawable.icon_inline_up);
                }

                break;
        }
    }

    private void revertView(boolean isRevertLastState) {
        tvDeliveryPlaceText.setEnabled(true);
        ivDeliveryPlaceArrow.setImageResource(R.drawable.icon_inline_down);
        tvProteinPriceText.setEnabled(true);
        ivProteinPriceArrow.setImageResource(R.drawable.icon_inline_down);
        if (isRevertLastState) {
            lastClickViewID = -1;
        }
    }

    private void dismissSelectView(View currentView) {
        llMoreFiltrate.setVisibility(View.GONE);
        llSelectPlace.setVisibility(View.GONE);

        if (currentView == mProteinPriceListView) {
            llMoreFiltrate.setVisibility(View.VISIBLE);
            llMoreFiltrate.requestFocus();
        } else if (llSelectPlace == currentView) {
            llSelectPlace.setVisibility(View.VISIBLE);
            setSelectMainPlaceAndVicePlace();
        }
    }

    private void setSelectMainPlaceAndVicePlace() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mPlaceMoreListView.getLayoutParams();
        if (placeClassifyMainAdapter.getSelectItem() == 0) {
            if (layoutParams.weight != 0) {
                layoutParams.weight = 0;
                mPlaceMoreListView.setLayoutParams(layoutParams);
            }
        } else {
            if (layoutParams.weight != 3) {
                layoutParams.weight = 3;
                mPlaceMoreListView.setLayoutParams(layoutParams);
            }
        }


    }

    private void setTextEnabled() {
        llDeliveryPlace.setEnabled(true);
        llProteinPrice.setEnabled(true);
    }


    private void reloadLastSelectPriceMethod() {

        if (!TextUtils.isEmpty(SPUtil.getString("conditions"))) {
            JsonObject jsonObject = JsonFormat.String2Object(SPUtil.getString("conditions"));
            JsonFormat.populate(jsonObject, selectPriceParams);
            regionId = selectPriceParams.get("regionId");
            locationId = selectPriceParams.get("locationId");
            productType = selectPriceParams.get("productType");
        }

    }


    private void callPhone(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_PHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhone(SPUtil.getString(Constants.KEY_CONTACT_PHONE));
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void emptyPriceList(int type, String text) {
//        mListView.setVisibility(View.INVISIBLE);
        llEmptyView.setVisibility(View.VISIBLE);
//        llEmptyView.clearFocus();
        mListView.requestFocus();
//        mListView.setEmptyView(llEmptyView);
        tvEmptyAction.setText(text);
        if (type == 0) {
            ivEmptyImageView.setImageResource(R.drawable.default_img_none);
        } else if (type == 1) {
            ivEmptyImageView.setImageResource(R.drawable.default_img_404);
        }
    }

    private void fullPriceList() {
        llEmptyView.setVisibility(View.GONE);
    }
//
}