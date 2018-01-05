package com.changcai.buyer.business_logic.about_buy_beans.person_authenticate.big_photo;

/**
 * Created by liuxingwei on 2016/11/30.
 */

public class BigPhotoPresenter implements BigPhotoContract.Presenter{

    protected BigPhotoContract.View view;

    public BigPhotoPresenter(BigPhotoContract.View view) {
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
