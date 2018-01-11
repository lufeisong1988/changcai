package com.changcai.buyer.ui.consultant;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.TextView;

import com.changcai.buyer.BaseCompatActivity;
import com.changcai.buyer.CommonApplication;
import com.changcai.buyer.R;
import com.changcai.buyer.bean.UserInfo;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.ui.consultant.present.ConsultantSettingPresent;
import com.changcai.buyer.ui.consultant.present.imp.ConsultantSettingPresentImp;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.ServerErrorCodeDispatch;
import com.changcai.buyer.util.ToastUtil;
import com.changcai.buyer.view.ActionSheetListDialog;
import com.changcai.buyer.view.RotateDotsProgressView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lufeisong on 2017/12/27.
 * 顾问设置
 */

public class ConsultantSettingActivity extends BaseCompatActivity implements ConsultantSettingViewModel {


    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.cl_servicelevel)
    ConstraintLayout clServicelevel;
    @BindView(R.id.tv_onlinestatus)
    TextView tvOnlinestatus;
    @BindView(R.id.cl_onlinestatus)
    ConstraintLayout clOnlinestatus;
    @BindView(R.id.tv_servicestatus)
    TextView tvServicestatus;
    @BindView(R.id.cl_servicestatus)
    ConstraintLayout clServicestatus;
    @BindView(R.id.progress)
    RotateDotsProgressView progress;
    private ActionSheetListDialog dialog;

    private ConsultantSettingPresent present;

    private boolean toNext = false;
    private UserInfo userInfo;


    ArrayList<String> counselorStatusItems = new ArrayList<>();
    final ArrayList<String> counselorStatusItemsValue = new ArrayList<>();
    ArrayList<String> serviceStatusItems = new ArrayList<>();
    final ArrayList<String> serviceStatusItemsValue = new ArrayList<>();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        present.onDestory();
    }

    @Override
    protected void injectFragmentView() {
        counselorStatusItems.add("上线");
        counselorStatusItems.add("下线");
        counselorStatusItemsValue.add("ONLINE");
        counselorStatusItemsValue.add("DOWNLINE");
        serviceStatusItems.add("正常");
        serviceStatusItems.add("临时退出");
        serviceStatusItemsValue.add("NORMAL");
        serviceStatusItemsValue.add("EXIT");
        updateUI();
        present = new ConsultantSettingPresentImp(this);
        toNext = false;
        present.getUserLevel(toNext);
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
        return R.string.settings;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_consultant_setting;
    }


    @OnClick({R.id.cl_servicelevel, R.id.cl_onlinestatus, R.id.cl_servicestatus})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cl_servicelevel:
                toNext = true;
                present.getUserLevel(toNext);
                break;
            case R.id.cl_onlinestatus:
                userInfo = SPUtil.getObjectFromShare(Constants.KEY_USER_INFO);
                if(userInfo.getServiceLevel().equals( "-1")){
                    ToastUtil.showLong(ConsultantSettingActivity.this,"你没有权限修改！");
                    return;
                }
                dialog = new ActionSheetListDialog(ConsultantSettingActivity.this).builder()
                        .setClickListener(new ActionSheetListDialog.ClickListener() {
                            @Override
                            public void cancleClick() {

                            }

                            @Override
                            public void ensureClick(int currentPosition) {
                                present.updateCounselor(userInfo.getServiceLevel(), counselorStatusItemsValue.get(currentPosition), userInfo.getServiceStatus());
                            }
                        })
                        .setAdapter(counselorStatusItems, userInfo.getCounselorStatus().equals("ONLINE") ? 0 : 1);
                dialog.show();
                break;
            case R.id.cl_servicestatus:
                userInfo = SPUtil.getObjectFromShare(Constants.KEY_USER_INFO);
                if(userInfo.getServiceLevel().equals( "-1")){
                    ToastUtil.showLong(ConsultantSettingActivity.this,"你没有权限修改！");
                    return;
                }
                dialog = new ActionSheetListDialog(ConsultantSettingActivity.this).builder()
                        .setClickListener(new ActionSheetListDialog.ClickListener() {
                            @Override
                            public void cancleClick() {

                            }

                            @Override
                            public void ensureClick(int currentPosition) {
                                present.updateCounselor(userInfo.getServiceLevel(), userInfo.getCounselorStatus(), serviceStatusItemsValue.get(currentPosition));
                            }
                        })
                        .setAdapter(serviceStatusItems, userInfo.getServiceStatus().equals("NORMAL") ? 0 : 1);
                dialog.show();
                break;
        }
    }

    private void updateUI() {
        userInfo = SPUtil.getObjectFromShare(Constants.KEY_USER_INFO);
        if (userInfo.getServiceLevel() == null || userInfo.getServiceLevel().equals("")) {
            tvLevel.setText(R.string.user_setting_choose);
        } else {
            if (CommonApplication.getInstance().getUserLevelBean() != null) {
                int index = CommonApplication.getInstance().getUserLevelBean().getCode().indexOf(userInfo.getServiceLevel());
                tvLevel.setText(index >= 0 ? CommonApplication.getInstance().getUserLevelBean().getName().get(index) : "注册及以上会员");
            }
        }
        if (userInfo.getCounselorStatus() == null || userInfo.getCounselorStatus().equals("")) {
            tvOnlinestatus.setText(R.string.user_setting_choose);
        } else {
            int index = counselorStatusItemsValue.indexOf(userInfo.getCounselorStatus());
            tvOnlinestatus.setText(index >= 0 ? counselorStatusItems.get(index) : "");
        }
        if (userInfo.getServiceStatus() == null || userInfo.getServiceStatus().equals("")) {
            tvServicestatus.setText(R.string.user_setting_choose);
        } else {
            int index = serviceStatusItemsValue.indexOf(userInfo.getServiceStatus());
            tvServicestatus.setText(index >= 0 ? serviceStatusItems.get(index) : "");
        }
    }

    @Override
    public void showLoading() {
        progress.setVisibility(View.VISIBLE);
        progress.showAnimation(true);
    }

    @Override
    public void dismissLoading() {
        progress.setVisibility(View.GONE);
        progress.refreshDone(true);
    }

    @Override
    public void getUserLevelSucceed() {
        updateUI();
        if (toNext) {
            userInfo = SPUtil.getObjectFromShare(Constants.KEY_USER_INFO);
            if(userInfo.getServiceLevel() .equals( "-1")){
                ToastUtil.showLong(ConsultantSettingActivity.this,"你没有权限修改！");
                return;
            }
            dialog = new ActionSheetListDialog(ConsultantSettingActivity.this).builder()
                    .setClickListener(new ActionSheetListDialog.ClickListener() {
                        @Override
                        public void cancleClick() {

                        }

                        @Override
                        public void ensureClick(int currentPosition) {
                            String code = CommonApplication.getInstance().getUserLevelBean().getCode().get(currentPosition);
                            present.updateCounselor(code, userInfo.getCounselorStatus(), userInfo.getServiceStatus());
                        }
                    })
                    .setAdapter(CommonApplication.getInstance().getUserLevelBean().getName(), CommonApplication.getInstance().getUserLevelBean().getCode().indexOf(userInfo.getServiceLevel()));
            dialog.show();

        }
    }

    @Override
    public void getUserLevelFail(String failStr) {
        if (toNext) {
            ServerErrorCodeDispatch.getInstance().showNetErrorDialog(this, failStr);
        }
    }

    @Override
    public void getUserLevelError() {
        if (toNext) {
            ServerErrorCodeDispatch.getInstance().showNetErrorDialog(this, getString(R.string.network_unavailable));
        }
    }

    @Override
    public void updateConsultantSucceed() {
        dialog.dismiss();
        updateUI();
        ToastUtil.showShort(this, "修改成功");
    }

    @Override
    public void updateConsultantFail(String failStr) {
        ServerErrorCodeDispatch.getInstance().showNetErrorDialog(this, failStr);
    }

    @Override
    public void updateConsultantError() {
        ServerErrorCodeDispatch.getInstance().showNetErrorDialog(this, getString(R.string.network_unavailable));
    }
}
