package com.changcai.buyer.ui.news;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.changcai.buyer.CommonApplication;
import com.changcai.buyer.R;
import com.changcai.buyer.bean.BackReseverInfo;
import com.changcai.buyer.bean.NewsReader;
import com.changcai.buyer.bean.TradeTime;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.common.Urls;
import com.changcai.buyer.http.HttpListener;
import com.changcai.buyer.http.VolleyUtil;
import com.changcai.buyer.interface_api.ApiCodeErrorException;
import com.changcai.buyer.interface_api.ApiServiceGenerator;
import com.changcai.buyer.interface_api.NetworkResultFunc1;
import com.changcai.buyer.interface_api.TradeTimeService;
import com.changcai.buyer.listener.CustomListener;
import com.changcai.buyer.listener.PicassoPauseOnScrollListener;
import com.changcai.buyer.rx.RxBus;
import com.changcai.buyer.ui.base.BaseFragment;
import com.changcai.buyer.ui.login.LoginActivity;
import com.changcai.buyer.ui.main.MainActivity;
import com.changcai.buyer.ui.news.adapter.AdvanceNewsAdapter;
import com.changcai.buyer.ui.news.bean.NewsClassify;
import com.changcai.buyer.ui.news.bean.NewsEntity;
import com.changcai.buyer.ui.news.bean.RecommendNewsEntity;
import com.changcai.buyer.util.AndroidUtil;
import com.changcai.buyer.util.DateUtil;
import com.changcai.buyer.util.JsonFormat;
import com.changcai.buyer.util.ResUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.ServerErrorCodeDispatch;
import com.changcai.buyer.util.UserDataUtil;
import com.changcai.buyer.view.HomeAdsHolderView;
import com.changcai.buyer.view.RotateDotsProgressView;
import com.changcai.buyer.view.XListView;
import com.changcai.buyer.view.banner.CBViewHolderCreator;
import com.changcai.buyer.view.banner.ConvenientBanner;
import com.changcai.buyer.view.indicator.FragmentContainerHelper;
import com.changcai.buyer.view.indicator.MagicIndicator;
import com.changcai.buyer.view.indicator.commonnavigator.CommonNavigator;
import com.changcai.buyer.view.indicator.commonnavigator.abs.CommonNavigatorAdapter;
import com.changcai.buyer.view.indicator.commonnavigator.abs.IPagerIndicator;
import com.changcai.buyer.view.indicator.commonnavigator.abs.IPagerTitleView;
import com.changcai.buyer.view.indicator.commonnavigator.indicators.LinePagerIndicator;
import com.changcai.buyer.view.indicator.commonnavigator.titles.SimplePagerTitleView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by liuxingwei on 2016/11/4.
 */

public class AdvanceNewsMainFragment extends BaseFragment implements View.OnClickListener, CustomListener {

    private static final int SET_NEWS_LIST = 1;

    @Nullable
    @BindView(R.id.tv_time)
    TextView tvTime;

    @Nullable
    @BindView(R.id.tv_week)
    TextView tvWeek;


    @Nullable
    @BindView(R.id.tv_event)
    TextView tvEvent;

    @Nullable
    @BindView(R.id.llMiddleSection)
    LinearLayout llMiddleSection;

    @Nullable
    @BindView(R.id.llMiddle_tv_time)
    TextView llMiddleTvTime;

    @Nullable
    @BindView(R.id.llMiddle_tv_week)
    TextView llMiddleTvWeek;
    MagicIndicator magicIndicator;
    @BindView(R.id.tv_none_today)
    TextView tvNoneToday;
    ImageView ivEmptyView;
    TextView tvEmptyAction;
    RotateDotsProgressView rotateDotsProgressView;

    private XListView xListView;
    private View mCalendarHeaderView;

    //cms 资讯分类目
    private List<NewsClassify> newsClassify;
    //轮播图
    private ConvenientBanner bannerAds;
    //轮播图 data
    private List<RecommendNewsEntity> recommendNewsList;
    //message handler to refresh banner
    private uiHandler handler;
    //refresh cursor
    // 轮播图 指示器
    private Bitmap[] indicator = new Bitmap[2];
    //list 分页index
    private int currentPage;
    //资讯列表
    private List<NewsEntity> newsList;
    //资讯列表item id
    private String newsClassifyId = "0";
    //资讯列表适配器
    private AdvanceNewsAdapter adapter;
    private SimpleDateFormat mSDF = new SimpleDateFormat(
            "HH:mm");
    private Date mDate = new Date();


    private LinearLayout relativeLayoutReloadRootView;
    private RelativeLayout rlReleaseRootView;

    private boolean reserveRequestError;
    private boolean getInfoTabRequestError;
    private boolean getInfoRecommendError;
    private boolean getArtcileListError;
    private Hidden _mHidden;


    private Unbinder unbinder;

    //unix time from server
    private Date date;

    private Observable<Boolean> logoutEvent;
    private Observable<Boolean> backgroundToForegroundEvent;//从后台回到前台
    private CommonNavigator commonNavigator;
    private CommonNavigatorAdapter commonNavigatorAdapter;
    private FragmentContainerHelper mFragmentContainerHelper = new FragmentContainerHelper();

    private int[] screenLocation = new int[2];
    private NewsEntity news;
    private View tempView;
    private int tempPostion;

    private int nowIndex;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        _mHidden = (Hidden) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logoutEvent = RxBus.get().register("inOrOutAction", Boolean.class);
        logoutEvent.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (tempView != null && AdvanceNewsMainFragment.this.isVisible()) {
                            goDetails(tempView, tempPostion);
                        }
                        getArticleList(0, newsClassifyId);
                    }
                });

        backgroundToForegroundEvent = RxBus.get().register("backgroundToForeground", Boolean.class);
        backgroundToForegroundEvent.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                getArticleList(0, newsClassifyId);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mCalendarHeaderView = inflater.inflate(R.layout.activity_cms_list_calendar, null, false);
        unbinder = ButterKnife.bind(this, mCalendarHeaderView);
        bannerAds = (ConvenientBanner) inflater.inflate(R.layout.news_banner, container, false);
        View view = inflater.inflate(R.layout.news_advance_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rotateDotsProgressView = (RotateDotsProgressView) view.findViewById(R.id.news_progress);
        ivEmptyView = (ImageView) view.findViewById(R.id.emptyView);
        tvEmptyAction = (TextView) view.findViewById(R.id.tv_empty_action);
        magicIndicator = (MagicIndicator) view.findViewById(R.id.magic_indicator);
        xListView = (XListView) view.findViewById(R.id.mListView);
        relativeLayoutReloadRootView = (LinearLayout) view.findViewById(R.id.rl_reload_root_view);
        rlReleaseRootView = (RelativeLayout) view.findViewById(R.id.rl_release_root_view);
        rlReleaseRootView.setOnClickListener(this);
        rlReleaseRootView.setVisibility(View.GONE);
        initDataResource();

        tvEvent.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        tvWeek.setOnClickListener(this);

//        getReserver();
        getInfoRecommend("");
    }

    @Override
    protected void initSystemStatusBar() {
        super.initSystemStatusBar();
        immersionBar.statusBarDarkFont(true,0.2f).statusBarColor(R.color.white).fitsSystemWindows(true).init();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister("inOrOutAction", logoutEvent);
        RxBus.get().unregister("backgroundToForeground", backgroundToForegroundEvent);
        unbinder.unbind();
    }


    public interface Hidden {
        void hiddenTab();
    }


    /**
     * 数据等初始化
     */
    private void initDataResource() {
        newsClassify = new ArrayList<>();
        recommendNewsList = new ArrayList<>();
        newsList = new ArrayList<>();
        handler = new uiHandler(this);
        indicator[0] = ResUtil.drawableToBitamp(getActivity(), R.drawable.page_indicator_for_home_normal);
        indicator[1] = ResUtil.drawableToBitamp(getActivity(), R.drawable.page_indicator_for_home_focused);

        commonNavigator = new CommonNavigator(getContext());
        commonNavigatorAdapter = new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return newsClassify.size();
            }

            @SuppressWarnings("deprecation")
            @Override
            public IPagerTitleView getTitleView(final Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(getContext());
                simplePagerTitleView.setText(newsClassify.get(index).getName());
                simplePagerTitleView.setPadding(getResources().getDimensionPixelSize(R.dimen.dim15));
                simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                simplePagerTitleView.setNormalColor(getResources().getColor(R.color.storm_gray));
                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.membership_color));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mFragmentContainerHelper.setInterpolator(new LinearInterpolator());
                        mFragmentContainerHelper.handlePageSelected(index);
                        nowIndex = index;
                        newsClassifyId = newsClassify.get(index).getFolderId();
                        Map<String, String> stringMap = new HashMap<String, String>();
                        stringMap.put("account", UserDataUtil.isLogin() ? UserDataUtil.userMobile() : "未登录用户");
                        stringMap.put("memberGrade", UserDataUtil.isLogin() ? UserDataUtil.userGradeName() : "未登录用户");
                        stringMap.put("tabName", newsClassify.get(index).getName());
                        stringMap.put("startClickedTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())).toString());
                        MobclickAgent.onEvent(getActivity(), "dailyList_tabFilter", stringMap);
                        if (nowIndex != 0) {
//                            xListView.smoothScrollToPosition(7);
                            currentPage = 0;
                            getArticleList(currentPage, newsClassifyId);
                        } else {
//                            xListView.smoothScrollToPosition(0);
                            getInfoRecommend("");
//                            getInfoRecommend();
                        }

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
                linePagerIndicator.setColors(getResources().getColor(R.color.membership_color));
                return linePagerIndicator;
            }
        };
        commonNavigator.setAdapter(commonNavigatorAdapter);
        magicIndicator.setNavigator(commonNavigator);
        mFragmentContainerHelper.attachMagicIndicator(magicIndicator);
        xListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        xListView.setPullLoadEnable(true);
        xListView.setPullRefreshEnable(true);
        xListView.setHeaderBgNewStyle();
        xListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onPullRefresh() {

                currentPage = 0;
//                getArticleList(currentPage, newsClassifyId);
                if (nowIndex == 0) {
                    getInfoRecommend("");
                } else {
                    getArticleList(currentPage, newsClassifyId);
                }
            }

            @Override
            public void onPullLoadMore() {
                getArticleList(++currentPage, newsClassifyId);
            }
        });

        xListView.setOnScrollListener(new PicassoPauseOnScrollListener(false, true) {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE:
                        Picasso.with(getActivity()).resumeTag(getActivity());
                        break;
                    case SCROLL_STATE_FLING:
                        if (pauseOnFling)
                            Picasso.with(getActivity()).pauseTag(getActivity());
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        if (pauseOnScroll)
                            Picasso.with(getActivity()).pauseTag(getActivity());
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }


    public void disMissReloadView() {
        relativeLayoutReloadRootView.setVisibility(View.GONE);
        ivEmptyView.setImageDrawable(null);
        tvEmptyAction.setText(null);
    }

    public void showReloadView(boolean isErrorStatus) {
        relativeLayoutReloadRootView.setVisibility(View.VISIBLE);
        if (isErrorStatus) {
            ivEmptyView.setImageResource(R.drawable.default_img_404);
            tvEmptyAction.setText("加载失败，请下拉刷新");
            if (xListView.getHeaderViewsCount() > 2) {
                xListView.removeHeaderView(bannerAds);
            }
        } else {
            ivEmptyView.setImageResource(R.drawable.default_img_none);
            tvEmptyAction.setText(R.string.no_cms_dta);
        }
    }

    private void getReserver() {

        HashMap<String, String> params = new HashMap<>();
        VolleyUtil.getInstance().httpPost(activity, Urls.BACK_SERVER, params, new HttpListener() {
            @Override
            public void onError(String error) {
                super.onError(error);
                reserveRequestError = true;
                showReloadView(true);
                if (adapter == null) {
                    adapter = new AdvanceNewsAdapter(getActivity(), newsList);
                    xListView.setAdapter(adapter);
                    xListView.setPullLoadEnable(false);
                }
                ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.network_unavailable));
            }

            @Override
            public void onResponseJson(JsonObject response) {
                super.onResponseJson(response);

                String errorCode = response.get(Constants.RESPONSE_CODE).getAsString();
                if (errorCode.equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {
                    reserveRequestError = false;
                    Gson gson = new Gson();
                    BackReseverInfo info = gson.fromJson(response.get(Constants.RESPONSE_CONTENT),
                            new TypeToken<BackReseverInfo>() {
                            }.getType());
                    if (info.getNeedUpdate().equalsIgnoreCase("true")) {

                    }
                    if (info.getReleaseStatus().equalsIgnoreCase("true")) {
                        showShortToast("服务器升级中");
                        rlReleaseRootView.setVisibility(View.VISIBLE);
                        _mHidden.hiddenTab();
                    } else {
                        if (xListView.getAdapter() != null) {
                            xListView.setAdapter(null);
                        }
                        getInfoRecommend("");
                        rlReleaseRootView.setVisibility(View.GONE);
                    }
                }

            }
        }, false);

    }

    /**
     * 获取目录
     */
    private void getInfoTab() {
        HashMap<String, String> params = new HashMap<String, String>();
        VolleyUtil.getInstance().httpPost(activity, Urls.GET_INFO_TAB, params, new HttpListener() {
            @Override
            public void onResponseJson(JsonObject response) {
                super.onResponseJson(response);
                String errorCode = response.get(Constants.RESPONSE_CODE).getAsString();
                newsClassify.clear();
                if (errorCode.equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {
                    Gson gson = new Gson();
                    List<NewsClassify> newsClassifyTemp = gson.fromJson(response.get(Constants.RESPONSE_CONTENT),
                            new TypeToken<List<NewsClassify>>() {
                            }.getType());
                    newsClassify.addAll(newsClassifyTemp);
                    commonNavigatorAdapter.notifyDataSetChanged();
                    reserveRequestError = false;
                    getTradeEventDay();
                } else {
                    reserveRequestError = true;
                    ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.net_error), errorCode);

                }

            }

            @Override
            public void onError(String error) {
                super.onError(error);
                getInfoTabRequestError = true;
                ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.network_unavailable));
            }
        }, false);
    }

    /**
     * 获取轮播图,保留字段id。
     *
     * @param id
     */
    private void getInfoRecommend(String id) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("tabId", id);
        VolleyUtil.getInstance().httpPost(activity, Urls.GET_INFO_RECOMMEND, params, new HttpListener() {
            @Override
            public void onResponseJson(JsonObject response) {
                super.onResponseJson(response);
                String errorCode = response.get(Constants.RESPONSE_CODE).getAsString();
                if (errorCode.equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {
                    getInfoRecommendError = false;
                    Gson gson = new Gson();
                    recommendNewsList = gson.fromJson(response.get(Constants.RESPONSE_CONTENT),
                            new TypeToken<List<RecommendNewsEntity>>() {
                            }.getType());
                    xListView.removeHeaderView(bannerAds);
                    xListView.removeHeaderView(mCalendarHeaderView);
                    /**
                     * 广告初始化
                     */
                    bannerAds = (ConvenientBanner) LayoutInflater.from(getActivity()).inflate(R.layout.news_banner, null);
//                    List<String> titles = new ArrayList<String>(recommendNewsList.size());
//                    for (int i = 0; i < recommendNewsList.size(); i++) {
//                        titles.add(recommendNewsList.get(i).getName());
//                    }
                    bannerAds.setPageIndicator(indicator, null);
//                            .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
                    xListView.addHeaderView(bannerAds);
                    xListView.addHeaderView(mCalendarHeaderView);
                    updateADLayout(recommendNewsList);
                    handler.obtainMessage(SET_NEWS_LIST).sendToTarget();

                    getInfoTab();
                } else {
                    getInfoRecommendError = true;
                    ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.net_work_exception), errorCode);
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.network_unavailable));
                getInfoRecommendError = true;
                xListView.stopRefresh();
            }
        }, false);
    }

    /**
     * 渲染广告U
     *
     * @param homeAds
     */
    public void updateADLayout(List<RecommendNewsEntity> homeAds) {
        if (homeAds == null || homeAds.size() == 0) {
            bannerAds.setVisibility(View.GONE);
            return;
        }

        bannerAds.setVisibility(View.VISIBLE);
        bannerAds.setPages(
                new CBViewHolderCreator<HomeAdsHolderView>() {
                    @Override
                    public HomeAdsHolderView createHolder() {
                        return new HomeAdsHolderView();
                    }
                }, homeAds);

        /**
         * 大于1个则自动翻页,轮播时间5000。
         */
        if (homeAds.size() > 1) {
            bannerAds.setManualPageable(true);
            bannerAds.setPointViewVisible(true);
            bannerAds.startTurning(5000);

        } else {
            bannerAds.setManualPageable(false);
            bannerAds.setPointViewVisible(false);
            bannerAds.stopTurning();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.rl_release_root_view:
//                if (reserveRequestError) {
//                    getReserver();
//                    return;
//                }
                if (getInfoTabRequestError) {
                    getInfoTab();
                }
                if (getInfoRecommendError) {
                    getInfoRecommend("");
                }

                if (getArtcileListError) {
                    getArticleList(0, newsClassifyId);
                }

                break;

            case R.id.tv_event:
            case R.id.tv_time:
            case R.id.tv_week:
                openActivity(CalendarActivity.class);
                break;
        }

    }


    @Override
    public void onCustomerListener(View v, int position) {
        news = newsList.get(position);
        tempView = v;
        tempPostion = position;
        if (!Boolean.valueOf(news.getHasAuthority())) {
            openActivity(LoginActivity.class);
            return;
        }
        goDetails(v, position);
    }

    private void goDetails(View v, int position) {
        if (!TextUtils.isEmpty(news.getArticleUrl())) {
            Vector<String> vector = CommonApplication.getInstance().articleId;
            vector.insertElementAt(news.getArticleId(), 0);
            Bundle b = new Bundle();
            b.putString("url", news.getArticleUrl());
            if (!TextUtils.isEmpty(news.getTag())) {
                b.putString("title", news.getTag());
            } else {
                b.putString("title", "资讯详情");
            }
            b.putString("info", news.getSummary());
            v.getLocationOnScreen(screenLocation);
            b.putFloat("percentY", (float) screenLocation[1] / AndroidUtil.getDisplayMetrics(getActivity()).heightPixels);
            b.putString("articleId", news.getArticleId());
            ((MainActivity) getActivity()).setExitSwichLayout();
            AndroidUtil.startBrowser(activity, b, false);
            NewsReader.getSingleton().addAlreadyNewsId(position, news.getArticleId());
            adapter.notifyDataSetChanged();
            news = null;
            tempView = null;

        }
    }

    /**
     * 弱引用，double check,对象是空，当前fragment 是否被加入
     */
    private static class uiHandler extends Handler {
        private WeakReference<AdvanceNewsMainFragment> activityWeakReference;

        public uiHandler(AdvanceNewsMainFragment fragment) {
            activityWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            AdvanceNewsMainFragment fragment = activityWeakReference.get();
            if (null != fragment) {

                switch (msg.what) {
                    case SET_NEWS_LIST:
                        if (fragment.isAdded()) {
                            if (fragment.adapter == null) {
                                fragment.adapter = new AdvanceNewsAdapter(fragment.getActivity(), fragment.newsList);
                            }
                            fragment.xListView.setAdapter(fragment.adapter);
                            fragment.adapter.setCustomListener(fragment);
                            fragment.getArticleList(fragment.currentPage, fragment.newsClassifyId);
                        }
                        break;
                    case 200:
                        Bundle bundle = msg.getData();
                        JsonObject jsonObject = JsonFormat.String2Object(bundle.get("apiResult").toString());
                        Gson gson = new Gson();
                        BackReseverInfo backReseverInfo = gson.fromJson(jsonObject.get(Constants.RESPONSE_CONTENT),
                                new TypeToken<BackReseverInfo>() {
                                }.getType());


                        if (backReseverInfo.getNeedUpdate().equalsIgnoreCase("true")) {

                        }

                        if (backReseverInfo.getReleaseStatus().equalsIgnoreCase("true")) {
                            fragment.showShortToast("服务器升级中");
                            fragment.rlReleaseRootView.setVisibility(View.VISIBLE);
                            fragment._mHidden.hiddenTab();
                        } else {
                            fragment.getInfoRecommend("");
                            fragment.rlReleaseRootView.setVisibility(View.GONE);
                        }
                        break;

                }
            }
        }
    }


    /**
     * page == 0  会clear原来的数据
     * 获取资讯板块详情文章列表
     */
    private void getArticleList(final int page, String id) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("folderId", id);
        params.put("currentPage", "" + page);
        params.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));

        if (page == 0 && !xListView.ismPullRefreshing()) {
            showProgress();
        }
        VolleyUtil.getInstance().httpPost(activity, Urls.GET_ARTICLE_LIST, params, new HttpListener() {
            @Override
            public void onResponseJson(JsonObject response) {
                super.onResponseJson(response);
                String errorCode = response.get(Constants.RESPONSE_CODE).getAsString();
                List<NewsEntity> tempList = null;
                disMissReloadView();
                if (errorCode.equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {
                    getArtcileListError = false;
                    Gson gson = new Gson();
                    if (page == 0) {
                        newsList.clear();
                        if (adapter != null) {
                            adapter.clearDataList();
                        }
                        currentPage = 0;
                    }
                    if (nowIndex != 0) {
                        xListView.removeHeaderView(bannerAds);
                    }
                    if (xListView.getHeaderViewsCount() == 1) {
                        xListView.addHeaderView(mCalendarHeaderView);
                    }
//                    else {
//                        currentPage++;
//                    }
                    if (response.get(Constants.RESPONSE_CONTENT).isJsonArray()) {
                        tempList = gson.fromJson(response.get(Constants.RESPONSE_CONTENT),
                                new TypeToken<List<NewsEntity>>() {
                                }.getType());
                        if (tempList != null && tempList.size() != 0) {
                            newsList.addAll(OrderAndGroupsData(tempList, page));
                        }
                    }
                    if (adapter != null)
                        adapter.setNewsList((ArrayList<NewsEntity>) newsList);

                    Log.d("xlistview", xListView.getHeaderViewsCount() + "count");
                    setFirstSectionAtrb();
                } else {
                    getArtcileListError = true;
                    ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.net_error), errorCode);
                }

                if (page == 0) {
                    disMissProgress();
                    mDate.setTime(System.currentTimeMillis());
                    String time = mSDF.format(mDate);
                    xListView.stopRefresh("最后更新 ".concat(time));
                    if (tempList == null) {
                        xListView.setPullLoadEnable(false);
                    } else {
                        if (tempList.size() == 0) {
                            xListView.removeHeaderView(mCalendarHeaderView);
                            xListView.removeHeaderView(bannerAds);
                            xListView.setPullLoadEnable(false);
                            showReloadView(false);
                        }
                        if (tempList.size() > 0 && tempList.size() < 10) {
                            xListView.setPullLoadEnable(false, R.string.listview_footer_hint_no_more_cms, false);
                        } else if (tempList.size() == 10) {
                            xListView.setPullLoadEnable(true);
                        }
                    }
                } else {
                    xListView.stopLoadMore();
                    if (tempList != null && tempList.size() < 10)
                        xListView.setPullLoadEnable(false, R.string.listview_footer_hint_no_more_cms, false);
                    else
                        xListView.setPullLoadEnable(true);
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.network_unavailable));
                getArtcileListError = true;

                disMissProgress();
                if (currentPage == 0) {
                    xListView.stopRefresh();
                    if (adapter != null) {
                        adapter.clearDataList();
                    }
                    xListView.removeHeaderView(bannerAds);
                    xListView.removeHeaderView(mCalendarHeaderView);
                    xListView.setPullLoadEnable(false);
                    showReloadView(true);
                } else {
                    xListView.stopLoadMore();
                    xListView.setPullLoadEnable(false, R.string.xlistview_footer_hint_no_more_fail, true);
                }
            }
        }, false);
    }


    private void showProgress() {
        rotateDotsProgressView.setVisibility(View.VISIBLE);
        rotateDotsProgressView.showAnimation(true);
    }

    private void disMissProgress() {
        rotateDotsProgressView.setVisibility(View.GONE);
        rotateDotsProgressView.refreshDone(true);
    }

    private void getTradeEventDay() {
        TradeTimeService tradeTimeService = ApiServiceGenerator.createService(TradeTimeService.class);
        Map<String, String> baseMap = new HashMap<>();
        tradeTimeService
                .getTradeTime(baseMap)
                .subscribeOn(Schedulers.io())
                .map(new NetworkResultFunc1<TradeTime>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<TradeTime>() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void call(TradeTime tradeTime) {
                        date = new Date(Long.parseLong(tradeTime.getCurrentTime()));
                        tvTime.setText(DateUtil.dateToString("yyyy年MM月dd日", date));
                        tvWeek.setText(DateUtil.getWeekOfDate(date));
                        setFirstSectionAtrb();
                        tvEvent.setVisibility(View.VISIBLE);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        date = new Date();
                        tvTime.setText(DateUtil.dateToString("yyyy年MM月dd日", date));
                        tvWeek.setText(DateUtil.getWeekOfDate(date));
                        if (throwable instanceof ApiCodeErrorException) {
                            ServerErrorCodeDispatch.getInstance().checkErrorCode(getContext(), ((ApiCodeErrorException) throwable).getState(), throwable.getMessage());
                        } else {
                            tvEvent.setVisibility(View.VISIBLE);
                        }
                        setFirstSectionAtrb();
                    }
                });
    }

    /**
     * 遍历 最新10条服务器过来的数据
     *
     * @param sectionNewsEntity
     * @param currentPageIndex
     * @return
     */
    private List<NewsEntity> OrderAndGroupsData(List<NewsEntity> sectionNewsEntity, int currentPageIndex) {
        List<NewsEntity> data = new ArrayList<>();
        String dateTime;
        String compareTime;
        for (int i = 0; i < sectionNewsEntity.size(); i++) {
            data.add(sectionNewsEntity.get(i));
            if (i < sectionNewsEntity.size() - 1) {
                dateTime = DateUtil.unixTime2Date(sectionNewsEntity.get(i).getCreateTime());
                compareTime = DateUtil.unixTime2Date(sectionNewsEntity.get(i + 1).getCreateTime());
                if (!dateTime.contentEquals(compareTime)) {
                    NewsEntity sectionNewsAdd = new NewsEntity();
                    sectionNewsAdd.setSection(compareTime);
//                    sectionNewsAdd.setCreateTime(sectionNewsEntity.get(i).getCreateTime());
                    sectionNewsAdd.setCreateTime(sectionNewsEntity.get(i + 1).getCreateTime());
                    if (!data.contains(sectionNewsAdd) && !newsList.contains(sectionNewsAdd)) {
                        data.add(sectionNewsAdd);
                    }
                }
            }
        }
        return data;
    }

    private void setFirstSectionAtrb() {
        if (newsList != null && newsList.size() > 0) {
            if (date == null) {
                return;
            }
            if (DateUtil.isSameDate(date, new Date(Long.parseLong(newsList.get(0).getCreateTime())))) {
                llMiddleSection.setVisibility(View.GONE);
                tvNoneToday.setVisibility(View.GONE);
            } else {
                llMiddleSection.setVisibility(View.VISIBLE);
                tvNoneToday.setVisibility(View.VISIBLE);
                llMiddleTvTime.setText(DateUtil.unixTime2Date(newsList.get(0).getCreateTime()));
                llMiddleTvWeek.setText(DateUtil.getWeekOfDate(new Date(Long.parseLong(newsList.get(0).getCreateTime()))));


            }
        }
    }


}
