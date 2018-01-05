package com.changcai.buyer.business_logic.about_buy_beans.point_price;

/**
 * Created by liuxingwei on 2017/4/6.
 */

public class PointPricePresenter implements PointPriceListContract.Presenter{
    PointPriceListContract.View view;

    public PointPricePresenter(PointPriceListContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void detach() {

    }
}
