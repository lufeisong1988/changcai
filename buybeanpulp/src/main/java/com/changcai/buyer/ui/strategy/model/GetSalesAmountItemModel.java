package com.changcai.buyer.ui.strategy.model;

import com.changcai.buyer.common.Constants;
import com.changcai.buyer.interface_api.ApiServiceGenerator;
import com.changcai.buyer.interface_api.BaseApiModel;
import com.changcai.buyer.ui.strategy.api.StrategyService;
import com.changcai.buyer.ui.strategy.bean.SalesAmountItemBean;
import com.changcai.buyer.ui.strategy.present.GetSalesAmountItemPresentCallback;
import com.changcai.buyer.util.SPUtil;

import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by lufeisong on 2017/9/3.
 */

public class GetSalesAmountItemModel implements GetSalesAmountItemModelInterface{
    private GetSalesAmountItemPresentCallback getSalesAmountItemPresentCallback;

    public GetSalesAmountItemModel(GetSalesAmountItemPresentCallback getSalesAmountItemPresentCallback) {
        this.getSalesAmountItemPresentCallback = getSalesAmountItemPresentCallback;
    }

    @Override
    public void getSalesAmountItem(String businessDate,String currentPage) {
        Map<String, String> map = SPUtil.getObjectFromShare(Constants.REQUEST_BASE_PARAMETERS);
        map.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        map.put("businessDate", businessDate);
        map.put("currentPage", currentPage);
        StrategyService strategyService = ApiServiceGenerator.createService(StrategyService.class);
        strategyService.getSalesAmountItem(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseApiModel<SalesAmountItemBean>>() {
                    @Override
                    public void call(BaseApiModel<SalesAmountItemBean> salesAmountItemBeanBaseApiModel) {
                        getSalesAmountItemPresentCallback.onGetSalesAmountItemNext(salesAmountItemBeanBaseApiModel.getResultObject());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        getSalesAmountItemPresentCallback.onGetSalesAmountItemError(throwable);
                    }
                });
    }
}

interface GetSalesAmountItemModelInterface{
    void getSalesAmountItem(String businessDate,String currentPage);
}