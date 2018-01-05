package com.changcai.buyer.business_logic.about_buy_beans.recharge;

import android.Manifest;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.changcai.buyer.BaseCompatActivity;
import com.changcai.buyer.R;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.permission.RuntimePermission;
import com.changcai.buyer.util.ActivityInjectUtils;
import com.changcai.buyer.util.SPUtil;

import java.util.List;

/**
 * Created by liuxingwei on 2017/1/5.
 */

public class RechargeActivity extends BaseCompatActivity implements RuntimePermission.PermissionCallbacks {
    RechargePresenter rechargePresenter;
    ImageView imageViewContactService;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void injectFragmentView() {
        setContactIconShow();
        RechargeFragment rechargeFragment = (RechargeFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (rechargeFragment == null){
            rechargeFragment = new RechargeFragment();
            ActivityInjectUtils.addFragmentToActivity(getSupportFragmentManager(),rechargeFragment,R.id.contentFrame);
        }
        rechargePresenter = new RechargePresenter(rechargeFragment);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected int getTitleTextColor() {
        return getResources().getColor(R.color.sticky_header_text_color);
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
        return R.string.recharge_title;
    }

    @Override
    protected int getLayoutId() {
            return R.layout.activity_main_authenticate;
    }

    private void setContactIconShow(){
        imageViewContactService = (ImageView) findViewById(R.id.iv_service_phone);
        imageViewContactService.setVisibility(View.VISIBLE);
        imageViewContactService.setImageResource(R.drawable.icon_hotline_blue);
        imageViewContactService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RuntimePermission.hasPermissions(RechargeActivity.this, Manifest.permission.CALL_PHONE)) {
                    showPhoneChooseDialog(SPUtil.getString(Constants.KEY_CONTACT_PHONE));
                } else {
                    RuntimePermission.requestPermissions(RechargeActivity.this, getString(R.string.permissions_tips_call),
                            Constants.REQUEST_PERMISSION_CALL_PHONE, Manifest.permission.CALL_PHONE);
                }
            }
        });
    }

    /**
     * call phone runtime permission ok
     * @param perms
     */
    @Override
    public void onPermissionsGranted(List<String> perms) {
        showPhoneChooseDialog(SPUtil.getString(Constants.KEY_CONTACT_PHONE));
    }

    /**
     * call runtime permission refuse
     * @param perms
     */
    @Override
    public void onPermissionsDenied(List<String> perms) {
        Toast.makeText(this, R.string.perssion_for_call, Toast.LENGTH_LONG).show();
    }
}
