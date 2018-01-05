package com.changcai.buyer.ui.strategy.present;

import android.content.Context;

import com.changcai.buyer.common.Constants;
import com.changcai.buyer.ui.strategy.CashReportOneViewModel;
import com.changcai.buyer.ui.strategy.bean.SalesAmountBean;
import com.changcai.buyer.ui.strategy.model.GetSalesAmonutModel;
import com.changcai.buyer.util.DesUtil;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.util.SPUtil;
import com.google.gson.Gson;

import java.util.Map;

/**
 * Created by lufeisong on 2017/9/2.
 */

public class GetSalesAmontuPrensent implements GetSalesAmontuPrensentIntreface,GetSalesAmountPresentCallback{
    private CashReportOneViewModel cashReportOneViewModel;
    private GetSalesAmonutModel getSalesAmonutModel;

    private SalesAmountBean salesAmountBean;
    public GetSalesAmontuPrensent(CashReportOneViewModel cashReportOneViewModel,Context context) {
        this.cashReportOneViewModel = cashReportOneViewModel;
        getSalesAmonutModel = new GetSalesAmonutModel(this,context);
    }

    @Override
    public void getSalesAmount() {
        LogUtil.d("CashReportOneFragment","getSalesAmount");
        if(cashReportOneViewModel != null){
            cashReportOneViewModel.showLoading();
        }
        getSalesAmonutModel.getSalesAmount();



    }

    @Override
    public void getTopFiveData(int position) {
        if(salesAmountBean != null) {
            Map<String, String> map = SPUtil.getObjectFromShare(Constants.REQUEST_BASE_PARAMETERS);
            map.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
            map.put("businessDate", salesAmountBean.getSalesByMonth().getX().get(position));
            Gson gson = new Gson();
            if (cashReportOneViewModel != null) {
                cashReportOneViewModel.updateToFiveData(DesUtil.encrypt(gson.toJson(map), "abcdefgh"));
            }
        }
    }

    @Override
    public void onDestory() {
        cashReportOneViewModel = null;
    }

    @Override
    public void setData(SalesAmountBean salesAmountBean) {
        this.salesAmountBean = salesAmountBean;
    }

    /**
     * 获取每月销售额 成功
     * @param salesAmountBean
     */
    @Override
    public void onGetSalesAmountNext(SalesAmountBean salesAmountBean) {
        this.salesAmountBean = salesAmountBean;
        if(cashReportOneViewModel != null){
            cashReportOneViewModel.dismissLoading();
            cashReportOneViewModel.updateSalesAmountData(salesAmountBean.getSalesByMonth().getX(),salesAmountBean.getSalesByMonth().getY(),salesAmountBean.getDateStr(),salesAmountBean.getSalesByMonth().getX().indexOf(salesAmountBean.getNowDate()));
        }
    }

    /**
     * 获取每月销售额 失败
     * @param e
     */
    @Override
    public void onGetSalesAmountError(Throwable e) {
        if(cashReportOneViewModel != null){
            cashReportOneViewModel.dismissLoading();
            cashReportOneViewModel.showNetErrorDialog();
        }
    }
}
interface GetSalesAmontuPrensentIntreface{
    void getSalesAmount();

    void getTopFiveData(int position);

    void onDestory();

    void setData(SalesAmountBean salesAmountBean);
}

