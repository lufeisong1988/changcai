package com.changcai.buyer.business_logic.about_buy_beans.person_authenticate;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.changcai.buyer.CompatTouchBackActivity;
import com.changcai.buyer.R;
import com.changcai.buyer.interface_api.MyService;
import com.changcai.buyer.permission.RuntimePermission;
import com.changcai.buyer.rx.BackEvent;
import com.changcai.buyer.util.ActivityInjectUtils;
import com.changcai.buyer.util.LogUtil;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.List;

/**
 * Created by liuxingwei on 2016/11/22.
 */

public class PersonAuthenticateActivity extends CompatTouchBackActivity implements RuntimePermission.PermissionCallbacks {

    PersonAuthenticatePresenter personAuthenticatePresenter;
    PersonAuthenticateFragment personAuthenticateFragment;
    private Intent serviceIntent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(PersonAuthenticateActivity.class.getSimpleName(),"onCreate");
        if (savedInstanceState != null) {
            LogUtil.d(PersonAuthenticateActivity.class.getSimpleName(),"savedInstanceState not null");
        }



    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.d(PersonAuthenticateActivity.class.getSimpleName(),"onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.d(PersonAuthenticateActivity.class.getSimpleName(),"onStart");
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackEvent.publish(true);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.d(PersonAuthenticateActivity.class.getSimpleName(),"onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.d(PersonAuthenticateActivity.class.getSimpleName(),"onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d(PersonAuthenticateActivity.class.getSimpleName(),"onDestroy");
        stopService(serviceIntent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("volue", " has been kill by system" );
        LogUtil.d(PersonAuthenticateActivity.class.getSimpleName(),"onSaveInstanceState");
    }

    @Override
    protected void injectFragmentView() {
        personAuthenticateFragment  = (PersonAuthenticateFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (null == personAuthenticateFragment) {
            personAuthenticateFragment = PersonAuthenticateFragment.newInstance();
            ActivityInjectUtils.addFragmentToActivity(getSupportFragmentManager(), personAuthenticateFragment, R.id.contentFrame);
        }
        personAuthenticatePresenter = new PersonAuthenticatePresenter(personAuthenticateFragment);
        serviceIntent = new Intent(PersonAuthenticateActivity.this, MyService.class);
        startService(serviceIntent);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        LogUtil.d(PersonAuthenticateActivity.class.getSimpleName(),"onLowMemory");
    }
    @Override
    protected void exitActivity() {
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
        return R.string.person_certify;
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
        personAuthenticateFragment.intentToGallery();
    }

    @Override
    public void onPermissionsDenied(List<String> perms) {
        Toast.makeText(this,R.string.perssion_for_set,Toast.LENGTH_LONG).show();
    }



}
