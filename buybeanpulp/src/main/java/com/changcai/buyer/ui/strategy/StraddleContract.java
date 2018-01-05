package com.changcai.buyer.ui.strategy;

import android.content.Context;
import android.support.annotation.DrawableRes;

import com.changcai.buyer.BasePresenter;
import com.changcai.buyer.BaseView;
import com.changcai.buyer.bean.SpotFolderListBean;
import com.changcai.buyer.bean.StraddleModel;

import java.util.List;

/**
 * Created by liuxingwei on 2017/8/1.
 */

public interface StraddleContract {

    interface View extends BaseView<StraddleContract.Presenter> {
        boolean isActive();

        void addDataList(List<StraddleModel> straddleModels);

        void addDataListWithOnRefresh(List<StraddleModel> straddleModels);


        void addDataListWithLoadMore(List<StraddleModel> straddleModels);

        Context getViewContext();

        void showProgress();

        void dismissProgress();

    }

    interface Presenter extends BasePresenter {
        void getStraddleData(int pageIndex);

        void getStraddleDataOnRefresh();
    }
}
