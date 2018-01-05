package com.changcai.buyer.ui.strategy.present;

import com.changcai.buyer.ui.strategy.bean.SalesAmountBean;

/**
 * Created by lufeisong on 2017/9/2.
 */

public interface GetSalesAmountPresentCallback {
    void onGetSalesAmountNext(SalesAmountBean salesAmountBean);
    void onGetSalesAmountError(Throwable e);
}
