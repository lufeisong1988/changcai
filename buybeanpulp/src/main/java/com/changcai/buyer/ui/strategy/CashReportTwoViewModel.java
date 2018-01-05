package com.changcai.buyer.ui.strategy;

import com.changcai.buyer.ui.strategy.bean.SalesAmountItemBean;

import java.util.List;

/**
 * Created by lufeisong on 2017/9/3.
 */

public interface CashReportTwoViewModel {

    void updateTotalSales(String totalSales);

    void updateCurrentMonth(String month);

    void refreshSalesAmountItem(List<SalesAmountItemBean.SalesDetailBean> list);

    void loadMoreSalesAmountItem(List<SalesAmountItemBean.SalesDetailBean> list);

    void setPullRefreshEnable(Boolean enable);

    void setPullLoadMoreEnable(Boolean enable);

    void endData(Boolean enable, boolean isLoadMoreFail);

    void stopRefreshView();

    void stopLoadMoreView();

    void showLoading();

    void dismissLoading();

    void emptyData();

    void showNetErrorDialog();


    void refreshNetError();

}
