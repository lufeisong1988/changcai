package com.changcai.buyer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;

import butterknife.Unbinder;

/**
 * Created by liuxingwei on 2017/4/6.
 */

public class BaseListFragment extends ListFragment {

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
        if (!isAdded())return;
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
        activity = null;
        if (baseUnbinder!=null)baseUnbinder.unbind();
    }

}
