package com.changcai.buyer.im.main.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.GetCounselorsModel;
import com.changcai.buyer.im.DemoCache;
import com.changcai.buyer.im.session.SessionHelper;
import com.changcai.buyer.ui.base.BaseActivity;
import com.changcai.buyer.util.PicassoImageLoader;
import com.changcai.buyer.util.ServerErrorCodeDispatch;
import com.changcai.buyer.util.ToastUtil;
import com.changcai.buyer.view.RotateDotsProgressView;
import com.changcai.buyer.view.RoundImageView;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.SimpleCallback;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lufeisong on 2017/12/25.
 */

public class UserProfileActivity extends BaseActivity {


    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.iv_user_header)
    RoundImageView ivUserHeader;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_detail)
    TextView tvUserDetail;
    @BindView(R.id.tv_setting)
    TextView tvSetting;
    @BindView(R.id.iv_grade)
    ImageView ivGrade;
    @BindView(R.id.btn_addFriend)
    Button btnAddFriend;
    @BindView(R.id.news_progress)
    RotateDotsProgressView newsProgress;

    private Drawable defaultDrawable, defaultGradeDrawable;
    private String account;
    private NimUserInfo nimUserInfo;
    private HashMap<String, Object> extMap;
    private String teamId = "";

    public static void start(Context context, String account, HashMap<String, Object> map) {
        Intent intent = new Intent();
        intent.setClass(context, UserProfileActivity.class);
        intent.putExtra(Extras.EXTRA_ACCOUNT, account);
        if(map == null){
            map = new HashMap<>();
        }
        intent.putExtra(Extras.EXTRA_EXT, map);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);

        initListener();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUserProfile();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    void initListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfileActivity.this.finish();
            }
        });
        tvSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfileSettingActivity.start(UserProfileActivity.this, account);
            }
        });
    }

    void initData() {

        defaultDrawable = this.getResources().getDrawable(R.drawable.icon_default_head);
        defaultGradeDrawable = ContextCompat.getDrawable(this, R.mipmap.no_network_2);
        parseIntent();
        if (!DemoCache.getAccount().equals(account)) {
            tvSetting.setVisibility(View.GONE);
        } else {
            tvSetting.setVisibility(View.VISIBLE);
        }
    }

    void parseIntent() {
        account = getIntent().getStringExtra(Extras.EXTRA_ACCOUNT);
        extMap = (HashMap<String, Object>) getIntent().getSerializableExtra(Extras.EXTRA_EXT);
        if (extMap != null && extMap.containsKey(Extras.EXTRA_TEAM_ID)) {//存在teamid，进入拉群的逻辑
            teamId = (String) extMap.get(Extras.EXTRA_TEAM_ID);
            btnAddFriend.setVisibility(View.VISIBLE);
        } else {
            btnAddFriend.setVisibility(View.GONE);
        }
    }

    void updateUserProfile() {
        nimUserInfo = (NimUserInfo) NimUIKit.getUserInfoProvider().getUserInfo(account);
        if (nimUserInfo == null) {
            NimUIKit.getUserInfoProvider().getUserInfoAsync(account, new SimpleCallback<NimUserInfo>() {

                @Override
                public void onResult(boolean success, NimUserInfo result, int code) {
                    nimUserInfo = result;
                    updateUI();
                }
            });
        } else {
            updateUI();
        }
    }

    void updateUI() {
        if (nimUserInfo.getAvatar() != null && !nimUserInfo.getAvatar().equals("")) {
            PicassoImageLoader.getInstance().displayNetImage(UserProfileActivity.this, nimUserInfo.getAvatar(), ivUserHeader, defaultDrawable);
        }
        if (nimUserInfo.getName() != null && !nimUserInfo.getName().equals("")) {
            tvUserName.setText(nimUserInfo.getName());
        } else {
            if (nimUserInfo.getMobile() != null && !nimUserInfo.getMobile().equals("")) {
                tvUserName.setText(nimUserInfo.getMobile());
            } else {
                if (nimUserInfo.getAccount() != null && !nimUserInfo.getAccount().equals("")) {
                    tvUserName.setText(nimUserInfo.getAccount());
                }
            }
        }
        if (nimUserInfo.getSignature() != null && !nimUserInfo.getSignature().equals("")) {
            tvUserDetail.setText(nimUserInfo.getSignature().replace("\\n", "\n"));
            tvUserDetail.setTextColor(getResources().getColor(R.color.font_black));
        } else {
            tvUserDetail.setText("暂无更多信息");
            tvUserDetail.setTextColor(getResources().getColor(R.color.font_gray));
        }

        boolean showGrade = true;
        for (GetCounselorsModel.InfoBean infoBean : SessionHelper.getInfo()) {
            if (infoBean.getAccid().equals(account)) {
                showGrade = false;
                break;
            }
        }
        if (showGrade) {
            ivGrade.setVisibility(View.VISIBLE);
            String grade = UserInfoHelper.getUserExtLevel(account);
            LogUtil.d("level", "level = " + grade);
            switch (grade) {
                case "-1":
                    ivGrade.setVisibility(View.INVISIBLE);
                    break;
                case "0":
                    PicassoImageLoader.getInstance().displayResourceImageNoResize(this, R.drawable.grade_qt, ivGrade);
                    break;
                case "100":
                    PicassoImageLoader.getInstance().displayResourceImageNoResize(this, R.drawable.grade_by, ivGrade);
                    break;
                case "150":
                    PicassoImageLoader.getInstance().displayResourceImageNoResize(this, R.drawable.grade_by_plus, ivGrade);
                    break;
                case "200":
                    PicassoImageLoader.getInstance().displayResourceImageNoResize(this, R.drawable.grade_hj, ivGrade);
                    break;
                case "300":
                    PicassoImageLoader.getInstance().displayResourceImageNoResize(this, R.drawable.grade_zs, ivGrade);
                    break;
                case "400":
                    PicassoImageLoader.getInstance().displayResourceImageNoResize(this, R.drawable.grade_vip, ivGrade);
                    break;
                default:
                    ivGrade.setVisibility(View.INVISIBLE);
                    break;
            }
        } else {
            ivGrade.setVisibility(View.INVISIBLE);
        }


    }

    public void showLoading() {
        newsProgress.setVisibility(View.VISIBLE);
        newsProgress.showAnimation(true);
    }


    public void dismissLoading() {
        newsProgress.setVisibility(View.GONE);
        newsProgress.refreshDone(true);
    }

    @OnClick(R.id.btn_addFriend)
    public void onClick() {
        showLoading();
        List<String> members = new ArrayList<>();
        members.add(account);
        com.changcai.buyer.util.LogUtil.d("NimIM","teamId = " + teamId);
        NIMClient.getService(TeamService.class)
                .addMembers(teamId, members)
                .setCallback(new RequestCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> strings) {
                com.changcai.buyer.util.LogUtil.d("NimIM","list.String = " + strings.toString());
                if (!isFinishing()) {
                    dismissLoading();
                    ToastUtil.showLong(UserProfileActivity.this, "添加账号成功");
                    UserProfileActivity.this.finish();
                }
            }

            @Override
            public void onFailed(int i) {
                com.changcai.buyer.util.LogUtil.d("NimIM","onFailed = " + i);
                if (!isFinishing()) {
                    dismissLoading();
                    ServerErrorCodeDispatch.getInstance().showNetErrorDialog(UserProfileActivity.this, "添加用户失败！请重试");
                }
            }

            @Override
            public void onException(Throwable throwable) {
                com.changcai.buyer.util.LogUtil.d("NimIM","onException = " + throwable.toString());
                if (!isFinishing()) {
                    dismissLoading();
                    ServerErrorCodeDispatch.getInstance().showNetErrorDialog(UserProfileActivity.this, "添加用户失败！请重试");
                }
            }
        });
    }
}
