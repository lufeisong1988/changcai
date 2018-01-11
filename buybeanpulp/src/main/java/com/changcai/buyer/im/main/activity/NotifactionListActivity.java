package com.changcai.buyer.im.main.activity;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.changcai.buyer.CompatTouchBackActivity;
import com.changcai.buyer.R;
import com.changcai.buyer.bean.GetCounselorsModel;
import com.changcai.buyer.im.main.present.NotifactionListPresentInterface;
import com.changcai.buyer.im.main.present.imp.NotifactionListPresentImp;
import com.changcai.buyer.im.main.viewmodel.NotifacitonListViewModel;
import com.changcai.buyer.im.session.SessionHelper;
import com.changcai.buyer.ui.login.LoginActivity;
import com.changcai.buyer.util.NimSessionHelper;
import com.changcai.buyer.util.ServerErrorCodeDispatch;
import com.changcai.buyer.view.RotateDotsProgressView;
import com.netease.nim.uikit.common.util.sys.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lufeisong on 2017/12/19.
 */

public class NotifactionListActivity extends CompatTouchBackActivity implements NotifacitonListViewModel {


    @BindView(R.id.iv_vip_dot)
    ImageView ivVipDot;
    @BindView(R.id.tv_vip_content)
    TextView tvVipContent;
    @BindView(R.id.tv_vip_time)
    TextView tvVipTime;
    @BindView(R.id.cl_vip)
    ConstraintLayout clVip;
    @BindView(R.id.iv_consultant_dot)
    ImageView ivConsultantDot;
    @BindView(R.id.tv_consultant_content)
    TextView tvConsultantContent;
    @BindView(R.id.tv_consultant_time)
    TextView tvConsultantTime;
    @BindView(R.id.cl_consultant)
    ConstraintLayout clConsultant;
    @BindView(R.id.news_progress)
    RotateDotsProgressView newsProgress;
    private NotifactionListPresentInterface present;




    @Override
    protected void onResume() {
        super.onResume();
        present.init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        present.onDestory();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void injectFragmentView() {

        initData();
    }

    @Override
    protected int getTitleTextColor() {
        return getResources().getColor(R.color.black);
    }

    @Override
    protected int getNavigationVisible() {
        return View.VISIBLE;
    }

    @Override
    protected int getToolBarBackgroundColor() {
        return getResources().getColor(R.color.white);
    }

    @Override
    protected int getNavigationIcon() {
        return R.drawable.icon_nav_back;
    }

    @Override
    protected int getTitleText() {
        return R.string.notifaction_title;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_notifaction_center;
    }


    private void initData() {
        present = new NotifactionListPresentImp(this);
        present.getCounselorsModel(false);
        NimSessionHelper.getInstance().registerOnlineStatus(true);
    }


    //viewModel
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

    @Override
    public void getCounselorsModelSucceed(List<GetCounselorsModel.InfoBean> info) {
        SessionHelper.startP2MSession(NotifactionListActivity.this, (ArrayList<GetCounselorsModel.InfoBean>) info, null);
    }

    @Override
    public void getCounselorsModelFail(String failStr) {
        ServerErrorCodeDispatch.getInstance().showNetErrorDialog(this, failStr);
    }

    @Override
    public void getCounselorsModelError() {
        ServerErrorCodeDispatch.getInstance().showNetErrorDialog(this, getString(R.string.network_unavailable));
    }

    @Override
    public void unLogin() {
        Intent intent2 = new Intent(this, LoginActivity.class);
        startActivity(intent2);
    }

    @Override
    public void toNOTIFACTION() {
        startActivity(new Intent(NotifactionListActivity.this, NotifactionSessionListActivity.class));
    }

    @Override
    public void hideNOTIFACTION() {
        clConsultant.setVisibility(View.GONE);
    }

    @Override
    public void showNOTIFACTION() {
        clConsultant.setVisibility(View.VISIBLE);
    }

    @Override
    public void loginNimFail(String failStr) {
        ServerErrorCodeDispatch.getInstance().showNetErrorDialog(this, failStr);
    }

    @Override
    public void updateConsultantStatus(final boolean showDot, final String message, final long time) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (showDot) {
                    ivVipDot.setVisibility(View.VISIBLE);
                } else {
                    ivVipDot.setVisibility(View.GONE);
                }
                tvVipContent.setText(message);
                tvVipTime.setText(TimeUtil.getTimeShowString(time, true));
            }
        });

    }

    @Override
    public void updateAllStatus(final boolean showDot, final String message, final long time) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (showDot) {
                    ivConsultantDot.setVisibility(View.VISIBLE);
                } else {
                    ivConsultantDot.setVisibility(View.GONE);
                }
                tvConsultantContent.setText(message);
                tvConsultantTime.setText(TimeUtil.getTimeShowString(time, true));
            }
        });

    }


    @OnClick({R.id.cl_vip, R.id.cl_consultant})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cl_vip:
                present.toVip();
                break;
            case R.id.cl_consultant:
                present.toAnswers();
                break;
        }
    }

}
