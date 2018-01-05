package com.changcai.buyer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import butterknife.Unbinder;

/**
 * Created by liuxingwei on 2016/11/22.
 */

public class BaseFragment extends Fragment {

    protected Activity activity;
    protected Unbinder baseUnbinder;


    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    protected void gotoActivity(Class<? extends Activity> clazz) {
        gotoActivity(clazz, false, null);
    }

    protected void gotoActivity(Class<? extends Activity> clazz, Bundle bundle) {
        gotoActivity(clazz, false, bundle);
    }

    protected void gotoActivity(Class<? extends Activity> clazz, boolean finish, Bundle pBundle) {
        if (!isAdded()) return;
        Intent intent = new Intent(getContext(), clazz);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }

        startActivity(intent);
        if (finish) {
            activity.finish();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (baseUnbinder != null) {
            baseUnbinder.unbind();
        }
        activity = null;
    }


}
