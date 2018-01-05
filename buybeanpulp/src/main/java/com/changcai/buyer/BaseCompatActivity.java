package com.changcai.buyer;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.changcai.buyer.rx.BackEvent;
import com.changcai.buyer.util.AppManager;
import com.changcai.buyer.view.immersion.ImmersionBar;

import butterknife.ButterKnife;

/**
 * Created by liuxingwei on 2016/11/17.
 */

public abstract class BaseCompatActivity extends AppCompatActivity  {


    protected View toolbar;
    /*
     *center title in toolbar
     */
    protected TextView titleText;
    /*
     *back image button
     */
    protected ImageView backImage;

    protected TextView tv_right;
    protected ImmersionBar immersionBar;
    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
//        StatusBarUtil.StatusBarLightMode(this);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initToolbar();
        initSystemStatusBar();
        injectFragmentView();

    }

    protected void initSystemStatusBar(){
        immersionBar = ImmersionBar.with(this);
        immersionBar.navigationBarWithKitkatEnable(false).init();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * inject view
     */
    protected abstract void injectFragmentView();

    /**
     * init tool view
     */
    @SuppressWarnings("WrongConstant")
    protected void initToolbar() {
        toolbar =  findViewById(R.id.titleView);
        toolbar.setBackgroundColor(getToolBarBackgroundColor());
        titleText = (TextView) findViewById(R.id.tvTitle);
        titleText.setText(getTitleText());
        titleText.setTextColor(getTitleTextColor());
        backImage = (ImageView) findViewById(R.id.btnBack);
        backImage.setImageResource(getNavigationIcon());
        backImage.setVisibility(getNavigationVisible());
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_right = (TextView) findViewById(R.id.btnRight);
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolBarRightBtnListener();
            }
        });
    }
    public void toolBarRightBtnListener(){


    }
    protected void setLineShow(){
        findViewById(R.id.view_bottom_line).setVisibility(View.VISIBLE);
    }

    /**
     * color for center title text
     * @return
     */
    protected abstract int getTitleTextColor();

    /**
     * View VISIBLE ENUM
     * @return
     */
    protected abstract int getNavigationVisible();

    /**
     * return tool bar color
     * @return
     */
    protected abstract int getToolBarBackgroundColor();

    /**
     * navigationIcon
     * @return
     */
    protected abstract int getNavigationIcon();

    /**
     * return center title text s
     * @return
     */
    protected abstract int getTitleText();

    /**
     * return main layout file id
     * @return
     */
    protected abstract int getLayoutId();


    @Override
    public void onBackPressed() {
        BackEvent.publish(true);
    }

    /**
     * 显示拨打电话的dialog
     */
    protected void showPhoneChooseDialog(final String phone) {
        View view = getLayoutInflater().inflate(R.layout.layout_quoto_phone, null);
        final Dialog dialog = new Dialog(this, R.style.whiteFrameWindowStyle);
        dialog.setContentView(view);

        TextView tvPhone = (TextView) view.findViewById(R.id.phone);
        tvPhone.setText(getString(R.string.service_phone) + phone);
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
//                if (ActivityCompat.checkSelfPermission(QuoteDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });

        TextView tvCancel = (TextView) view.findViewById(R.id.cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.choosed_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        wl.gravity = Gravity.BOTTOM;
        dialog.onWindowAttributesChanged(wl);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
    protected void showKeyboard(boolean isShow) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShow) {
            if (getCurrentFocus() == null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            } else {
                imm.showSoftInput(getCurrentFocus(), 0);
            }
        } else {
            if (getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
