package com.changcai.buyer.ui.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.changcai.buyer.R;
import com.changcai.buyer.util.AndroidUtil;
import com.changcai.buyer.util.AppManager;
import com.changcai.buyer.util.FileUtil;
import com.changcai.buyer.util.StatusBarUtil;
import com.changcai.buyer.view.immersion.ImmersionBar;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * @author zhoujun
 * @version 1.0
 * @description
 * @date 2014年7月29日 上午10:02:39
 */
public class BaseActivity extends FragmentActivity {
    protected TextView btnLeft, btnRight;
    protected ImageView btnBack, iv_btn_right;
    protected TextView tvTitle;
    protected View titleView;
    protected ImmersionBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ((CommonApplication) getApplication()).addActivity(this);
        AppManager.getAppManager().addActivity(this);
//        StatusBarUtil.StatusBarLightMode(this);
        initSystemStatusBar();
    }

    protected void initSystemStatusBar() {
        bar = ImmersionBar.with(this);
        bar.navigationBarWithKitkatEnable(false).init();
    }


    protected void gotoActivity(Class<? extends Activity> clazz) {
        gotoActivity(clazz, false);
    }

    protected void gotoActivity(Class<? extends Activity> clazz, Bundle bundle) {
        gotoActivity(clazz, false, bundle);
    }

    /**
     * 初始化title
     */
    protected void initTitle() {
        titleView = findViewById(R.id.viewTop);
        btnLeft = (TextView) findViewById(R.id.btnLeft);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        btnRight = (TextView) findViewById(R.id.btnRight);
        btnRight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showShareDialog(v);
            }
        });
        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_btn_right = (ImageView) findViewById(R.id.iv_btn_right);

    }

    /**
     * 显示返回键
     */
    protected void showBackButton() {
        if (btnBack != null) btnBack.setVisibility(View.VISIBLE);
    }

    /**
     * 显示分享对话框
     */
    protected void showShareDialog(View v) {
        boolean isSaveScreenSuccess = FileUtil.savePic(AndroidUtil.takeScreenShot(this));
        if (!isSaveScreenSuccess) {
            Toast.makeText(this, "截屏失败", Toast.LENGTH_SHORT).show();
            return;
        }
//        final BelowView blv = new BelowView(this, R.layout.dialog_share);
//        blv.showBelowView(v, true, 10, 0);
//        View view = blv.getBelowView();
//        TextView tvWeiboShare = (TextView) view.findViewById(R.id.tvWeiboShare);
//        tvWeiboShare.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                blv.dismissBelowView();
//                gotoActivity(SnsShareActivity.class);
//            }
//        });
//        TextView tvWeixinShare = (TextView) view.findViewById(R.id.tvWeixinShare);
//        tvWeixinShare.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                blv.dismissBelowView();
//                gotoActivity(ShareToWeiXinActivity.class);
//            }
//        });
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


    public void hideInputKeyBoard(View view) {
        if (view != null) {
            InputMethodManager mImm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            mImm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected void showShortToast(String pMsg) {
        if (this == null || this.isFinishing()) {
            return;
        }
        if (pMsg != null)
            Toast.makeText(this, pMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
}
