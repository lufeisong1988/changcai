package com.changcai.buyer.ui.news;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changcai.buyer.CommonApplication;
import com.changcai.buyer.R;
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
import com.changcai.buyer.ui.base.BaseAbstraceFragment;
import com.changcai.buyer.ui.login.LoginActivity;
import com.changcai.buyer.ui.main.MainActivity;
import com.changcai.buyer.ui.news.adapter.AdvanceNewsAdapter;
import com.changcai.buyer.ui.news.bean.NewsClassify;
import com.changcai.buyer.ui.news.bean.NewsEntity;
import com.changcai.buyer.ui.news.bean.RecommendNewsEntity;
import com.changcai.buyer.util.AndroidUtil;
import com.changcai.buyer.util.DateUtil;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.util.ResUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.ServerErrorCodeDispatch;
import com.changcai.buyer.view.CustomFontTextView;
import com.changcai.buyer.view.HomeAdsHolderView;
import com.changcai.buyer.view.RotateDotsProgressView;
import com.changcai.buyer.view.XListView;
import com.changcai.buyer.view.banner.CBViewHolderCreator;
import com.changcai.buyer.view.banner.ConvenientBanner;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by lufeisong on 2017/10/12.
 */

public class NewFragment extends BaseAbstraceFragment implements View.OnClickListener, CustomListener {

    @BindView(R.id.mListView)
    XListView mListView;
    @BindView(R.id.emptyView)
    ImageView emptyView;
    @BindView(R.id.tv_empty_action)
    TextView tvEmptyAction;
    @BindView(R.id.rl_reload_root_view)
    LinearLayout rlReloadRootView;
    @BindView(R.id.news_progress)
    RotateDotsProgressView newsProgress;

    CustomFontTextView tvTime;
    CustomFontTextView tvWeek;
    CustomFontTextView tvEvent;
    TextView tvNoneToday;
    CustomFontTextView llMiddleTvTime;
    CustomFontTextView llMiddleTvWeek;
    LinearLayout llMiddleSection;
    private View mCalendarHeaderView;

    private boolean destoryViewAble = false;//viewpager滑动导致vie被destory释放
    private Observable<Boolean> logoutEvent;//登录状态
    private Observable<Boolean> backgroundToForegroundEvent;//从后台回到前台


    //当前选择的cms
    private int nowIndex = 0; //位置
    //分类详情
    private NewsClassify newsClassify;
    //轮播图
    private List<RecommendNewsEntity> recommendNewsList = new ArrayList<>();//data
    private Bitmap[] indicator = new Bitmap[2];//指示器
    private ConvenientBanner bannerAds;


    //资讯列表
    private List<NewsEntity> newsList = new ArrayList<>();//资讯集合
    private AdvanceNewsAdapter adapter;//适配器
    private int currentPage = 0;//当前分页
    //点击的item
    private NewsEntity news;//item的详情
    private View tempView;  //被点击的item的临时view
    private int tempPostion;//被点击的item的临时position
    private int[] screenLocation = new int[2];

    private SimpleDateFormat mSDF = new SimpleDateFormat(
            "HH:mm");
    private Date mDate = new Date();
    private Date date;


    /**缓存状态**/
    //资讯列表
    private final int INFO_RECOMMEND_SUCCEED = 0;
    private final int INFO_RECOMMEND_NET_ERROR = 1;
    private final int INFO_RECOMMEND_ERROR = 2;
    private int infoRecommednCode = INFO_RECOMMEND_SUCCEED;
    private final int ARTICLE_LIST_SUCCEED = 0;
    private final int ARTICLE_LIST_NET_ERROR = 1;
    private final int ARTICLE_LIST_ERROR = 2;
    private int articleListCode = ARTICLE_LIST_SUCCEED;
    private List<NewsEntity> tempList = null;
    //当天行为
    private boolean tradeEventDayDataError = false;

    public NewFragment() {
    }

    public NewFragment(NewsClassify newsClassify, int nowIndex) {
        this.newsClassify = newsClassify;
        this.nowIndex = nowIndex;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logoutEvent = RxBus.get().register("inOrOutAction", Boolean.class);
        logoutEvent.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        LogUtil.d("Update",nowIndex + " login");
                        if (tempView != null && NewFragment.this.isVisible()) {
                            goDetails(tempView, tempPostion);
                        }
                        getRefreshData();
                    }
                });

        backgroundToForegroundEvent = RxBus.get().register("backgroundToForeground", Boolean.class);
        backgroundToForegroundEvent.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                getRefreshData();
            }
        });
    }

    @Override
    public int setResId() {
        return R.layout.fragment_new;
    }

    @Override
    protected void initSystemStatusBar() {
        super.initSystemStatusBar();
        immersionBar.statusBarDarkFont(true, 0.2f).statusBarColor(R.color.white).fitsSystemWindows(true).init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister("inOrOutAction", logoutEvent);
        RxBus.get().unregister("backgroundToForeground", backgroundToForegroundEvent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        destoryViewAble = true;
    }


    @Override
    protected void initView() {
        mCalendarHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_cms_list_calendar, null, false);
        tvTime = (CustomFontTextView) mCalendarHeaderView.findViewById(R.id.tv_time);
        tvWeek = (CustomFontTextView) mCalendarHeaderView.findViewById(R.id.tv_week);
        tvEvent = (CustomFontTextView) mCalendarHeaderView.findViewById(R.id.tv_event);
        tvNoneToday = (TextView) mCalendarHeaderView.findViewById(R.id.tv_none_today);
        llMiddleTvTime = (CustomFontTextView) mCalendarHeaderView.findViewById(R.id.llMiddle_tv_time);
        llMiddleTvWeek = (CustomFontTextView) mCalendarHeaderView.findViewById(R.id.llMiddle_tv_week);
        llMiddleSection = (LinearLayout) mCalendarHeaderView.findViewById(R.id.llMiddleSection);
        bannerAds = (ConvenientBanner) LayoutInflater.from(getActivity()).inflate(R.layout.news_banner, null);
    }

    @Override
    protected void initConfig() {
        //添加dot背景图
        if(nowIndex == 0){
            indicator[0] = ResUtil.drawableToBitamp(getActivity(), R.drawable.page_indicator_for_home_normal);
            indicator[1] = ResUtil.drawableToBitamp(getActivity(), R.drawable.page_indicator_for_home_focused);
            bannerAds.setPageIndicator(indicator, null);
        }
        //添加适配器
        if (adapter == null) {
            adapter = new AdvanceNewsAdapter(getActivity(), newsList);
        }
        mListView.setAdapter(adapter);
        adapter.setCustomListener(this);
    }

    @Override
    protected void initData() {
//        if(!destoryViewAble){
            getRefreshData();
//        }else{
//            destoryViewAble = false;
//            getReloadData();
//        }
    }

    @Override
    protected void initListener() {
        tvEvent.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        tvWeek.setOnClickListener(this);
        rlReloadRootView.setOnClickListener(this);

        mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mListView.setPullLoadEnable(true);
        mListView.setPullRefreshEnable(true);
        mListView.setHeaderBgNewStyle();
        mListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onPullRefresh() {
                getRefreshData();
            }

            @Override
            public void onPullLoadMore() {
                getArticleList(++currentPage, newsClassify.getFolderId());
            }
        });

        mListView.setOnScrollListener(new PicassoPauseOnScrollListener(false, true) {
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
    //针对viewpager滑动，主动检测
    //检测是否存在数据，如果不存在，再次刷新数据
    public  void checkData(){
        if(rlReloadRootView.getVisibility() == View.VISIBLE && tvEmptyAction.getText().toString().equals(getActivity().getResources().getString(R.string.reload_text))){
            getRefreshData();
        }
    }
    //获取数据
    private void getRefreshData() {
        currentPage = 0;
        if (nowIndex == 0) {//首页需要加载 ad，加载cms分类目
            getInfoRecommend("");
        }else{//加载cms分类目
            getArticleList(currentPage, newsClassify.getFolderId());
        }
        getTradeEventDay();
    }
    //view回复后重新加载数据
    private void getReloadData(){
        if (nowIndex == 0) {//首页需要加载 ad，加载cms分类目
            updateInfoRecommendData();
        }
        updateArticleListData();
        updateTradeEventDayData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_reload_root_view://加载错误，重新加载数据
                getRefreshData();
                break;
            case R.id.tv_event:
            case R.id.tv_time:
            case R.id.tv_week:
                openActivity(CalendarActivity.class);
                break;
        }
    }

    //TODO 跳转详情页面

    /**
     * 跳转详情页面(1)
     * item点击回调
     *
     * @param v
     * @param position
     */
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

    /**
     * 跳转详情页面(2)
     * 跳转逻辑
     *
     * @param v
     * @param position
     */
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

    //TODO 获取轮播体

    /**
     * 获取轮播图(1)
     * 获取数据
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
                disMissReloadView();
                String errorCode = response.get(Constants.RESPONSE_CODE).getAsString();
                if (errorCode.equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {
                    infoRecommednCode = INFO_RECOMMEND_SUCCEED;
                    Gson gson = new Gson();
                    recommendNewsList = gson.fromJson(response.get(Constants.RESPONSE_CONTENT),
                            new TypeToken<List<RecommendNewsEntity>>() {
                            }.getType());
                    updateInfoRecommendData();
                    getArticleList(currentPage,newsClassify.getFolderId());
                } else {
                    infoRecommednCode = INFO_RECOMMEND_NET_ERROR;
                    if(isAdded()){
                        ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.net_work_exception), errorCode);
                    }
                    mListView.stopRefresh();
                    updateInfoRecommendData();
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                infoRecommednCode = INFO_RECOMMEND_ERROR;
                if(isAdded()){
                    ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.network_unavailable));
                }
                mListView.stopRefresh();
                updateInfoRecommendData();
            }
        }, false);
    }

    /**
     * 获取轮播图(2)
     * 渲染ui
     *
     */
    private void updateInfoRecommendData() {
        if(infoRecommednCode == INFO_RECOMMEND_SUCCEED){
            mListView.removeHeaderView(bannerAds);
            mListView.addHeaderView(bannerAds);
            mListView.removeHeaderView(mCalendarHeaderView);
            mListView.addHeaderView(mCalendarHeaderView);
            if (recommendNewsList == null || recommendNewsList.size() == 0) {
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
                    }, recommendNewsList);
            /**
             * 大于1个则自动翻页,轮播时间5000。
             */
            if (recommendNewsList.size() > 1) {
                bannerAds.setManualPageable(true);
                bannerAds.setPointViewVisible(true);
                bannerAds.startTurning(5000);

            } else {
                bannerAds.setManualPageable(false);
                bannerAds.setPointViewVisible(false);
                bannerAds.stopTurning();
            }
        }else if(infoRecommednCode == INFO_RECOMMEND_NET_ERROR || infoRecommednCode == INFO_RECOMMEND_ERROR){
            if (adapter != null) {
                adapter.clearDataList();
            }
            mListView.removeHeaderView(bannerAds);
            mListView.removeHeaderView(mCalendarHeaderView);
            mListView.setPullLoadEnable(false);
            showReloadView(true);
        }

    }

    //TODO 获取资讯列表

    /**
     * 获取数据
     *
     * @param page
     * @param id
     */
    private void getArticleList(final int page, String id) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("folderId", id);
        params.put("currentPage", "" + page);
        params.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));

        if (page == 0 && !mListView.ismPullRefreshing()) {
            showProgress();
        }
        VolleyUtil.getInstance().httpPost(activity, Urls.GET_ARTICLE_LIST, params, new HttpListener() {
            @Override
            public void onResponseJson(JsonObject response) {
                super.onResponseJson(response);
                String errorCode = response.get(Constants.RESPONSE_CODE).getAsString();
                disMissReloadView();
                if (errorCode.equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {
                    articleListCode = ARTICLE_LIST_SUCCEED;
                    Gson gson = new Gson();
                    if (page == 0) {
                        newsList.clear();
                        if (adapter != null) {
                            adapter.clearDataList();
                        }
                    }
                    if (response.get(Constants.RESPONSE_CONTENT).isJsonArray()) {
                        tempList = gson.fromJson(response.get(Constants.RESPONSE_CONTENT),
                                new TypeToken<List<NewsEntity>>() {
                                }.getType());
                        if (tempList != null && tempList.size() != 0) {
                            newsList.addAll(OrderAndGroupsData(tempList, page));
                        }
                    }

                } else {
                    articleListCode = ARTICLE_LIST_NET_ERROR;
                    if(isAdded()){
                        ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.net_error), errorCode);
                    }
                }
                if (page == 0) {
                    disMissProgress();
                    mDate.setTime(System.currentTimeMillis());
                    String time = mSDF.format(mDate);
                    mListView.stopRefresh("最后更新 ".concat(time));
                } else {
                    mListView.stopLoadMore();
                }
                updateArticleListData();
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                articleListCode = ARTICLE_LIST_ERROR;
                disMissProgress();
                if(isAdded()){
                    ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.network_unavailable));
                }
                if (currentPage == 0) {
                    mListView.stopRefresh();
                } else {
                    mListView.stopLoadMore();
                }
                updateArticleListData();
            }
        }, false);
    }
    private void updateArticleListData(){
        LogUtil.d("Update",nowIndex + " update");
      if(articleListCode == ARTICLE_LIST_SUCCEED || articleListCode == ARTICLE_LIST_NET_ERROR){
          if(articleListCode == ARTICLE_LIST_SUCCEED){
              if (mListView.getHeaderViewsCount() == 1) {
                  mListView.addHeaderView(mCalendarHeaderView);
              }
              if (adapter != null)
                  adapter.setNewsList((ArrayList<NewsEntity>) newsList);
              setFirstSectionAtrb();
          }else if(articleListCode == ARTICLE_LIST_NET_ERROR){
              showReloadView(true);
          }
          if (currentPage == 0) {
              if (tempList == null) {
                  mListView.setPullLoadEnable(false);
              } else {
                  if (tempList.size() == 0) {
                      mListView.removeHeaderView(mCalendarHeaderView);
                      mListView.removeHeaderView(bannerAds);
                      mListView.setPullLoadEnable(false);
                      showReloadView(false);
                  }
                  if (tempList.size() > 0 && tempList.size() < 10) {
                      mListView.setPullLoadEnable(false, R.string.listview_footer_hint_no_more_cms, false);
                  } else if (tempList.size() == 10) {
                      mListView.setPullLoadEnable(true);
                  }
              }
          } else {
              if (tempList != null && tempList.size() < 10)
                  mListView.setPullLoadEnable(false, R.string.listview_footer_hint_no_more_cms, false);
              else
                  mListView.setPullLoadEnable(true);
          }
      }else if(articleListCode == ARTICLE_LIST_ERROR){
          if (currentPage == 0) {
              if (adapter != null) {
                  adapter.clearDataList();
              }
              mListView.removeHeaderView(bannerAds);
              mListView.removeHeaderView(mCalendarHeaderView);
              mListView.setPullLoadEnable(false);
              showReloadView(true);
          } else {
              mListView.setPullLoadEnable(false, R.string.xlistview_footer_hint_no_more_fail, true);
          }
      }
    }

    //TODO 获取交易事件
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
                        tradeEventDayDataError = false;
                        updateTradeEventDayData();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        date = new Date();
                        tradeEventDayDataError = false;
                        if (throwable instanceof ApiCodeErrorException) {
                            tradeEventDayDataError = true;
                            ServerErrorCodeDispatch.getInstance().checkErrorCode(getContext(), ((ApiCodeErrorException) throwable).getState(), throwable.getMessage());
                        }
                        updateTradeEventDayData();
                    }
                });
    }

    private void updateTradeEventDayData(){
        if(date != null) {
            tvTime.setText(DateUtil.dateToString("yyyy年MM月dd日", date));
            tvWeek.setText(DateUtil.getWeekOfDate(date));
            if(!tradeEventDayDataError){
                tvEvent.setVisibility(View.VISIBLE);
            }
            setFirstSectionAtrb();
        }
    }
    //TODO reloadview状态

    /**
     * 隐藏
     */
    private void disMissReloadView() {
        rlReloadRootView.setVisibility(View.GONE);
        emptyView.setImageDrawable(null);
        tvEmptyAction.setText(null);
    }

    /**
     * 显示
     *
     * @param isErrorStatus
     */
    private void showReloadView(boolean isErrorStatus) {
        rlReloadRootView.setVisibility(View.VISIBLE);
        if (isErrorStatus) {
            emptyView.setImageResource(R.drawable.default_img_404);
            tvEmptyAction.setText(R.string.reload_text);
            if (mListView.getHeaderViewsCount() > 2) {
                mListView.removeHeaderView(bannerAds);
            }
        } else {
            emptyView.setImageResource(R.drawable.default_img_none);
            tvEmptyAction.setText(R.string.no_cms_dta);
        }
    }
    //TODO loading

    /**
     * 显示
     */
    private void showProgress() {
        newsProgress.setVisibility(View.VISIBLE);
        newsProgress.showAnimation(true);
    }

    /**
     * 隐藏
     */
    private void disMissProgress() {
        newsProgress.setVisibility(View.GONE);
        newsProgress.refreshDone(true);
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
