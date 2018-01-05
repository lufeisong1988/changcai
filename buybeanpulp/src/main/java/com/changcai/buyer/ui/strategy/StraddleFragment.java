package com.changcai.buyer.ui.strategy;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.changcai.buyer.BaseFragment;
import com.changcai.buyer.R;
import com.changcai.buyer.bean.StraddleModel;
import com.changcai.buyer.listener.CustomListener;
import com.changcai.buyer.util.AndroidUtil;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.view.AutoEmptyView;
import com.changcai.buyer.view.CustomFontTextView;
import com.changcai.buyer.view.MyAlertDialog;
import com.changcai.buyer.view.RotateDotsProgressView;
import com.changcai.buyer.view.XListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liuxingwei on 2017/7/26.
 * 套利图景
 */

public class StraddleFragment extends BaseFragment implements StraddleContract.View, View.OnClickListener {


    @BindView(R.id.mListView)
    XListView mListView;
    @BindView(R.id.tv_explain)
    CustomFontTextView tvExplain;
    @BindView(R.id.progressView)
    RotateDotsProgressView progressView;
    private StraddleListAdapter straddleListAdapter;

    private StraddleContract.Presenter presenter;

    private List<StraddleModel> straddleModelList;
    private boolean isCreated;
    protected int pageIndex;
    SimpleDateFormat mSDF = new SimpleDateFormat(
            "HH:mm");
    Date mDate = new Date();

    private AutoEmptyView autoEmptyView;
    private boolean isAuthority;
    private LevelJudgementView levelJudgementView;
    private Bundle authenticationBundle;

    public Bundle getAuthenticationBundle() {
        return authenticationBundle;
    }

    public void setAuthenticationBundle(Bundle authenticationBundle) {
        this.authenticationBundle = authenticationBundle;
    }

    private void setTvExplainVisible() {
        tvExplain.setVisibility(View.VISIBLE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.straddle_layout, container, false);
        baseUnbinder = ButterKnife.bind(this, view);
        LogUtil.d("StraddleFragment","onCreateView");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList();
        LogUtil.d("StraddleFragment","onViewCreated");
        isCreated = true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        LogUtil.d("StraddleFragment","setUserVisibleHint");
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isCreated) {
            if(isAuthority){
                presenter.getStraddleData(pageIndex);
            }else{
                withoutPermission(getAuthenticationBundle());
            }

        }
    }

    @Override
    public void onAttach(Activity activity) {
        LogUtil.d("StraddleFragment","onAttach");
        super.onAttach(activity);
    }

    @Override
    public void onStart() {
        LogUtil.d("StraddleFragment","onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        LogUtil.d("StraddleFragment","onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        LogUtil.d("StraddleFragment","onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        LogUtil.d("StraddleFragment","onStop");
        super.onStop();
    }

    @Override
    public void showProgress() {
        mListView.setVisibility(View.GONE);
        progressView.setVisibility(View.VISIBLE);
        progressView.showAnimation(true);
    }

    @Override
    public void dismissProgress() {
        progressView.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
        progressView.refreshDone(true);
    }


    private void initList() {
        straddleModelList = new ArrayList<>();
        straddleListAdapter = new StraddleListAdapter(getContext(), straddleModelList);
        mListView.setAdapter(straddleListAdapter);
        mListView.setHeaderMargin(getResources().getDimensionPixelSize(R.dimen.dim10), 0, 0, 0);
        mListView.setEnableOnRefreshWhileRefreshing(false);
        mListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onPullRefresh() {
                pageIndex = 0;
                presenter.getStraddleDataOnRefresh();
            }

            @Override
            public void onPullLoadMore() {
                presenter.getStraddleData(++pageIndex);
            }
        });
        straddleListAdapter.setCustomListener(new CustomListener() {
            @Override
            public void onCustomerListener(View v, int position) {
                StraddleModel straddleModel = straddleModelList.get(position);
                if (!TextUtils.isEmpty(straddleModel.getArticleUrl())) {
                    Bundle b = new Bundle();
                    b.putString("url", straddleModel.getArticleUrl());
                    if (!TextUtils.isEmpty(straddleModel.getTitle())) {
                        b.putString("title", straddleModel.getTitle());
                    } else {
                        b.putString("title", "资讯详情");
                    }
                    b.putString("info", straddleModel.getContent());
                    AndroidUtil.startBrowser(activity, b, false);
                }
            }
        });
    }

    /**
     * 普通加载
     *
     * @param straddleModels
     */
    @Override
    public void addDataList(List<StraddleModel> straddleModels) {
        setTvExplainVisible();
        if (straddleModels == null) {
            //加载失败裂开图
            straddleModelList.clear();
            mListView.setPullLoadEnable(false);
            mListView.setPullRefreshEnable(false);
            straddleListAdapter.notifyDataSetChanged();
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
        } else if (straddleModels.size() == 0) {
            //没有内容
            if (mListView.getVisibility() == View.VISIBLE) {
                mListView.setVisibility(View.GONE);
            }
            straddleModelList.clear();
            straddleListAdapter.notifyDataSetChanged();
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
            if(autoEmptyView != null && autoEmptyView.getParent() != null){
                ((LinearLayout)autoEmptyView.getParent()).removeView(autoEmptyView);
            }
            ((LinearLayout) mListView.getParent()).addView(autoEmptyView);
            mListView.setEmptyView(autoEmptyView);
        } else if (straddleModels.size() > 0) {
            straddleModelList.clear();
            straddleModelList.addAll(straddleModels);
            straddleListAdapter.notifyDataSetChanged();
            if (straddleModels.size() < 10) {
                mListView.setPullLoadEnable(false, R.string.no_more_conten, false);
            } else {
                mListView.setPullLoadEnable(true);
            }
        }
    }

    @Override
    public void addDataListWithOnRefresh(List<StraddleModel> straddleModels) {
        setTvExplainVisible();
        if (straddleModels == null) {
            //加载失败裂开图
            mListView.stopRefresh();
            mListView.setPullLoadEnable(true);
            mListView.setPullRefreshEnable(false);
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
            mListView.setEmptyView(autoEmptyView);
        } else if (straddleModels.size() == 0) {
            //没有内容
            straddleModelList.clear();
            mListView.stopRefresh();
            mListView.setPullRefreshEnable(true);
            mListView.setPullLoadEnable(false);
            straddleListAdapter.notifyDataSetChanged();
            if(autoEmptyView != null && autoEmptyView.getParent() != null){
                ((LinearLayout)autoEmptyView.getParent()).removeView(autoEmptyView);
            }
//            ((LinearLayout) mListView.getParent()).removeView(autoEmptyView);
            if (autoEmptyView == null) {
                autoEmptyView = new AutoEmptyView(getContext());
            }
            autoEmptyView.setEmptyStatus();
            if(autoEmptyView != null && autoEmptyView.getParent() != null){
                ((LinearLayout)autoEmptyView.getParent()).removeView(autoEmptyView);
            }
            ((LinearLayout) mListView.getParent()).addView(autoEmptyView);
            mListView.setEmptyView(autoEmptyView);
        } else if (straddleModels.size() > 0) {
            straddleModelList.clear();
            straddleModelList.addAll(straddleModels);
            straddleListAdapter.notifyDataSetChanged();
            mDate.setTime(System.currentTimeMillis());
            mListView.stopRefresh("最后更新".concat(mSDF.format(mDate)));
            if (straddleModelList.size() < 10) {
                mListView.setPullLoadEnable(false, R.string.no_more_conten, false);
            } else {
                mListView.setPullLoadEnable(true);
            }
        }
    }

    @Override
    public void addDataListWithLoadMore(List<StraddleModel> straddleModels) {
        setTvExplainVisible();
        if (straddleModels == null) {
            //加载失败裂开图
            mListView.setPullLoadEnable(false, R.string.xlistview_footer_hint_no_more_fail, true);
            mListView.setPullRefreshEnable(false);
        } else if (straddleModels.size() == 0) {
            //没有内容
            mListView.setPullRefreshEnable(true);
            mListView.setPullLoadEnable(false, R.string.no_more_conten, false);
        } else if (straddleModels.size() > 0) {
            straddleModelList.addAll(straddleModels);
            if (straddleModels.size() < 10) {
                mListView.setPullLoadEnable(false, R.string.no_more_conten, false);
            } else {
                mListView.setPullLoadEnable(true);
            }
        }
        straddleListAdapter.notifyDataSetChanged();
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }


    @Override
    public boolean isActive() {
        return isAdded();
    }


    @Override
    public void setPresenter(StraddleContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showErrorDialog(String message) {
        MyAlertDialog alertDialog = new MyAlertDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", message);
        bundle.putInt("icon", R.drawable.icon_alt_fail);
        alertDialog.setArguments(bundle);
        alertDialog.show(getChildFragmentManager(), "alert");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }


    public void requestPermissionFromServer(boolean isAuthority, boolean isForce) {
        LogUtil.d("3.2.0","fragment two isAuthority = " + isAuthority + ";isForce = " + isForce);
        if (mListView.getVisibility() == View.GONE) {
            if (autoEmptyView !=null && autoEmptyView.getVisibility() ==View.VISIBLE){

            }else{
                mListView.setVisibility(View.VISIBLE);
            }
        }
        if (isAuthority) {
            if (!isForce) {
                if (this.isAuthority == isAuthority) return;
            }
            hasPermission();
            if (getAuthenticationBundle() != null && getAuthenticationBundle().containsKey("StrategyInitModel")) {
                presenter.getStraddleData(pageIndex);
            }
        } else {
            withoutPermission(getAuthenticationBundle());
        }
        this.isAuthority = isAuthority;
    }

    private void withoutPermission(Bundle arguments) {
        if (levelJudgementView == null) {
            levelJudgementView = new LevelJudgementView(getContext());
        }
        if (autoEmptyView != null){
            autoEmptyView.setVisibility(View.GONE);
        }
        if(levelJudgementView != null && levelJudgementView.getParent() != null){
            ((LinearLayout)levelJudgementView.getParent()).removeView(levelJudgementView);
        }
//        ((LinearLayout) mListView.getParent()).removeView(levelJudgementView);
        if(autoEmptyView != null && autoEmptyView.getParent() != null){
            ((LinearLayout)autoEmptyView.getParent()).removeView(autoEmptyView);
        }
//        ((LinearLayout) mListView.getParent()).removeView(autoEmptyView);
        ((LinearLayout) mListView.getParent()).addView(levelJudgementView);
        tvExplain.setVisibility(View.INVISIBLE);
        if (straddleModelList != null) {
            straddleModelList.clear();
        }
        if (straddleListAdapter != null) {
            straddleListAdapter.notifyDataSetChanged();
        }
        mListView.setEmptyView(levelJudgementView);
        levelJudgementView.showPermission(arguments, "arbitrage");
    }

    private void hasPermission() {
        if(levelJudgementView != null && levelJudgementView.getParent() != null){
            ((LinearLayout)levelJudgementView.getParent()).removeView(levelJudgementView);
        }
//        ((LinearLayout) mListView.getParent()).removeView(levelJudgementView);
    }

    public void setAuthenticationFail() {
        mListView.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof AutoEmptyView) {
            presenter.getStraddleData(pageIndex);
        }
    }
}
