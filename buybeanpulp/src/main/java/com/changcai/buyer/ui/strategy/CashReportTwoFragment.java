package com.changcai.buyer.ui.strategy;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.changcai.buyer.BaseFragment;
import com.changcai.buyer.R;
import com.changcai.buyer.ui.strategy.adapter.SalesAmountItemAdapter;
import com.changcai.buyer.ui.strategy.bean.SalesAmountItemBean;
import com.changcai.buyer.ui.strategy.present.GetSalesAmountItemPresent;
import com.changcai.buyer.util.DateUtil;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.util.ServerErrorCodeDispatch;
import com.changcai.buyer.view.RotateDotsProgressView;
import com.changcai.buyer.view.XListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.changcai.buyer.ui.strategy.CashReportFragment.XList;
import static com.changcai.buyer.ui.strategy.CashReportFragment.YList;
import static com.changcai.buyer.ui.strategy.CashReportFragment.currentMonthPosition;
import static com.changcai.buyer.ui.strategy.CashReportFragment.dateStr;

/**
 * Created by lufeisong on 2017/9/2.
 * 销售明细
 */

public class CashReportTwoFragment extends BaseFragment implements CashReportTwoViewModel {
    @BindView(R.id.ll_empty_view)
    LinearLayout llEmptyView;
    @BindView(R.id.ll_container_empty_view)
    LinearLayout llContainerEmptyView;
    @BindView(R.id.rl_bottom_container)
    RelativeLayout rlBottomContainer;
    private CashReportTwoFragment instance;
    @BindView(R.id.tv_cashreport_two_money)
    TextView tvCashreportTwoMoney;
    @BindView(R.id.tv_cashreport_two_month)
    TextView tvCashreportTwoMonth;
    @BindView(R.id.xlv_cashreprot_two)
    XListView xlvCashreprotTwo;
    @BindView(R.id.dots_progress)
    RotateDotsProgressView dotsProgress;
    private DatePickerPopWin pickerPopWin;

    private GetSalesAmountItemPresent getSalesAmountItemPresent;
    private SalesAmountItemAdapter adapter;
    private List<SalesAmountItemBean.SalesDetailBean> list = new ArrayList<>();

    private int _currentMonthPosition = -1;

    public static CashReportTwoFragment getInstance() {
        CashReportTwoFragment fragment = new CashReportTwoFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cashreport_two, container, false);
        baseUnbinder = ButterKnife.bind(this, view);
        init();
        initListener();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getSalesAmountItemPresent.onDestory();
    }

    private void init() {
        getSalesAmountItemPresent = new GetSalesAmountItemPresent(this);

        xlvCashreprotTwo.addHeaderView(LayoutInflater.from(activity).inflate(R.layout.adapter_salesamount_item, null));
        xlvCashreprotTwo.setEnableOnRefreshWhileRefreshing(false);
        xlvCashreprotTwo.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onPullRefresh() {
                getSalesAmountItemPresent.refreshAmountItem();
            }

            @Override
            public void onPullLoadMore() {
                getSalesAmountItemPresent.loadMoreAmountItem();
            }
        });

        adapter = new SalesAmountItemAdapter(list, activity);
        xlvCashreprotTwo.setAdapter(adapter);
        getSalesAmountItemPresent.init(XList, YList, dateStr, currentMonthPosition);
    }

    private void initListener() {
        tvCashreportTwoMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dateStr == null || dateStr.getMonthsBeenList().size() == 0){
                    return;
                }
                final ArrayList<String> years = new ArrayList<String>();
                final ArrayList<ArrayList<String>> months = new ArrayList<ArrayList<String>>();
                for (int i = 0; i < dateStr.getMonthsBeenList().size(); i++) {
                    years.add(dateStr.getMonthsBeenList().get(i).getYear());
                    months.add(dateStr.getMonthsBeenList().get(i).getMonths());
                }
                pickerPopWin = new DatePickerPopWin.Builder(activity, new DatePickerPopWin.OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                        String yearMonth = years.get(year).substring(0, 4) + "-" + months.get(year).get(month).substring(0, 2);
                        currentMonthPosition = XList.indexOf(yearMonth);
                        LogUtil.d("TAG", "year:" + year + ";month:" + month);
                        getSalesAmountItemPresent.initAmountItem(currentMonthPosition);
                    }
                }).textConfirm("确定") //text of confirm button
                        .btnTextSize(16) // button text size
                        .viewTextSize(25) // pick view text size
                        .colorConfirm(Color.parseColor("#FFFFFF"))//color of confirm button
                        .dateChose(XList.get(currentMonthPosition) + "-01") // date chose when init popwindow
                        .setWriteYears(years)
                        .setWriteMonths(months)
                        .build();
                pickerPopWin.showPopWin(activity);
            }
        });
    }


    @Override
    public void updateTotalSales(String totalSales) {
        tvCashreportTwoMoney.setText(totalSales);
    }

    @Override
    public void updateCurrentMonth(String month) {
        tvCashreportTwoMonth.setText(month);
    }

    @Override
    public void refreshSalesAmountItem(List<SalesAmountItemBean.SalesDetailBean> list) {
        _currentMonthPosition = currentMonthPosition;
        this.list.clear();
        this.list.addAll(list);
        adapter.notifyDataSetChanged();
        llEmptyView.setVisibility(View.GONE);
        llContainerEmptyView.setVisibility(View.GONE);
    }

    @Override
    public void loadMoreSalesAmountItem(List<SalesAmountItemBean.SalesDetailBean> list) {
        this.list.addAll(list);
        adapter.notifyDataSetChanged();
        llEmptyView.setVisibility(View.GONE);
        llContainerEmptyView.setVisibility(View.GONE);
    }

    @Override
    public void setPullRefreshEnable(Boolean enable) {
        xlvCashreprotTwo.setPullRefreshEnable(enable);
    }

    @Override
    public void setPullLoadMoreEnable(Boolean enable) {
        xlvCashreprotTwo.setPullLoadEnable(enable);
    }

    @Override
    public void endData(Boolean enable, boolean isLoadMoreFail) {
        xlvCashreprotTwo.setPullLoadEnable(enable, R.string.no_more_conten, isLoadMoreFail);
    }

    @Override
    public void stopRefreshView() {
        xlvCashreprotTwo.stopRefresh("最后更新 " + DateUtil.getStringDate());
    }

    @Override
    public void stopLoadMoreView() {
        xlvCashreprotTwo.stopLoadMore();
    }

    @Override
    public void showLoading() {
        dotsProgress.setVisibility(View.VISIBLE);
        dotsProgress.showAnimation(true);
    }

    @Override
    public void dismissLoading() {
        dotsProgress.setVisibility(View.GONE);
        dotsProgress.refreshDone(true);
    }

    @Override
    public void emptyData() {
        llEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNetErrorDialog() {
        ServerErrorCodeDispatch.getInstance().showNetErrorDialog(getContext(), getString(R.string.network_unavailable));
    }


    @Override
    public void refreshNetError() {

    }

    public void refresh() {
//        if (_currentMonthPosition != currentMonthPosition ) {
        getSalesAmountItemPresent.init(XList, YList, dateStr, currentMonthPosition);
//        }
    }
}
