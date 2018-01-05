package com.changcai.buyer.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.changcai.buyer.R;
import com.changcai.buyer.ui.news.bean.RecommendNewsEntity;
import com.changcai.buyer.util.AndroidUtil;
import com.changcai.buyer.util.PicassoImageLoader;
import com.changcai.buyer.view.banner.Holder;

/**
 * 首页广告轮播图自定义视图
 *
 * @author GONGGUODONG377@pingan.com.cn
 */
public class HomeAdsHolderView implements Holder<RecommendNewsEntity> {

    private Context mContext = null;
    /**
     * 广告图片
     */
    private RoundImageView ivAds;

    private Drawable drawable;



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View createView(Context context) {
        mContext = context;
        ivAds = new RoundImageView(context);
        ivAds.setScaleType(ImageView.ScaleType.FIT_XY);
        ivAds.setBorderRadius(6);
        ivAds.setType(RoundImageView.TYPE_ROUND);
        return ivAds;
    }

    @Override
    public void UpdateUI(Context context, final int position,final RecommendNewsEntity data) {
        ivAds.setTag(data);
        ivAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("url", data.getUrl());
                b.putString("title", "资讯详情");
                b.putString("bannerTitle",data.getName());
                b.putString("info",data.getSummary());
                AndroidUtil.startBrowser(mContext, b, false);
            }
        });
//        ImageLoader.getInstance().displayImage(data.getPicture(), ivAds);
        if (drawable == null){
            drawable = ContextCompat.getDrawable(mContext, R.drawable.cc_bg_defualt);
        }
        PicassoImageLoader.getInstance().displayNetImage((Activity) mContext,data.getPicture(),ivAds,drawable);
    }
}
