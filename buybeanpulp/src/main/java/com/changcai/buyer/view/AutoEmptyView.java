package com.changcai.buyer.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changcai.buyer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liuxingwei on 2017/8/1.
 */

public class AutoEmptyView extends LinearLayout {
    @BindView(R.id.emptyView)
    ImageView emptyView;
    @BindView(R.id.tv_empty_action)
    TextView tvEmptyAction;

    public AutoEmptyView(Context context) {
        super(context);
        init();
    }

    public AutoEmptyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoEmptyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AutoEmptyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    public void init() {
        if (this.getLayoutParams()==null){
            LinearLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParams.gravity=Gravity.CENTER;
            this.setLayoutParams(layoutParams);

        }
        this.setOrientation(VERTICAL);
        this.setGravity(Gravity.CENTER);
        LayoutInflater.from(getContext()).inflate(R.layout.auto_empty_layout, this, true);
        ButterKnife.bind(this);
    }

    public void setErrorStatus(){
        setEmptyImage(R.drawable.default_img_404);
        setEmptyMessage("加载失败，请点击刷新");
    }

    public void  setEmptyStatus(){
        setEmptyImage(R.drawable.default_img_none);
        setEmptyMessage("抱歉，该栏目暂无内容");
    }

    public void setEmptyImage(@DrawableRes int resource) {
        emptyView.setImageResource(resource);
    }

    public void setEmptyMessage(String emptyMessage) {
        tvEmptyAction.setText(emptyMessage);
    }
}
