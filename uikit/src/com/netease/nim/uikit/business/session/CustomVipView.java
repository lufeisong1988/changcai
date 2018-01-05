package com.netease.nim.uikit.business.session;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.nim.uikit.R;

/**
 * Created by lufeisong on 2017/12/20.
 */

public class CustomVipView extends LinearLayout {
    private Context context;
    private ImageView iv_vg,iv_header,iv_guard,iv_dot;
    private TextView tv_name;
    public CustomVipView(Context context) {
        super(context);
        init(context);
    }

    public CustomVipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomVipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }
    void init(Context context){
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.view_vip_header,this);
        iv_vg = view.findViewById(R.id.iv_bg);
        iv_header = view.findViewById(R.id.iv_header);
        iv_guard = view.findViewById(R.id.iv_guard);
        iv_dot = view.findViewById(R.id.iv_dot);
        tv_name = view.findViewById(R.id.tv_name);
    }
    public void setGuard(int resId){
        iv_guard.setImageResource(resId);
    }
    public void showDot(){
        iv_dot.setVisibility(VISIBLE);
    }
    public void hideDot(){
        iv_dot.setVisibility(GONE);
    }
    public void setName(String name){
        tv_name.setText(name);
    }
    public void click(){
        iv_vg.setBackgroundResource(R.drawable.custom_online_bg);
    }
    public void unClick(){
        iv_vg.setBackgroundResource(R.drawable.custom_online_bg);
    }
}
