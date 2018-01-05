package com.changcai.buyer.ui.strategy;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.changcai.buyer.BaseFragment;
import com.changcai.buyer.R;
import com.changcai.buyer.bean.Articles;
import com.changcai.buyer.bean.SpotFolderListBean;
import com.changcai.buyer.bean.StrategyInitModel;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.util.AndroidUtil;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.view.AutoEmptyView;
import com.changcai.buyer.view.CustomFontTextView;
import com.changcai.buyer.view.FontCache;
import com.changcai.buyer.view.MyAlertDialog;
import com.changcai.buyer.view.RotateDotsProgressView;
import com.changcai.buyer.view.XListView;
import com.changcai.buyer.view.indicator.FragmentContainerHelper;
import com.changcai.buyer.view.indicator.MagicIndicator;
import com.changcai.buyer.view.indicator.commonnavigator.CommonNavigator;
import com.changcai.buyer.view.indicator.commonnavigator.abs.CommonNavigatorAdapter;
import com.changcai.buyer.view.indicator.commonnavigator.abs.IPagerIndicator;
import com.changcai.buyer.view.indicator.commonnavigator.abs.IPagerTitleView;
import com.changcai.buyer.view.indicator.commonnavigator.indicators.WrapPagerIndicator;
import com.changcai.buyer.view.indicator.commonnavigator.titles.EnterTextChangedPagerTitleView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liuxingwei on 2017/7/26.
 * 现货策略
 */

public class PromptGoodsFragment extends BaseFragment implements PromptGoodsContract.View ,View.OnClickListener{


    @BindView(R.id.prompt_indicator)
    MagicIndicator promptIndicator;
    @BindView(R.id.mListView)
    XListView mListView;
    @BindView(R.id.tv_declare)
    CustomFontTextView customFontTextViewDeclare;
    @BindView(R.id.progress)
    RotateDotsProgressView progress;

    private ImageView iv_watermark;

    private FragmentContainerHelper containerHelper = new FragmentContainerHelper();

    private PromptGoodsAdapter promptGoodsAdapter;
//    private PromptHeaderView promptHeaderView;
    private LevelJudgementView levelJudgementView;
    private PromptGoodsContract.Presenter presenter;

    private List<Articles> articlesList;
    private boolean viewCreated;

    private int pageIndex;
    private String nowFolderId;
    private AutoEmptyView autoEmptyView;
    SimpleDateFormat mSDF = new SimpleDateFormat(
            "HH:mm");
    Date mDate = new Date();
    private boolean authorized;

    private Bundle authenticationBundle;

    CommonNavigatorAdapter commonNavigatorAdapter;
    private List<SpotFolderListBean> mSpotFolderListBeanList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("circle","onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prompt_goods, container, false);

        baseUnbinder = ButterKnife.bind(this, view);
        iv_watermark = (ImageView) view.findViewById(R.id.iv_watermark);
        LogUtil.d("circle","onCreateView");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListViewHeader();
        if(strategyInitModel != null){
            initIndicator(strategyInitModel.getSpotFolderList());
        }
        LogUtil.d("circle","onViewCreated");
        if(savedInstanceState != null){
            setAuthenticationBundle(savedInstanceState);
            requestPermissionFromServer(savedInstanceState.getBoolean("authorized"),false);
        }
        viewCreated = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d("circle","onDestroy");
        presenter.detach();
        viewCreated = false;
    }

    public Bundle getAuthenticationBundle() {
        return authenticationBundle;
    }

    public void setAuthenticationBundle(Bundle authenticationBundle) {
        this.authenticationBundle = authenticationBundle;
    }


    @Override
    public void setCustomFontTextViewDeclareVisible() {
        customFontTextViewDeclare.setVisibility(View.VISIBLE);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        LogUtil.d("circle","setUserVisibleHint");
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && viewCreated ) {
            if(authorized){
                if (!TextUtils.isEmpty(nowFolderId)) {
                    pageIndex = 0;
                    containerHelper.handlePageSelected(0);
                    nowFolderId = strategyInitModel.getSpotFolderList().get(0).getFolderId();
                    presenter.getSpotStrategy(pageIndex, nowFolderId);
                }
            }else{
                withoutPermission(getAuthenticationBundle());
            }


        }
    }


    private void initListViewHeader() {
        articlesList = new ArrayList<>();
//        promptHeaderView = new PromptHeaderView(getContext());
//        mListView.addHeaderView(promptHeaderView);
        mListView.setEnableOnRefreshWhileRefreshing(false);
        promptGoodsAdapter = new PromptGoodsAdapter(getContext(), articlesList);
        mListView.setAdapter(promptGoodsAdapter);
        mListView.setHeaderMargin(getResources().getDimensionPixelSize(R.dimen.dim10),0,0,0);
        mListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onPullRefresh() {
                pageIndex = 0;
                presenter.getSpotStrategyOnRefresh(nowFolderId);
            }

            @Override
            public void onPullLoadMore() {
                presenter.getSpotStrategy(++pageIndex, nowFolderId);
            }
        });
    }

    @Override
    public void setHeaderText(String headerText) {

//        promptHeaderView.setCustomFontText(headerText);
    }

    @Override
    public void addDataNormal(List<Articles> data) {
        articlesList.clear();
        if (data == null) {
            iv_watermark.setVisibility(View.GONE);
            //加载失败裂开图
            mListView.setPullLoadEnable(false);
            mListView.setPullRefreshEnable(false);
            if(autoEmptyView != null && autoEmptyView.getParent() != null){
                ((LinearLayout)autoEmptyView.getParent()).removeView(autoEmptyView);
            }
//            ((LinearLayout) mListView.getParent()).removeView(autoEmptyView);
            if (autoEmptyView == null) {
                autoEmptyView = new AutoEmptyView(getContext());
            }
            autoEmptyView.setErrorStatus();
            autoEmptyView.setOnClickListener(this);
            if(autoEmptyView != null && autoEmptyView.getParent() != null){
                ((LinearLayout)autoEmptyView.getParent()).removeView(autoEmptyView);
            }
            ((LinearLayout) mListView.getParent()).addView(autoEmptyView);
            mListView.setEmptyView(autoEmptyView);
        } else if (data.size() == 0) {
            iv_watermark.setVisibility(View.GONE);
            //没有内容
            mListView.setPullRefreshEnable(true);
            mListView.setPullLoadEnable(false);
            if(autoEmptyView != null && autoEmptyView.getParent() != null){
                ((LinearLayout)autoEmptyView.getParent()).removeView(autoEmptyView);
            }
//            ((LinearLayout) mListView.getParent()).removeView(autoEmptyView);
            if (autoEmptyView == null) {
                autoEmptyView = new AutoEmptyView(getContext());
            }
            autoEmptyView.setEmptyStatus();
            autoEmptyView.setOnClickListener(this);
            if(autoEmptyView != null && autoEmptyView.getParent() != null){
                ((LinearLayout)autoEmptyView.getParent()).removeView(autoEmptyView);
            }
            ((LinearLayout) mListView.getParent()).addView(autoEmptyView);
            mListView.setEmptyView(autoEmptyView);
        } else if (data.size() > 0) {
            iv_watermark.setVisibility(View.VISIBLE);
            articlesList.addAll(data);
            if (data.size() < 10) {
                mListView.setPullLoadEnable(false, R.string.no_more_conten, false);
            } else {
                mListView.setPullLoadEnable(true);
            }
        }
        promptGoodsAdapter.notifyDataSetChanged();
    }

    @Override
    public void addDataWithOnRefresh(List<Articles> data) {
        if (data == null) {
            iv_watermark.setVisibility(View.GONE);
            //加载失败裂开图
            mListView.setPullLoadEnable(true);
            mListView.setPullRefreshEnable(true);
            if(autoEmptyView != null && autoEmptyView.getParent() != null){
                ((LinearLayout)autoEmptyView.getParent()).removeView(autoEmptyView);
            }
//            ((LinearLayout) mListView.getParent()).removeView(autoEmptyView);
            if (autoEmptyView == null) {
                autoEmptyView = new AutoEmptyView(getContext());
            }
            autoEmptyView.setErrorStatus();
            if(autoEmptyView != null && autoEmptyView.getParent() != null){
                ((LinearLayout)autoEmptyView.getParent()).removeView(autoEmptyView);
            }
            ((LinearLayout) mListView.getParent()).addView(autoEmptyView);
            autoEmptyView.setOnClickListener(this);
            mListView.setEmptyView(autoEmptyView);
            mListView.stopRefresh();
        } else if (data.size() == 0) {
            iv_watermark.setVisibility(View.GONE);
            //没有内容
            articlesList.clear();
            mListView.stopRefresh("最后更新".concat(mSDF.format(mDate)));
            promptGoodsAdapter.notifyDataSetChanged();
            mListView.setPullRefreshEnable(true);
            mListView.setPullLoadEnable(false);
            if(autoEmptyView != null && autoEmptyView.getParent() != null){
                ((LinearLayout)autoEmptyView.getParent()).removeView(autoEmptyView);
            }
//            ((LinearLayout) mListView.getParent()).removeView(autoEmptyView);
            if (autoEmptyView == null) {
                autoEmptyView = new AutoEmptyView(getContext());
            }
            autoEmptyView.setEmptyStatus();
            autoEmptyView.setOnClickListener(this);
            if(autoEmptyView != null && autoEmptyView.getParent() != null){
                ((LinearLayout)autoEmptyView.getParent()).removeView(autoEmptyView);
            }
            ((LinearLayout) mListView.getParent()).addView(autoEmptyView);
            mListView.setEmptyView(autoEmptyView);
        } else if (data.size() > 0) {
            iv_watermark.setVisibility(View.VISIBLE);
            articlesList.clear();
            articlesList.addAll(data);
            promptGoodsAdapter.notifyDataSetChanged();
            mDate.setTime(System.currentTimeMillis());
            mListView.stopRefresh("最后更新".concat(mSDF.format(mDate)));
            if (articlesList.size() < 10) {
                mListView.setPullLoadEnable(false, R.string.no_more_conten, false);
            } else {
                mListView.setPullLoadEnable(true);
            }
        }
    }


    @Override
    public void addDataLoadMore(List<Articles> data) {
        if (data == null) {
            iv_watermark.setVisibility(View.GONE);
            //加载失败裂开图
            mListView.setPullLoadEnable(false, R.string.xlistview_footer_hint_no_more_fail, true);
            mListView.setPullRefreshEnable(false);
        } else if (data.size() == 0) {
            iv_watermark.setVisibility(View.GONE);
            //没有内容
            mListView.setPullRefreshEnable(true);
            mListView.setPullLoadEnable(false, R.string.no_more_conten, false);
        } else if (data.size() > 0) {
            iv_watermark.setVisibility(View.VISIBLE);
            articlesList.addAll(data);
            if (data.size() < 10) {
                mListView.setPullLoadEnable(false, R.string.no_more_conten, false);
            } else {
                mListView.setPullLoadEnable(true);
            }
        }
        promptGoodsAdapter.notifyDataSetChanged();
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }
    @Override
    public void initIndicator(List<SpotFolderListBean> folderListBeen) {
        if (mSpotFolderListBeanList != null) {
            mSpotFolderListBeanList.clear();
            mSpotFolderListBeanList.addAll(folderListBeen);
//            commonNavigatorAdapter.notifyDataSetChanged();
//            return;
        } else {
            mSpotFolderListBeanList = new ArrayList<>();
            mSpotFolderListBeanList.addAll(folderListBeen);
        }
        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        commonNavigatorAdapter = new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mSpotFolderListBeanList.size();
            }

            @SuppressWarnings("deprecation")
            @Override
            public IPagerTitleView getTitleView(final Context context, final int index) {
                EnterTextChangedPagerTitleView clipPagerTitleView = new EnterTextChangedPagerTitleView(context);
                clipPagerTitleView.setSelectedColor(getResources().getColor(R.color.white));
                clipPagerTitleView.setNormalColor(getResources().getColor(R.color.gray_chateau));
                clipPagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                clipPagerTitleView.setText(mSpotFolderListBeanList.get(index).getName());
                clipPagerTitleView.setPadding(getResources().getDimensionPixelSize(R.dimen.dim49));
                clipPagerTitleView.setTypeface(FontCache.getTypeface("ping_fang_light.ttf", context));
                clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        containerHelper.handlePageSelected(index);
                        nowFolderId = mSpotFolderListBeanList.get(index).getFolderId();
                        pageIndex = 0;
                        presenter.getSpotStrategy(pageIndex, nowFolderId);
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
    public void showLoadProgress() {
        mListView.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        progress.showAnimation(true);

    }

    @Override
    public void dismissLoadProgress() {
        progress.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
        progress.refreshDone(true);
    }


    private void withoutPermission(Bundle bundle) {
        iv_watermark.setVisibility(View.GONE);
        if (levelJudgementView == null) {
            levelJudgementView = new LevelJudgementView(getContext());
        }
        if (autoEmptyView != null) {
            autoEmptyView.setVisibility(View.GONE);
        }
        if(autoEmptyView != null && autoEmptyView.getParent() != null){
            ((LinearLayout)autoEmptyView.getParent()).removeView(autoEmptyView);
        }
//        ((LinearLayout)mListView.getParent()).removeView(autoEmptyView);
        if(levelJudgementView != null && levelJudgementView.getParent() != null){
            ((LinearLayout)levelJudgementView.getParent()).removeView(levelJudgementView);
        }
        ((LinearLayout)mListView.getParent()).addView(levelJudgementView);
        customFontTextViewDeclare.setVisibility(View.INVISIBLE);
        promptIndicator.setVisibility(View.GONE);
        if (articlesList != null) {
            articlesList.clear();
        }
        if (promptGoodsAdapter != null) {
            promptGoodsAdapter.notifyDataSetChanged();
        }
        mListView.setEmptyView(levelJudgementView);
        levelJudgementView.showPermission(bundle,"spot");
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(PromptGoodsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showErrorDialog(String message) {
        MyAlertDialog alertDialog = new MyAlertDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", message);
        bundle.putInt("icon", R.drawable.icon_alt_fail);
        alertDialog.setArguments(bundle);
        try {
            alertDialog.show(getChildFragmentManager(), "alert");
        }catch (Exception e){

        }

    }
    StrategyInitModel strategyInitModel;

    boolean isForce;
    public void requestPermissionFromServer(boolean authorized, boolean isForce) {
        LogUtil.d("3.2.0","fragment one authorized = " + authorized + ";isForce = " + isForce);
        this.isForce = isForce;
        if (mListView.getVisibility() == View.GONE) {
            if (autoEmptyView !=null && autoEmptyView.getVisibility() ==View.VISIBLE){

            }else{
                mListView.setVisibility(View.VISIBLE);
            }
        }
        if (authorized) {
            if (!isForce) {
                if (this.authorized == authorized) return;
            }
            hasPermission();
            if (getAuthenticationBundle() != null && getAuthenticationBundle().containsKey("StrategyInitModel")) {
                strategyInitModel = (StrategyInitModel) getAuthenticationBundle().getSerializable("StrategyInitModel");
                initIndicator(strategyInitModel.getSpotFolderList());
                if (TextUtils.isEmpty(nowFolderId)) {
                    nowFolderId = strategyInitModel.getSpotFolderList().get(0).getFolderId();
                }
                if (Constants.TABINDEX == 0) {
                    if (Constants.FOLDER >= 0 && Constants.FOLDER < strategyInitModel.getSpotFolderList().size() - 1) {
                        if (nowFolderId.equalsIgnoreCase(strategyInitModel.getSpotFolderList().get(Constants.FOLDER).getFolderId())) {
                            Constants.FOLDER = -1;
                            Constants.TABINDEX = -1;
                        } else {
                            nowFolderId = strategyInitModel.getSpotFolderList().get(Constants.FOLDER).getFolderId();
                            containerHelper.handlePageSelected(Constants.FOLDER);
                            Constants.FOLDER = -1;
                            Constants.TABINDEX = -1;
                        }
                    }else{
                        return;
                    }
                }
                presenter.getSpotStrategy(pageIndex, nowFolderId);
            }
        } else {
            withoutPermission(getAuthenticationBundle());
        }
        this.authorized = authorized;
    }

    private void hasPermission() {
        if(levelJudgementView != null && levelJudgementView.getParent() != null){
            ((LinearLayout)levelJudgementView.getParent()).removeView(levelJudgementView);
        }
//        ((LinearLayout) mListView.getParent()).removeView(levelJudgementView);
        promptIndicator.setVisibility(View.VISIBLE);
    }

    public void setAuthenticationFail() {
        mListView.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof AutoEmptyView){
            presenter.getSpotStrategy(pageIndex,nowFolderId);
        }
    }




    @Override
    public void onSaveInstanceState(Bundle outState) {
        LogUtil.d("circle","onSaveInstanceState");
        super.onSaveInstanceState(outState);
        outState.putSerializable("StrategyInitModel", strategyInitModel);
        outState.putBoolean("authorized",authorized);
    }


}
