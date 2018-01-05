package com.changcai.buyer.view;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.GetQuotePriceBean;
import com.changcai.buyer.util.DateUtil;
import com.changcai.buyer.util.DensityConst;
import com.changcai.buyer.util.LogUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lufeisong on 2017/11/16.
 */

public class CustomNewIndexPriceView extends LinearLayout{
    private Context context;
    private TextView tv_companyName,tv_price,tv_percent,tv_state,tv_time,tv_location;
    private ImageView iv_arrow;
    private ViewPager vp_price;
    private View root_view;

    private PriceAdapter priceAdapter;
    private List<View> priceViews = new ArrayList<>();
//    private List<GetQuotePriceBean.QuotePriceBean.ResultBean> resultBeens = new ArrayList<>();
    public CustomNewIndexPriceView(Context context) {
        super(context);
        init(context);
    }

    public CustomNewIndexPriceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomNewIndexPriceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    void init(Context context){
        DensityConst.initDensity((Activity) context);
        this.context = context;
//        root_view = LayoutInflater.from(context).inflate(R.layout.view_custom_new_index_price,this);
        View view = LayoutInflater.from(context).inflate(R.layout.view_custom_new_index_price,this);
        vp_price = (ViewPager) view.findViewById(R.id.vp_price);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void updatePrice(List<GetQuotePriceBean.QuotePriceBean.ResultBean> resultBeen) {

//        vp_price = (ViewPager) root_view.findViewById(R.id.vp_price);
//        this.resultBeens.clear();
        priceViews.clear();
//        this.resultBeens.addAll(resultBeen);
        for (int i = 0; i < resultBeen.size(); i++) {
            GetQuotePriceBean.QuotePriceBean.ResultBean resultBean = resultBeen.get(i);
            View view = LayoutInflater.from(context).inflate(R.layout.view_custom_new_index_price_item, null);
            RelativeLayout ll_price = (RelativeLayout) view.findViewById(R.id.ll_price);
            tv_companyName = (TextView) view.findViewById(R.id.tv_companyName);
            iv_arrow = (ImageView) view.findViewById(R.id.iv_arrow);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
            tv_percent = (TextView) view.findViewById(R.id.tv_percent);
            tv_state = (TextView) view.findViewById(R.id.tv_state);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_location = (TextView) view.findViewById(R.id.tv_location);
            RelativeLayout ll = (RelativeLayout) view.findViewById(R.id.ll_price);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i == 0) {
//                lp.setMargins(getResources().getDimensionPixelSize(R.dimen.dim40), getResources().getDimensionPixelSize(R.dimen.dim0), getResources().getDimensionPixelSize(R.dimen.dim20), getResources().getDimensionPixelSize(R.dimen.dim0));
                lp.setMargins(getResources().getDimensionPixelSize(R.dimen.dim40), getResources().getDimensionPixelSize(R.dimen.dim0), getResources().getDimensionPixelSize(R.dimen.dim0), getResources().getDimensionPixelSize(R.dimen.dim0));
            } else if (i == 2) {
//                lp.setMargins(getResources().getDimensionPixelSize(R.dimen.dim20), getResources().getDimensionPixelSize(R.dimen.dim0), getResources().getDimensionPixelSize(R.dimen.dim40), getResources().getDimensionPixelSize(R.dimen.dim0));
                lp.setMargins(getResources().getDimensionPixelSize(R.dimen.dim0), getResources().getDimensionPixelSize(R.dimen.dim0), getResources().getDimensionPixelSize(R.dimen.dim40), getResources().getDimensionPixelSize(R.dimen.dim0));
            } else {
//                lp.setMargins(getResources().getDimensionPixelSize(R.dimen.dim20), getResources().getDimensionPixelSize(R.dimen.dim0), getResources().getDimensionPixelSize(R.dimen.dim20), getResources().getDimensionPixelSize(R.dimen.dim0));
                lp.setMargins(getResources().getDimensionPixelSize(R.dimen.dim40), getResources().getDimensionPixelSize(R.dimen.dim0), getResources().getDimensionPixelSize(R.dimen.dim40), getResources().getDimensionPixelSize(R.dimen.dim0));
            }
            ll.setLayoutParams(lp);
            tv_companyName.setText(resultBean.getProductName() + " " + resultBean.getFactoryName());
            tv_price.setText(resultBean.getShowPrice());
            tv_percent.setText(resultBean.getProteinSpec() + " | " + DateUtil.parseYYYYmmToMM(resultBean.getDeliveryDate()) + "月");
            tv_state.setText(resultBean.getPrice());
            tv_time.setText(DateUtil.dateToString("MM-dd HH:mm",new Date(resultBean.getQuoteDate())));
            tv_location.setText(resultBean.getDeliveryLocation());
            if(resultBean.getPrice().equals("") || resultBean.getPrice().equals("持平")) {
                ll_price.setBackground(getResources().getDrawable(R.drawable.price_bg_balance));
            }else if(resultBean.getPrice().substring(0,1).equals("-")){
//                Drawable mRouteOnDraw = context.getResources().getDrawable(
//                        R.drawable.icon_godown);
                iv_arrow.setBackground(getResources().getDrawable(R.drawable.icon_price_down));
                ll_price.setBackground(getResources().getDrawable(R.drawable.price_bg_down));
            }else{
//                Drawable mRouteOnDraw = context.getResources().getDrawable(
//                        R.drawable.icon_goup);
                iv_arrow.setBackground(getResources().getDrawable(R.drawable.icon_price_up));
                ll_price.setBackground(getResources().getDrawable(R.drawable.price_bg_up));
            }
            priceViews.add(view);
        }
        priceAdapter = new PriceAdapter();
        vp_price.setAdapter(priceAdapter);
    }


    class PriceAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            LogUtil.d("count","priceViews.size = " + priceViews.size());
            return priceViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(priceViews.get(position));
            return priceViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(priceViews.get(position));
        }

        @Override
        public float getPageWidth(int position) {
            if(position == 0 || position == (priceViews.size() - 1)){
                return getResources().getDimensionPixelSize(R.dimen.dim516) / DensityConst.width;
            }else{
                return getResources().getDimensionPixelSize(R.dimen.dim556) / DensityConst.width;
            }
        }
    }

}
