package com.changcai.buyer.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.GetCounselorsModel;
import com.changcai.buyer.util.PicassoImageLoader;

/**
 * Created by lufeisong on 2017/12/21.
 */

public class CustomVipView extends LinearLayout {
    private Context context;
    private GetCounselorsModel.InfoBean infoBean;
    private ImageView iv_vg,iv_guard,iv_dot;
    private RoundImageView iv_header;
    private TextView tv_name;
    private Drawable defaultDrawable;


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
        defaultDrawable = context.getResources().getDrawable(R.drawable.icon_default_head);
        View view = LayoutInflater.from(context).inflate(R.layout.view_vip_header,this);
        iv_vg = view.findViewById(R.id.iv_bg);
        iv_header = view.findViewById(R.id.iv_header);
        iv_guard = view.findViewById(R.id.iv_guard);
        iv_dot = view.findViewById(R.id.iv_dot);
        tv_name = view.findViewById(R.id.tv_name);


    }
    public void setGuard(int resId){
        iv_guard.setVisibility(VISIBLE);
        try {
            PicassoImageLoader.getInstance().displayResourceImageNoResize((Activity)(context),resId,iv_guard);
        }catch (Exception e){

        }
    }
    public void hideGurad(){
        iv_guard.setVisibility(INVISIBLE);
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
        iv_vg.setBackgroundResource(R.drawable.custom_select_bg);
    }
    public void unClick(){
        if(infoBean.getCounselorStatus().equals("ONLINE")){
            iv_vg.setBackgroundResource(R.drawable.custom_online_bg);

        }else{
            iv_vg.setBackgroundResource(R.drawable.custom_offline_bg);

        }
    }

    public void setHeader(String picUrl){

        try {

            PicassoImageLoader.getInstance().displayNetImage((Activity)(context),picUrl,iv_header,defaultDrawable);
        }catch (Exception e){

        }
    }

    public GetCounselorsModel.InfoBean getInfoBean() {
        return infoBean;
    }

    public void setInfoBean(GetCounselorsModel.InfoBean infoBean) {
        this.infoBean = infoBean;
        if(infoBean.getCounselorStatus().equals("ONLINE")){
            iv_header.setColorFilter(null);
        }else{
            iv_header.setColorFilter(setColorMatrix(0));
        }
        unClick();
    }
    private ColorMatrixColorFilter setColorMatrix(float saturation){
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(saturation);//饱和度 0灰色 100过度彩色，50正常
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        return filter;
    }
}
