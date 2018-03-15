package com.changcai.buyer.im.main.fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.GetCounselorsModel;
import com.changcai.buyer.bean.GroupDetailModel;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.im.common.UniversalItemDecoration;
import com.changcai.buyer.im.main.activity.NotifactionSessionListActivity;
import com.changcai.buyer.im.main.adapter.TeamAdapter;
import com.changcai.buyer.im.main.present.NotifactionListPresentInterface;
import com.changcai.buyer.im.main.present.imp.NotifactionListPresentImp;
import com.changcai.buyer.im.main.viewmodel.NotifacitonListViewModel;
import com.changcai.buyer.im.session.SessionHelper;
import com.changcai.buyer.ui.base.BaseAbstraceFragment;
import com.changcai.buyer.ui.login.LoginActivity;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.ServerErrorCodeDispatch;
import com.changcai.buyer.view.ConfirmDialog;
import com.changcai.buyer.view.RotateDotsProgressView;
import com.netease.nim.uikit.business.session.TeamOnlineModel;
import com.netease.nim.uikit.common.util.sys.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * Created by lufeisong on 2018/3/12.
 */

public class NotifactionListFragment extends BaseAbstraceFragment implements NotifacitonListViewModel,TeamAdapter.GroupAdapterListener {
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
    @BindView(R.id.rv_group)
    RecyclerView rvGroup;


    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private NotifactionListPresentInterface present;

    List<GroupDetailModel> list = new ArrayList<>();//群组列表

    private boolean showCallPhoneAble = true;//是否显示拨打客服电话dialog

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initConfig() {
        super.initConfig();
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initData() {
        super.initData();
        adapter = new TeamAdapter(getActivity(),list,this);
        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rvGroup.addItemDecoration(new UniversalItemDecoration() {
            @Override
            public Decoration getItemOffsets(int position) {
                ColorDecoration colorDecoration = new ColorDecoration();
                colorDecoration.decorationColor = getResources().getColor(R.color.sea_gray);
                colorDecoration.bottom = getResources().getDimensionPixelSize(R.dimen.dim20);
                return colorDecoration;
            }
        });

        rvGroup.setLayoutManager(layoutManager);
        rvGroup.setAdapter(adapter);

        present = new NotifactionListPresentImp(this);

//        present.init();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        immersionBar.statusBarDarkFont(true, 0.2f).statusBarColor(R.color.white).fitsSystemWindows(true).init();
    }

    @Override
    public int setResId() {
        return R.layout.activity_notifaction_center;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            present.init("onHiddenChanged");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        present.init("onResume");
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d("onLine","onDestory");
        present.onDestory();
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
        SessionHelper.startP2MSession(getActivity(), (ArrayList<GetCounselorsModel.InfoBean>) info, null);
    }

    @Override
    public void getCounselorsModelFail(String failStr) {
        ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getActivity(), failStr);
    }

    @Override
    public void getCounselorsModelError() {
        ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getActivity(), getString(R.string.network_unavailable));
    }

    @Override
    public void unLogin() {
        Intent intent2 = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent2);
    }

    @Override
    public void toNOTIFACTION() {
        startActivity(new Intent(getActivity(), NotifactionSessionListActivity.class));
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
        ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getActivity(), failStr);
    }

    @Override
    public void loginNimSucceed() {

    }

    @Override
    public void updateConsultantStatus(final boolean showDot, final String message, final long time) {
        getActivity().runOnUiThread(new Runnable() {
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
        getActivity().runOnUiThread(new Runnable() {
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

    @Override
    public void updateTeamStatus(final int unReadCount,final String tid, final String message, final long time) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < list.size();i++){
                    GroupDetailModel groupDetailModel = list.get(i);
                    if(tid.equals(groupDetailModel.gettId())){
                        list.get(i).setMsgUnreadCount(unReadCount);
                        list.get(i).setMsgTime(time);
                        adapter.notifyItemChanged(i);
                        break;
                    }
                }
            }
        });
    }

    @Override
    public void joinTeam(String teamId) {
        SessionHelper.startTeamSession(getActivity(), teamId);
    }

    @Override
    public void unJoinTeam(String teamId) {
        if (showCallPhoneAble) {
            showCallPhoneAble = false;
            String info = "您尚未报名课堂活动，有疑问请联系买豆粕网客服。";
            for(GroupDetailModel groupDetailModel : list){
                if(teamId.equals(groupDetailModel.gettId())){
                    info = groupDetailModel.getTdesc();
                    break;
                }
            }
            ConfirmDialog.createConfirmDialog(getActivity(), "提示",info, "取消", "拨打客服电话", new ConfirmDialog.OnBtnConfirmListener() {
                @Override
                public void onConfirmListener() {
                    showCallPhoneAble = true;
                }
            }, new ConfirmDialog.OnBtnConfirmListener() {
                @Override
                public void onConfirmListener() {
                    showCallPhoneAble = true;
                    PermissionGen.needPermission(NotifactionListFragment.this, 100,
                            new String[]{
                                    Manifest.permission.CALL_PHONE,
                            }
                    );
                }
            });
        }

    }

    @Override
    public void unExistTeam() {
        ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getActivity(), "暂时无法加入此群");
    }

    @Override
    public void joinTeamFail(String fail) {
        ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getActivity(), fail);
    }

    @Override
    public void joinTeamError() {
        ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getActivity(), getString(R.string.network_unavailable));
    }

    @Override
    public void updateOnlineMembers(final boolean showAble, final List<TeamOnlineModel> teamOnlineModels) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0;i < list.size();i++){
                    for(int j = 0;j < teamOnlineModels.size();j++){
                        if(list.get(i).gettId().equals(teamOnlineModels.get(j).getTid())){
                            list.get(i).setOnLineMap(teamOnlineModels.get(j).getOnLineMap());
                            list.get(i).setOffLineMap(teamOnlineModels.get(j).getOffLineMap());
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void exitTeamByManager() {
        present.init("exitTeamByManager");
    }

    @Override
    public void kickOut() {
        present.init("kickOut");
    }

    @Override
    public void refreshGroupList(List<GroupDetailModel> teamList) {
        list.clear();
        list.addAll(teamList);
        adapter.notifyDataSetChanged();
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


    @PermissionSuccess(requestCode = 100)
    public void doSomethingSucceed() {
        showChooseDialog(SPUtil.getString(Constants.KEY_CONTACT_PHONE));

    }

    @PermissionFail(requestCode = 100)
    public void doSomethingFail() {
        Toast.makeText(getActivity(), R.string.perssion_for_call, Toast.LENGTH_LONG).show();
    }

    //群组信息点击事件
    @Override
    public void onClickListener(int position) {
        present.toTeam(position);
    }
}
