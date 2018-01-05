package com.changcai.buyer.business_logic.about_buy_beans.person_introduce;

import android.os.Build;
import android.view.View;

import com.changcai.buyer.CompatTouchBackActivity;
import com.changcai.buyer.R;
import com.changcai.buyer.util.ActivityInjectUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by liuxingwei on 2016/11/22.
 */

public class PersonIntroduceActivity extends CompatTouchBackActivity {

    PersonIntroducePresenter personIntroducePresenter;
    @Override
    protected void injectFragmentView() {
        PersonIntroduceFragment personIntroduceFragment = (PersonIntroduceFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (personIntroduceFragment == null){
            personIntroduceFragment = PersonIntroduceFragment.newInstance();
            ActivityInjectUtils.addFragmentToActivity(getSupportFragmentManager(),personIntroduceFragment,R.id.contentFrame);
        }
        personIntroducePresenter = new PersonIntroducePresenter(personIntroduceFragment);


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
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
        return R.string.input_user_introduction;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_authenticate;
    }
}
