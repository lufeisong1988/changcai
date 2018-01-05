package com.changcai.buyer.business_logic.about_buy_beans.company_authenticate;

import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;

import com.changcai.buyer.BaseCompatActivity;
import com.changcai.buyer.R;
import com.changcai.buyer.permission.RuntimePermission;
import com.changcai.buyer.rx.BackEvent;
import com.changcai.buyer.util.ActivityInjectUtils;
import com.changcai.buyer.util.LogUtil;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.List;

/**
 * Created by liuxingwei on 2016/11/26.
 */

public class CompanyAuthenticateActivity extends BaseCompatActivity implements RuntimePermission.PermissionCallbacks{
    CompanyAuthenticatePresenter companyAuthenticatePresenter;

    @Override
    protected void injectFragmentView() {
        CompanyAuthenticateFragment companyAuthenticateFragment = (CompanyAuthenticateFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (null == companyAuthenticateFragment) {
            companyAuthenticateFragment = CompanyAuthenticateFragment.getInstance();
            ActivityInjectUtils.addFragmentToActivity(getSupportFragmentManager(), companyAuthenticateFragment, R.id.contentFrame);
        }
        companyAuthenticatePresenter = new CompanyAuthenticatePresenter(companyAuthenticateFragment);


    }


    @Override
    protected void onStart() {
        super.onStart();
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackEvent.publish(true);
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
        return R.string.company_authenticate;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_authenticate;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        RuntimePermission.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    @Override
    public void onPermissionsGranted(List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(List<String> perms) {

    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        LogUtil.d("com.changcai.buyer","low memory");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d("com.changcai.buyer","destroy by system");
    }
}
