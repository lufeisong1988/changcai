package com.changcai.buyer.ui.strategy;

import com.changcai.buyer.ui.strategy.bean.SalesAmountBean;

import java.util.List;

/**
 * Created by lufeisong on 2017/9/2.
 */

public interface CashReportOneViewModel {
    void updateSalesAmountData(List<String> Xs, List<String> Ys, SalesAmountBean.DateStrBean dateStrBean, int position);

    void updateToFiveData(String str);

    void showLoading();
    void dismissLoading();
    void showNetErrorDialog();

}
