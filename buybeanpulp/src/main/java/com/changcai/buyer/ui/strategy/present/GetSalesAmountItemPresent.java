package com.changcai.buyer.ui.strategy.present;

import com.changcai.buyer.ui.strategy.CashReportTwoViewModel;
import com.changcai.buyer.ui.strategy.bean.SalesAmountBean;
import com.changcai.buyer.ui.strategy.bean.SalesAmountItemBean;
import com.changcai.buyer.ui.strategy.model.GetSalesAmountItemModel;
import com.changcai.buyer.util.DateUtil;

import java.util.List;

/**
 * Created by lufeisong on 2017/9/3.
 */

public class GetSalesAmountItemPresent implements GetSalesAmountItemPresentInterface,GetSalesAmountItemPresentCallback{
    private CashReportTwoViewModel cashReportTwoViewModel;
    private GetSalesAmountItemModel getSalesAmountItemModel;
    private final int maxRequestCount = 10;//每次请求返回的最大数据长度

    private List<String> Xs;
    private List<String> Ys;
    private int currentPage = 0;
    private int currentMonthPosition = -1;

    public GetSalesAmountItemPresent(CashReportTwoViewModel cashReportTwoViewModel) {
        this.cashReportTwoViewModel = cashReportTwoViewModel;
        getSalesAmountItemModel = new GetSalesAmountItemModel(this);

    }

    @Override
    public void init(List<String> Xs,List<String>  Ys,SalesAmountBean.DateStrBean dateStr, int currentPosition) {
        this.Xs = Xs;
        this.Ys = Ys;
        this.currentMonthPosition = currentPosition;
        initAmountItem(currentPosition);
    }

    /**
     * 初始化数据
     * @param position
     */
    @Override
    public void initAmountItem(int position) {
        if(position == -1){
            if(cashReportTwoViewModel != null){
                cashReportTwoViewModel.emptyData();
                cashReportTwoViewModel.updateTotalSales("0");
                cashReportTwoViewModel.updateCurrentMonth(DateUtil.getStringDateYYYYmm());
            }
            return;
        }
        currentPage = 0;
        currentMonthPosition = position;
        if(cashReportTwoViewModel != null){
            cashReportTwoViewModel.showLoading();
        }
        if(Xs.size() > 0){
            getSalesAmountItemModel.getSalesAmountItem(Xs.get(position),String.valueOf(currentPage));
        }else{
            if(cashReportTwoViewModel != null){
                cashReportTwoViewModel.dismissLoading();
                cashReportTwoViewModel.emptyData();
            }
        }
    }

    /**
     * 刷新数据
     */
    @Override
    public void refreshAmountItem() {
        initAmountItem(currentMonthPosition);
    }

    /**
     * 加载更多数据
     */
    @Override
    public void loadMoreAmountItem() {
        if(Xs.size() > 0){
            currentPage++;
            getSalesAmountItemModel.getSalesAmountItem(Xs.get(currentMonthPosition),String.valueOf(currentPage));
        }
    }

    @Override
    public void onDestory() {
        cashReportTwoViewModel = null;
    }

    /**
     * 获取数据成功
     * @param salesAmountItemBean
     */
    @Override
    public void onGetSalesAmountItemNext(SalesAmountItemBean salesAmountItemBean) {
        List<SalesAmountItemBean.SalesDetailBean> lists = salesAmountItemBean.getSalesDetail();
        if(cashReportTwoViewModel != null){
            if(currentPage == 0){
                cashReportTwoViewModel.dismissLoading();
                cashReportTwoViewModel.stopRefreshView();
                cashReportTwoViewModel.refreshSalesAmountItem(lists);
                cashReportTwoViewModel.updateCurrentMonth(Xs.get(currentMonthPosition));
                cashReportTwoViewModel.updateTotalSales(salesAmountItemBean.getTotalSales());
                if(lists.size() == 0){
                    cashReportTwoViewModel.emptyData();
                }else if(lists.size() < maxRequestCount){
                    cashReportTwoViewModel.endData(false,false);
                }
            }else{
                cashReportTwoViewModel.stopLoadMoreView();
                cashReportTwoViewModel.loadMoreSalesAmountItem(lists);
                if(lists.size() == 0){
                    currentPage--;
                    cashReportTwoViewModel.endData(false,false);
                }else if(lists.size() < maxRequestCount){
                    cashReportTwoViewModel.endData(false,false);
                }else{

                }
            }
        }

    }

    /**
     * 获取数据失败
     * @param e
     */
    @Override
    public void onGetSalesAmountItemError(Throwable e) {
        if(currentPage != 0){
            currentPage--;
        }
        if(cashReportTwoViewModel != null){
            cashReportTwoViewModel.showNetErrorDialog();
            cashReportTwoViewModel.stopLoadMoreView();
            cashReportTwoViewModel.stopRefreshView();
            if(currentPage != 0){
                cashReportTwoViewModel.setPullLoadMoreEnable(true);
            }else{
                cashReportTwoViewModel.dismissLoading();
                cashReportTwoViewModel.setPullRefreshEnable(true);
//                cashReportTwoViewModel.refreshNetError();
            }
        }
    }
}
interface GetSalesAmountItemPresentInterface{
    void init(List<String> dates,List<String> Ys, SalesAmountBean.DateStrBean dateStr,int currentPosition);
    void initAmountItem(int position);
    void refreshAmountItem();
    void loadMoreAmountItem();
    void onDestory();
}
