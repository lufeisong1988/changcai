package com.changcai.buyer.im.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.changcai.buyer.CompatTouchBackActivity;
import com.changcai.buyer.R;
import com.changcai.buyer.im.main.adapter.TeamMemberItemAdapter;
import com.changcai.buyer.im.main.present.TeamMemberPresent;
import com.changcai.buyer.im.main.present.imp.TeamMemberPresentImp;
import com.changcai.buyer.im.main.viewmodel.TeamMemberViewModel;
import com.changcai.buyer.rx.RxBus;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.util.ServerErrorCodeDispatch;
import com.changcai.buyer.view.ConfirmDialog;
import com.changcai.buyer.view.RotateDotsProgressView;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nimlib.sdk.team.constant.TeamMemberType;
import com.netease.nimlib.sdk.team.model.TeamMember;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by lufeisong on 2018/1/15.
 */

public class TeamMemberActivity extends CompatTouchBackActivity implements TeamMemberViewModel, TeamMemberItemAdapter.TeamMemberItemAdapterCallback {
    @BindView(R.id.gv_userIcon)
    GridView gvUserIcon;
    @BindView(R.id.news_progress)
    RotateDotsProgressView newsProgress;

    private TeamMemberPresent present;

    private TeamMemberItemAdapter adapter;

    private Observable<Boolean> teamMemberEvent;

    private List<TeamMember> teamMembers = new ArrayList<>();
    private HashMap<String, String> onLineMap = new HashMap<>();
    private HashMap<String, String> offLineMap = new HashMap<>();
    private String teamId;

    public static void start(Context context, String teamId) {
        Intent intent = new Intent();
        intent.setClass(context, TeamMemberActivity.class);
        intent.putExtra(Extras.EXTRA_TEAM_ID, teamId);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void injectFragmentView() {
        teamMemberEvent = RxBus.get().register("teamMember", Boolean.class);
        teamMemberEvent.subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ServerErrorCodeDispatch.getInstance().showDialog(TeamMemberActivity.this, "添加成员成功", R.drawable.artboard);
                        }
                    }, 500);
                }
            }
        });
        teamId = getIntent().getExtras().getString(Extras.EXTRA_TEAM_ID);
        adapter = new TeamMemberItemAdapter(teamMembers, this, this, onLineMap, offLineMap);
        present = new TeamMemberPresentImp(this, teamId, this);
        gvUserIcon.setAdapter(adapter);
                gvUserIcon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < teamMembers.size()) {
                    TeamMember teamMember = teamMembers.get(position);
                    if ((boolean) view.getTag(R.id.show_delete_able)) {
                        if (teamMember.getType().getValue() != TeamMemberType.Manager.getValue() && teamMember.getType().getValue() != TeamMemberType.Owner.getValue() ) {
                            showDeleteMember(teamMember.getAccount());
                        }
                    } else {
                        UserProfileActivity.start(TeamMemberActivity.this, teamMember.getAccount(),new HashMap<String, Object>() );
                    }
                } else if (position == teamMembers.size()) {//增加按钮
                    adapter.addMember();
                    TeamMemberAddActivity.start(TeamMemberActivity.this, teamId);
                } else if (position == teamMembers.size() + 1) {//减少按妞
                    adapter.removeMember();
                }

            }
        });
    }

    @Override
    protected int getTitleTextColor() {
        return getResources().getColor(R.color.black);
    }

    @Override
    protected int getNavigationVisible() {
        return View.VISIBLE;
    }

    @SuppressWarnings("deprecation")
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
        return R.string.empty;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_teammember;
    }

    @Override
    protected void onResume() {
        super.onResume();
        present.queryMemberList();
    }

    @Override
    protected void onDestroy() {
        present.onDestory();
        RxBus.get().unregister("teamMember", teamMemberEvent);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

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
    public void queryMemberListSucceed(List<TeamMember> teamMembers) {
        this.teamMembers.clear();
        this.teamMembers.addAll(teamMembers);
        adapter.customNotifyDataChange(teamMembers);
    }

    @Override
    public void queryMemberListFail(String failStr) {
        ServerErrorCodeDispatch.getInstance().showNetErrorDialog(this, failStr);
    }

    @Override
    public void queryMemberListError() {
        ServerErrorCodeDispatch.getInstance().showNetErrorDialog(this, "网络异常，获取信息失败");
    }

    @Override
    public void removeMemberSucceed(String member) {
        ServerErrorCodeDispatch.getInstance().showDialog(TeamMemberActivity.this, "删除成员成功", R.drawable.artboard);
        LogUtil.d("NimIM", "delete account = " + member);
        present.queryMemberList();
    }

    @Override
    public void removeMemberFail(String failStr) {
        ServerErrorCodeDispatch.getInstance().showNetErrorDialog(this, failStr);
    }

    @Override
    public void removeMemberError() {
        ServerErrorCodeDispatch.getInstance().showNetErrorDialog(this, "网络异常，获取信息失败");
    }

    @Override
    public void updateOnlineMembers(int onLineMemberNums, int totalMemberNums) {
        titleText.setText(onLineMemberNums + "/" + totalMemberNums + "人在线");
    }

    @Override
    public void exitTeamByManager() {
        titleText.setText("您已退出该群");
    }

    @Override
    public void updateOnlineMembersAdapter(HashMap<String, String> onLineMap, HashMap<String, String> offLineMap) {
        this.onLineMap.clear();
        this.offLineMap.clear();
        this.onLineMap.putAll(onLineMap);
        this.offLineMap.putAll(offLineMap);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showDeleteMember(final String member) {
        ConfirmDialog.createConfirmDialog(this, "提示", "确认删除该用户？", "取消", "确认", new ConfirmDialog.OnBtnConfirmListener() {
            @Override
            public void onConfirmListener() {

            }
        }, new ConfirmDialog.OnBtnConfirmListener() {
            @Override
            public void onConfirmListener() {
                present.removeMember(member);
            }
        });
    }
}
