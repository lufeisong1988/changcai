package com.changcai.buyer.view.banner;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.changcai.buyer.util.AndroidUtil;

/**
 * Created by liuxingwei on 2017/6/28.
 */

public class CBLoopPagerTransformer implements ViewPager.PageTransformer {


    private static final float MIN_SCALE = 0.9F;
    @Override
    public void transformPage(View page, float position) {

        if(position < -1){
            page.setScaleY(MIN_SCALE);
        }else if(position<= 1){
            //
            float scale = Math.max(MIN_SCALE,1 - Math.abs(position));
            page.setScaleY(scale);
            /*page.setScaleX(scale);

            if(position<0){
                page.setTranslationX(width * (1 - scale) /2);
            }else{
                page.setTranslationX(-width * (1 - scale) /2);
            }*/

        }else{
            page.setScaleY(MIN_SCALE);
        }
    }

//    private static final float MIN_SCALE = 0.85f;
//    private static final float MIN_ALPHA = 0.5f;

//    @Override
//    public void transformPage(View view, float position) {
//        int pageWidth = view.getWidth();
//        int pageHeight = view.getHeight();
//        if (position < -1) { // [-Infinity,-1)
//            // This page is way off-screen to the left.
////            view.setAlpha(MIN_ALPHA);
//            view.setScaleX(MIN_SCALE);
//            view.setScaleY(MIN_SCALE);
//        } else if (position <= 1) { // [-1,1]
//            // Modify the default slide transition to shrink the page as well
//            if (position < 0) {
//                view.setScaleX(1 + 0.1f * position);
//                view.setScaleY(1 + 0.1f * position);
//            } else {
//                view.setScaleX(1 - 0.1f * position);
//                view.setScaleY(1 - 0.1f * position);
//            }
//
//            // Scale the page down (between MIN_SCALE and 1)
//
//            // Fade the page relative to its size.
////            view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
//
//        } else { // (1,+Infinity]
//            // This page is way off-screen to the right.
//            view.setScaleX(MIN_SCALE);
//            view.setScaleY(MIN_SCALE);
////            view.setAlpha(MIN_ALPHA);
//        }
//    }
//    private int maxTranslateOffsetX;
//    private ViewPager viewPager;
//
//    public CBLoopPagerTransformer(Context context) {
//        this.maxTranslateOffsetX = AndroidUtil.dip2px(context, 180);
//    }


//    /**
//     * dp和像素转换
//     */
//    private int dp2px(Context context, float dipValue) {
//        float m = context.getResources().getDisplayMetrics().density;
//        return (int) (dipValue * m + 0.5f);
//    }

//    @Override
//    public void transformPage(View page, float position) {
//        if (viewPager == null) {
//            viewPager = (ViewPager) page.getParent();
//        }
//
//        int leftInScreen = page.getLeft() - viewPager.getScrollX();
//        int centerXInViewPager = leftInScreen + page.getMeasuredWidth() / 2;
//        int offsetX = centerXInViewPager - viewPager.getMeasuredWidth() / 2;
//        float offsetRate = (float) offsetX * 0.38f / viewPager.getMeasuredWidth();
//        float scaleFactor = 1 - Math.abs(offsetRate);
//        if (scaleFactor > 0) {
//            page.setScaleX(scaleFactor);
//            page.setScaleY(scaleFactor);
//            page.setTranslationX(-maxTranslateOffsetX * offsetRate);
//        }
//    }

}
