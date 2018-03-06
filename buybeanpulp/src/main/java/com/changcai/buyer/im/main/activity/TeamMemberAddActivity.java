package com.changcai.buyer.im.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;

import com.changcai.buyer.CompatTouchBackActivity;
import com.changcai.buyer.R;
import com.changcai.buyer.im.main.present.TeamMemberAddPresent;
import com.changcai.buyer.im.main.present.imp.TeamMemberAddPresentImp;
import com.changcai.buyer.im.main.viewmodel.TeamMemberAddViewModel;
import com.changcai.buyer.rx.RxBus;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.util.ServerErrorCodeDispatch;
import com.changcai.buyer.util.ToastUtil;
import com.changcai.buyer.view.ConfirmDialog;
import com.changcai.buyer.view.RotateDotsProgressView;
import com.netease.nim.uikit.business.session.constant.Extras;

import java.util.HashMap;

import butterknife.BindView;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by lufeisong on 2018/1/15.
 */

public class TeamMemberAddActivity extends CompatTouchBackActivity implements TeamMemberAddViewModel {

    @BindView(R.id.et_search_account)
    SearchView etSearchAccount;
    @BindView(R.id.news_progress)
    RotateDotsProgressView newsProgress;

    private TeamMemberAddPresent present;

    private String teamId = "";
    private Observable<Boolean> addTeamMemberEvent;
    private Observable<Boolean> chatMemberEvent;

    public static void start(Context context, String teamId) {
        Intent intent = new Intent();
        intent.setClass(context, TeamMemberAddActivity.class);
        intent.putExtra(Extras.EXTRA_TEAM_ID, teamId);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }
    @Override
    protected void injectFragmentView() {
        LogUtil.d("NimIM","injectFragmentView");
        addTeamMemberEvent = RxBus.get().register("addTeamMember",Boolean.class);
        addTeamMemberEvent.subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if(aBoolean){
                    TeamMemberAddActivity.this.finish();
                    RxBus.get().post("teamMember",true);
                }
            }
        });
        chatMemberEvent = RxBus.get().register("chatMember",Boolean.class);
        chatMemberEvent.subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if(aBoolean){
                    TeamMemberAddActivity.this.finish();
                }
            }
        });
        teamId = getIntent().getExtras().getString(Extras.EXTRA_TEAM_ID,"");
        if(TextUtils.isEmpty(teamId)){
            titleText.setText(getResources().getString(R.string.search_member));
        }else{
            titleText.setText(getResources().getString(R.string.add_group_member));
        }
        present = new TeamMemberAddPresentImp(this);
        etSearchAccount.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.isEmpty()) {
                    ToastUtil.showLong(TeamMemberAddActivity.this, "请输入需要查询的账号");
                } else {
                    present.fetchUserInfo(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    @SuppressWarnings("deprecation")
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
        return R.string.add_group_member;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_teammember_add;
    }

    @Override
    protected void onPause() {
        //取消焦点，隐藏软键盘
        etSearchAccount.clearFocus();
        super.onPause();
    }

    @Override
    protected void onResume() {
        //展示软键盘需要延迟处理
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                etSearchAccount.setIconified(false);
            }
        },500);
        super.onResume();
    }

    @Override
    protected void onDestroy() {

        present.onDestory();
        RxBus.get().unregister("addTeamMember", addTeamMemberEvent);
        super.onDestroy();
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
    public void showExistAccount(String account) {
        HashMap<String,Object> extmap = new HashMap<>();
        extmap.put(Extras.EXTRA_TEAM_ID,teamId);
        UserProfileActivity.start(this, account,extmap);
    }

    @Override
    public void showUnExistAccount(String account) {
        ConfirmDialog.createConfirmDialog(this,  "没找到该账号用户","提示", "确认" ,new ConfirmDialog.OnBtnConfirmListener() {
            @Override
            public void onConfirmListener() {
            }
        });
    }

    @Override
    public void showErrorStr(String errorStr) {
        ServerErrorCodeDispatch.getInstance().showNetErrorDialog(this, errorStr);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
