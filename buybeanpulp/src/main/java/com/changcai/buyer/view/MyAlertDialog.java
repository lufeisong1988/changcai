package com.changcai.buyer.view;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.changcai.buyer.R;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by liuxingwei on 2017/6/12.
 */

public class MyAlertDialog extends BaseBottomDialog {


    @BindView(R.id.iv_alert_icon)
    ImageView ivAlertIcon;
    @BindView(R.id.tv_alert_title)
    TextView tvAlertTitle;
    Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Class clasz = Class.forName("com.android.internal.R$style");
            Field field = clasz.getDeclaredField("Animation_Toast");
            field.setAccessible(true);
//            textAppearanceStyleArr = (int[])field.get(null);
            setStyle(DialogFragment.STYLE_NO_TITLE,
                    (Integer) field.get(null));
//
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        params.dimAmount = getDimAmount();
        //此处不要用view的宽高去决定参数，因为view的生命周期方法和fragment什么周期方法不同步 view.width或者view.getmesureHeight都为0
        params.width = getResources().getDimensionPixelOffset(R.dimen.dim560);
        params.height = getResources().getDimensionPixelOffset(R.dimen.dim144);
        params.gravity = Gravity.CENTER;

        window.setAttributes(params);
    }

    @Override
    public void onResume() {
        super.onResume();
        Observable.empty().delay(2000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {
                if(isAdded() && isVisible()){
                    dismiss();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.my_alert_dialog;
    }

    @Override
    public void bindView(View v) {
        unbinder = ButterKnife.bind(this, v);
        if (getArguments().containsKey("title")) {
            tvAlertTitle.setText(getArguments().getString("title"));
        }

        if (getArguments().containsKey("icon")) {
            ivAlertIcon.setImageResource(getArguments().getInt("icon",0));
        }

    }


    @Override
    public float getDimAmount() {
        return 0;
    }

}
