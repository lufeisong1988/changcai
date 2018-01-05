package com.changcai.buyer.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.util.AndroidUtil;
import com.changcai.buyer.util.AppManager;
import com.changcai.buyer.util.LogUtil;
import com.tencent.stat.EasyActivity;
import com.tencent.stat.EasyListActivity;

//import com.changcai.buyer.ui.login.LoginActivity;

/**
 * @author zhoujun
 * @version 1.0
 * @description
 * @date 2014年8月4日 下午9:40:21
 */
public class BaseFragmentActivity extends FragmentActivity {
    protected TextView btnLeft, tvTitle, btnRight;
    protected ImageView iv_btn_right, btnBack;
    protected View titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ((CommonApplication) getApplication()).addActivity(this);
        AppManager.getAppManager().addActivity(this);
    }

    /**
     * 显示返回键
     */
    protected void showBackButton() {
        if (btnBack != null) btnBack.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化title
     */
    protected void initTitle() {
        titleView = findViewById(R.id.viewTop);
        btnLeft = (TextView) findViewById(R.id.btnLeft);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        btnRight = (TextView) findViewById(R.id.btnRight);
        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().popBackStack();
            }
        });
        iv_btn_right = (ImageView) findViewById(R.id.iv_btn_right);
        tvTitle.setText("买豆粕资讯");
    }

    protected void gotoActivity(Class<? extends Activity> clazz) {
        gotoActivity(clazz, false);
    }

    protected void gotoActivity(Class<? extends Activity> clazz, Bundle bundle) {
        gotoActivity(clazz, false, bundle);
    }


    public void gotoActivity(Class<? extends Activity> clazz, boolean finish) {
        Intent intent = new Intent(this, clazz);
        this.startActivity(intent);
        if (finish) {
            this.finish();
        }
    }

    public void gotoActivity(Class<? extends Activity> clazz, boolean finish, Bundle pBundle) {
        Intent intent = new Intent(this, clazz);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }

        this.startActivity(intent);
        if (finish) {
            this.finish();
        }
    }

    protected void showNext(Fragment fragment, int id) {
        showNext(fragment, id, null, true);
    }

    protected void showNext(Fragment fragment, int id, Bundle b) {
        showNext(fragment, id, b, true);
    }

    protected void showNext(Fragment fragment, int id, Bundle b, boolean isAddBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(id, fragment);
        if (b != null) {
            fragment.setArguments(b);
        }
        if (isAddBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public View getTitleView() {

        return titleView;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
}
