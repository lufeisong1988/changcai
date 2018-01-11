package com.changcai.buyer.ui.news;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changcai.buyer.CommonApplication;
import com.changcai.buyer.R;
import com.changcai.buyer.bean.GetQuotePriceBean;
import com.changcai.buyer.bean.NoticeBean;
import com.changcai.buyer.bean.RecommendListBean;
import com.changcai.buyer.listener.PicassoPauseOnScrollListener;
import com.changcai.buyer.rx.RxBus;
import com.changcai.buyer.ui.base.BaseAbstraceFragment;
import com.changcai.buyer.ui.login.LoginActivity;
import com.changcai.buyer.ui.main.MainActivity;
import com.changcai.buyer.ui.news.adapter.NewIndexAdapter2;
import com.changcai.buyer.ui.news.adapter.NewIndexAdapter2.NewIndexAdapterCallback;
import com.changcai.buyer.ui.news.bean.NewsClassify;
import com.changcai.buyer.ui.news.bean.NewsEntity;
import com.changcai.buyer.ui.news.bean.RecommendNewsEntity;
import com.changcai.buyer.ui.news.present.NewIndexPresent;
import com.changcai.buyer.ui.news.present.NewIndexPresentInterface;
import com.changcai.buyer.util.AndroidUtil;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.util.ResUtil;
import com.changcai.buyer.util.ServerErrorCodeDispatch;
import com.changcai.buyer.view.CustomFontTextView;
import com.changcai.buyer.view.CustomNewIndexNoticeView;
import com.changcai.buyer.view.CustomNewIndexPriceView;
import com.changcai.buyer.view.CustomNewIndexRecommendView;
import com.changcai.buyer.view.HomeAdsHolderView;
import com.changcai.buyer.view.RotateDotsProgressView;
import com.changcai.buyer.view.XListView;
import com.changcai.buyer.view.banner.CBViewHolderCreator;
import com.changcai.buyer.view.banner.ConvenientBanner;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by lufeisong on 2017/10/12.
 */

public class NewIndexFragment extends BaseAbstraceFragment implements View.OnClickListener,  NewIndexViewModel, CustomNewIndexNoticeView.NoticeListener, CustomNewIndexRecommendView.TradeListener,NewIndexAdapterCallback {

    @BindView(R.id.emptyView)
    ImageView emptyView;
    @BindView(R.id.tv_empty_action)
    TextView tvEmptyAction;
    @BindView(R.id.rl_reload_root_view)
    LinearLayout rlReloadRootView;
    @BindView(R.id.news_progress)
    RotateDotsProgressView newsProgress;
    @BindView(R.id.mListView)
    XListView mListView;



    private Observable<Boolean> logoutEvent;//登录状态
    private Observable<Boolean> backgroundToForegroundEvent;//从后台回到前台

    private Bitmap[] indicator = new Bitmap[2];//指示器
    private ConvenientBanner bannerAds;


    //资讯列表
    private NewIndexAdapter2 adapter;//适配器

    //点击的item
    private NewsEntity news;//item的详情
    private View tempView;  //被点击的item的临时view
    private int tempPosition = 0;
    private int[] screenLocation = new int[2];


    private View newIndexView;
    private CustomFontTextView tv_morePrice;
    private CustomNewIndexNoticeView noticeView;
    private CustomNewIndexRecommendView recommendView;
    private CustomNewIndexPriceView priceView;


    private List<NewsClassify> newsClassifys;
    private List<String> groups = new ArrayList<>();
    private List<NewsEntity> articleList = new ArrayList<>();

    private NewIndexPresentInterface present;
    private SimpleDateFormat mSDF = new SimpleDateFormat(
            "HH:mm");
    private Date mDate = new Date();
    public NewIndexFragment() {
    }

    public NewIndexFragment(List<NewsClassify> newsClassifys) {
        this.newsClassifys = newsClassifys;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logoutEvent = RxBus.get().register("inOrOutAction", Boolean.class);
        logoutEvent.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (tempView != null && NewIndexFragment.this.isVisible()) {
                            goDetails(tempView, tempPosition);
                        }
                        refreshData();
                    }
                });

        backgroundToForegroundEvent = RxBus.get().register("backgroundToForeground", Boolean.class);
        backgroundToForegroundEvent.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                initData();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(setResId(), null);
        ButterKnife.bind(this, view);
        if(savedInstanceState != null){
            newsClassifys = (List<NewsClassify>) savedInstanceState.getSerializable("newsClassifys");
        }
        initView();
        initListener();
        initConfig();
        initData();
        return view;
    }

    @Override
    public int setResId() {
        return R.layout.fragment_new_index;
    }

    @Override
    protected void initSystemStatusBar() {
        super.initSystemStatusBar();
        immersionBar.statusBarDarkFont(true, 0.2f).statusBarColor(R.color.white).fitsSystemWindows(true).init();
    }

    @Override
    public void onResume() {
        super.onResume();
//        present.init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        present.destory();
        RxBus.get().unregister("inOrOutAction", logoutEvent);
        RxBus.get().unregister("backgroundToForeground", backgroundToForegroundEvent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("newsClassifys", (Serializable) newsClassifys);
        super.onSaveInstanceState(outState);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


    @Override
    protected void initView() {
        bannerAds = (ConvenientBanner) LayoutInflater.from(getActivity()).inflate(R.layout.news_banner, null);
        newIndexView = LayoutInflater.from(getActivity()).inflate(R.layout.view_new_index, null);
        noticeView = (CustomNewIndexNoticeView) newIndexView.findViewById(R.id.noticeView);
        recommendView = new CustomNewIndexRecommendView(getActivity());
        priceView = new CustomNewIndexPriceView(getActivity());
        tv_morePrice = (CustomFontTextView) priceView.findViewById(R.id.tv_morePrice);

    }


    @Override
    protected void initConfig() {
        //添加dot背景图

        indicator[0] = ResUtil.drawableToBitamp(getActivity(), R.drawable.page_indicator_for_home_normal);
        indicator[1] = ResUtil.drawableToBitamp(getActivity(), R.drawable.page_indicator_for_home_focused);
        bannerAds.setPageIndicator(indicator, null);



        //添加适配器
        if (adapter == null) {
            adapter = new NewIndexAdapter2(getActivity(), articleList,this);
        }
        mListView.setAdapter(adapter);
        mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(true);
        mListView.setHeaderBgNewStyle();
        mListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onPullRefresh() {
                refreshData();
            }

            @Override
            public void onPullLoadMore() {
            }
        });

        mListView.addHeaderView(bannerAds);
        mListView.addHeaderView(newIndexView);
        mListView.addHeaderView(recommendView);
        mListView.addHeaderView(priceView);
        present = new NewIndexPresent(getActivity(), this, newsClassifys);
    }

    @Override
    protected void initData() {
        present.init();
    }

    private void refreshData() {
        present.refresh();
    }

    @Override
    protected void initListener() {
        tv_morePrice.setOnClickListener(this);
        rlReloadRootView.setOnClickListener(this);
        recommendView.setListener(this);
        noticeView.setListener(this);
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
    public void checkData() {
        if (rlReloadRootView.getVisibility() == View.VISIBLE && tvEmptyAction.getText().toString().equals(getActivity().getResources().getString(R.string.reload_text))) {
            initData();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_reload_root_view://加载错误，重新加载数据
                initData();
                disMissReloadView();
                break;

            case R.id.tv_morePrice:
                RxBus.get().post("switchTabObservable", 2);
                break;
        }
    }

    //TODO 跳转详情页面

    /**
     * 跳转详情页面(1)
     * item点击回调
     *
     * @param v
     */
    public void onCustomerListener(View v, int position) {
        news = articleList.get(position);
        tempView = v;
        tempPosition = position;


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
//            NewsReader.getSingleton().addAlreadyNewsId(position, news.getArticleId());
//            adapter.notifyDataSetChanged();
            news = null;
            tempView = null;
            tempPosition = 0;

        }
    }


    private List<RecommendNewsEntity> tempAdLists = new ArrayList<>();

    //广告状态
    @Override
    public void updateAdSucceed(List<RecommendNewsEntity> adLists) {
        tempAdLists = adLists;
        if (adLists == null || adLists.size() == 0) {
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
                }, adLists);
        /**
         * 大于1个则自动翻页,轮播时间5000。
         */
        if (adLists.size() > 1) {
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
    public void updateAdFail() {
        if (tempAdLists == null || tempAdLists.size() == 0) {
            bannerAds.setVisibility(View.GONE);
        }
        refreshFail();
        if (isAdded()) {
            ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.net_error));
        }
    }

    @Override
    public void updateAdError() {
        if (tempAdLists == null || tempAdLists.size() == 0) {
            bannerAds.setVisibility(View.GONE);
        }
        refreshFail();
        if (isAdded()) {
            ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.network_unavailable));
        }
    }

    //公告状态
    @Override
    public void updateNoticeSucceed(List<NoticeBean.NoticeListBean> noticeList) {
        noticeView.update(noticeList);
    }

    @Override
    public void updateNoticeFail() {
        refreshFail();
        if (isAdded()) {
            ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.net_error));
        }
    }

    @Override
    public void updateNoticeError() {
        refreshFail();
        if (isAdded()) {
            ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.network_unavailable));
        }
    }

    //行业日历状态
    @Override
    public void updateTradeEventSucceed(Date date) {
        if (date != null) {
            recommendView.showTradeEventView(date);
        }
    }

    @Override
    public void updateTradeEventFail(Date date) {
        if (date != null) {
            recommendView.showTradeEventView(date);
        }
        refreshFail();
    }

    @Override
    public void updateTradeEventError() {
        recommendView.hideTradeEventView();
        refreshFail();
        if (isAdded()) {
            ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.network_unavailable));
        }
    }

    //为你推荐状态
    @Override
    public void updateRecommendSucceed(List<RecommendListBean.RecommendationListBean> recommendationList) {
        recommendView.updateRecommend(recommendationList);
    }

    @Override
    public void updateRecommendFail() {
        refreshFail();
        if (isAdded()) {
            ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.net_error));
        }
    }

    @Override
    public void updateRecommendError() {
        refreshFail();
        if (isAdded()) {
            ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.network_unavailable));
        }
    }

    //最新报价状态
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void updatePriceSucceed(List<GetQuotePriceBean.QuotePriceBean.ResultBean> resultBeen) {
        priceView.updatePrice(resultBeen);
    }

    @Override
    public void updatePriceFail() {
        refreshFail();
        if (isAdded()) {
            ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.net_error));
        }
    }

    @Override
    public void updatePriceError() {
        refreshFail();
        if (isAdded()) {
            ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.network_unavailable));
        }
    }

    //新闻状态
    @Override
    public void updateArticleListSucceed(List<NewsEntity> articles,List<String> articleGroups) {
        articleList.clear();
        groups.clear();
        articleList.addAll(articles);
        groups.addAll(articleGroups);
        adapter.notifyDataSetChanged();
        refreshSucceed();
    }

    @Override
    public void updateArticleListFail() {
        refreshFail();
        if (isAdded()) {
            ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.net_error));
        }
    }

    @Override
    public void updateArticleListError() {
        refreshFail();
        if (isAdded()) {
            ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.network_unavailable));
        }
    }


    private void refreshSucceed() {
        mDate.setTime(System.currentTimeMillis());
        String time = mSDF.format(mDate);
        mListView.stopRefresh("最后更新 ".concat(time));
        disMissReloadView();
        bannerAds.setVisibility(View.VISIBLE);
        newIndexView.setVisibility(View.VISIBLE);
        recommendView.setVisibility(View.VISIBLE);
        priceView.setVisibility(View.VISIBLE);
    }

    private void refreshFail() {
        mDate.setTime(System.currentTimeMillis());
        String time = mSDF.format(mDate);
        mListView.stopRefresh("最后更新 ".concat(time));
        showReloadView();
        bannerAds.setVisibility(View.GONE);
        newIndexView.setVisibility(View.GONE);
        recommendView.setVisibility(View.GONE);
        priceView.setVisibility(View.GONE);
        groups.clear();
        articleList.clear();
        adapter.notifyDataSetChanged();
    }

    //TODO loading状态
    @Override
    public void showLoading() {
        newsProgress.setVisibility(View.VISIBLE);
        newsProgress.showAnimation(true);
    }

    @Override
    public void dismissLoading() {
        newsProgress.setVisibility(View.GONE);
        newsProgress.refreshDone(true);
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
     */
    private void showReloadView() {
        rlReloadRootView.setVisibility(View.VISIBLE);
        emptyView.setImageResource(R.drawable.default_img_404);
        tvEmptyAction.setText(R.string.reload_text);
    }

    //TODO NoticeListener
    @Override
    public void noticeCallback() {

    }

    //TODO TradeListener
    @Override
    public void tradeCallback() {
        openActivity(CalendarActivity.class);
    }

    @Override
    public void recommendCallback() {

//        ToastUtil.showLong(getActivity(),"recommend click");
    }



    @Override
    public void groupCallback(String flag) {
        int index = groups.indexOf(flag) + 1;
        if(index > 0){
            RxBus.get().post("switchPage",index );
        }

    }

    @Override
    public void childCallback(View v,int position) {
        onCustomerListener(v, position);
    }
}
