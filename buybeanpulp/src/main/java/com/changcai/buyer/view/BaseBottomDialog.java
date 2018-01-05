package com.changcai.buyer.view;

/**
 * Created by shaohui on 16/10/11.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.changcai.buyer.R;

/**
 * Created by shaohui on 16/10/11.
 */

public abstract class BaseBottomDialog extends DialogFragment {

    private static final String TAG = "base_bottom_dialog";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BottomDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(getCancelOutside());

        View v = inflater.inflate(getLayoutRes(), container, false);
        bindView(v);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        params.dimAmount = getDimAmount();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
//        if (getHeight() > 0) {
//            params.height = getHeight();
//        } else {
//            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        }
        params.gravity = Gravity.TOP;

        window.setAttributes(params);
    }

    @LayoutRes
    public abstract int getLayoutRes();

    public abstract void bindView(View v);

    public int getHeight() {
        return -1;
    }

    /**
     * 获取透明度
     * @return
     */
    public abstract float getDimAmount();

    public boolean getCancelOutside() {
        return true;
    }

    public String getFragmentTag() {
        return TAG;
    }

    public void show(FragmentManager fragmentManager) {
        try{
            show(fragmentManager, getFragmentTag());
        }catch (Exception e){
        }
//        fragmentManager.beginTransaction().commitAllowingStateLoss();
    }


    protected void gotoActivity(Class<? extends Activity> clazz) {
        gotoActivity(clazz, false, null);
    }

    protected void gotoActivity(Class<? extends Activity> clazz, Bundle bundle) {
        gotoActivity(clazz, false, bundle);
    }

    protected void gotoActivity(Class<? extends Activity> clazz, boolean finish, Bundle pBundle) {
        Intent intent = new Intent(getActivity(), clazz);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }

        startActivity(intent);
        if (finish) {
            getActivity().finish();
        }

    }
}
