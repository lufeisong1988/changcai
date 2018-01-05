package com.changcai.buyer.ui.strategy.present;

import com.changcai.buyer.ui.strategy.bean.SalesAmountItemBean;

/**
 * Created by lufeisong on 2017/9/3.
 */

public interface GetSalesAmountItemPresentCallback {
    void onGetSalesAmountItemNext(SalesAmountItemBean salesAmountItemBean);
    void onGetSalesAmountItemError(Throwable e);
}
