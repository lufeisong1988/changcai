package com.changcai.buyer.business_logic.about_buy_beans.person_authenticate.big_photo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.changcai.buyer.CompatTouchBackActivity;
import com.changcai.buyer.R;
import com.changcai.buyer.util.ActivityInjectUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by liuxingwei on 2016/11/29.
 */

public class BigPhotoActivity extends CompatTouchBackActivity implements View.OnClickListener {

    BigPhotoPresenter bigPhotoPresenter;
    TextView btnRight;


    private int title;
    private String path;
    Bundle bundle;
    private SystemBarTintManager tintManager;

    @SuppressWarnings("deprecation")
    @Override
    protected void injectFragmentView() {
//        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) toolbar.getLayoutParams();
//        layoutParams.(layoutParams.leftMargin, 100, layoutParams.rightMargin, layoutParams.bottomMargin);
//        toolbar.setPadding(0,20,0,0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintColor(getResources().getColor(R.color.black));
            tintManager.setStatusBarTintEnabled(true);
        }
        toolbar.setPadding(0, 10, 0, 0);
        btnRight = (TextView) findViewById(R.id.btnRight);
        btnRight.setText(R.string.reupload);
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setTextColor(getResources().getColor(R.color.global_blue));
        btnRight.setOnClickListener(this);
        BigPhotoFragment bigPhotoFragment = (BigPhotoFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (null == bigPhotoFragment) {
            bigPhotoFragment = BigPhotoFragment.getInstance();
            ActivityInjectUtils.addFragmentToActivity(getSupportFragmentManager(), bigPhotoFragment, R.id.contentFrame);
        }
        bigPhotoFragment.setArguments(bundle);
        bigPhotoPresenter = new BigPhotoPresenter(bigPhotoFragment);


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
    @SuppressWarnings("deprecation")
    @Override
    protected int getTitleTextColor() {
        return getResources().getColor(R.color.white);
    }

    @Override
    protected int getNavigationVisible() {
        return View.VISIBLE;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected int getToolBarBackgroundColor() {
        return getResources().getColor(R.color.black);
    }

    @Override
    protected int getNavigationIcon() {
        return R.drawable.icon_nav_back;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected int getTitleText() {
        Intent intent = getIntent();
        bundle = intent.getExtras();
        title = bundle.getInt("title");
        if (title == 1) {
            return R.string.legal_id_front;
        } else if (title == 2) {
            return R.string.legal_id_background;
        } else if (title == 3) {
            return R.string.handle_id_card_front_picture;
        } else if (title == 4) {
            return R.string.license;
        } else if (title == 5) {
            return R.string.institutional_framework_code;
        } else if (title == 6) {
            return R.string.tax_license;
        }
        return R.string.app_name;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_authenticate;
    }


    public void onClick() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnRight:
                setResult(RESULT_OK);
                finish();
                break;
        }
    }
}
