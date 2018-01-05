package com.changcai.buyer.view;

import com.changcai.buyer.view.indicator.commonnavigator.model.PositionData;

import java.util.List;

/**
 * Created by liuxingwei on 2017/6/21.
 */

public interface ScrollNavigationTabFunction {


    void onStartTabSelected( String title,  int index);

    void onEndTabSelected( String title,  int index);


    /////////////////////////
    void onPositionDataProvide(List<PositionData> dataList);
    void updateIndicatorPosition(float fraction);

    void onAttachView();

    void onDetachFromViewGroup();

}
