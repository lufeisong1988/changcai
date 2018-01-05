package com.changcai.buyer.ui.news;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;

import com.changcai.buyer.BaseCompatActivity;
import com.changcai.buyer.R;
import com.changcai.buyer.ui.base.BaseActivity;
import com.changcai.buyer.ui.base.BaseFragmentActivity;
import com.changcai.buyer.ui.base.BaseTouchBackActivity;
import com.changcai.buyer.ui.calendar.CalendarFragment;
import com.changcai.buyer.util.ActivityInjectUtils;

/**
 * Created by liuxingwei on 2017/7/31.
 */

public class CalendarActivity extends BaseCompatActivity {


    CalendarFragment calendarFragment;

    @Override
    public void onBackPressed() {
        if (calendarFragment.getWvCalendar().canGoBack()){
            calendarFragment.getWvCalendar().goBack();
            return;
        }
        finish();
    }

    @Override
    protected void injectFragmentView() {
        if (calendarFragment == null) {
            calendarFragment = new CalendarFragment();
            ActivityInjectUtils.addFragmentToActivity(getSupportFragmentManager(), calendarFragment, R.id.contentFrame);
        }
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
        return R.string.profession_calendar;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_authenticate;
    }
}
