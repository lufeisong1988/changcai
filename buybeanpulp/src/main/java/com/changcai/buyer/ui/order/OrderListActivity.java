package com.changcai.buyer.ui.order;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.UserInfo;
import com.changcai.buyer.business_logic.about_buy_beans.full_pay.FullPayActivity;
import com.changcai.buyer.business_logic.about_buy_beans.full_pay.PayActivity;
import com.changcai.buyer.business_logic.about_buy_beans.sign_contract.SignContractActivity;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.common.Urls;
import com.changcai.buyer.http.HttpListener;
import com.changcai.buyer.http.VolleyUtil;
import com.changcai.buyer.listener.CustomListener;
import com.changcai.buyer.ui.base.BaseActivity;
import com.changcai.buyer.ui.base.BaseTouchBackActivity;
import com.changcai.buyer.ui.order.bean.Buttons;
import com.changcai.buyer.ui.order.bean.OrderInfo;
import com.changcai.buyer.ui.quote.ComputerOperationActivity;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.ServerErrorCodeDispatch;
import com.changcai.buyer.view.ConfirmDialog;
import com.changcai.buyer.view.IndicatorView;
import com.changcai.buyer.view.XListView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderListActivity extends BaseActivity implements View.OnClickListener {
    private XListView mListView;
    private LinearLayout ll_empty_view;

    private OrderListAdapter mAdapter;
    private ArrayList<OrderInfo> orderInfos = new ArrayList<>();
    private int currentPage = 0;
    private String status = "";
    private UserInfo userInfo;

    private boolean orderAction;//有订单操作行为


    private IndicatorView _indicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            status = bundle.getString("orderStatus", "");
        }
        userInfo = SPUtil.getObjectFromShare(Constants.KEY_USER_INFO);

        initTitle();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (orderAction) {
            currentPage = 0;
            getOrderList(currentPage);
        } else {
            getOrderList(currentPage);
        }
    }

    private void initView() {
        btnBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("我的订单");
//        else if (status.equalsIgnoreCase(Constants.NEW_BUY_CONFIRMING)) {
//            tvTitle.setText("待签署合同订单");
//        } else if (status.equalsIgnoreCase(Constants.ALL_PAY)) {
//            tvTitle.setText("待全额付订单");
//        } else if (status.equalsIgnoreCase(Constants.FAST_PAY)) {
//            tvTitle.setText("待转为直接付订单");
//        }
        titleView.setBackgroundColor(getResources().getColor(R.color.white));
        btnBack.setImageResource(R.drawable.icon_nav_back);
        tvTitle.setTextColor(getResources().getColor(R.color.black));
        View viewBottomLine = findViewById(R.id.view_bottom_line);
        viewBottomLine.setVisibility(View.GONE);
        _indicatorView = (IndicatorView) findViewById(R.id.indicatorView);

        if (TextUtils.isEmpty(status)) {
            _indicatorView.setIndex(0);
        } else if (status.contentEquals(Constants.NEW_BUY_CONFIRMING)) {
            _indicatorView.setIndex(1);
        } else if (status.contentEquals(Constants.NEW_BUY_DEPOSITING)) {
            _indicatorView.setIndex(2);
        } else if (status.contentEquals(Constants.OPEN_BUY_PICK)) {
            _indicatorView.setIndex(3);
        }
        _indicatorView.setOnIndicatorChangedListener(new IndicatorView.OnIndicatorChangedListener() {
            @Override
            public void onIndicatorChanged(int oldSelectedIndex, int newSelectedIndex) {
                switch (newSelectedIndex) {
                    case 0:
                        status = "";
                        currentPage = 0;
                        getOrderList(currentPage);
                        break;
                    case 1:
                        //待签署合同
                        status = Constants.NEW_BUY_CONFIRMING;
                        currentPage = 0;
                        getOrderList(currentPage);
                        break;
                    case 2:
                        //待支付
                        status = Constants.NEW_BUY_DEPOSITING;
                        currentPage = 0;
                        getOrderList(currentPage);
                        break;
                    case 3:
                        //待提货
                        status = Constants.OPEN_BUY_PICK;
                        currentPage = 0;
                        getOrderList(currentPage);
                        break;
                }
            }
        });
        mListView = (XListView) findViewById(R.id.mListView);
        mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        ll_empty_view = (LinearLayout) findViewById(R.id.ll_empty_view);
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(true);
        mListView.setHeaderBgNewStyle();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrderInfo orderInfo = (OrderInfo) parent.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("orderInfo", orderInfo);
                gotoActivity(OrderDetailActivity.class, bundle);
                orderAction = true;
            }
        });
        mListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onPullRefresh() {
                currentPage = 0;
                getOrderList(currentPage);
            }

            @Override
            public void onPullLoadMore() {
                getOrderList(currentPage + 1);
            }
        });
        if (mAdapter == null) {
            mAdapter = new OrderListAdapter(this);
            mListView.setAdapter(mAdapter);
        }
        mAdapter.setData(orderInfos);

        mAdapter.setCustomListener(new CustomListener() {
            @Override
            public void onCustomerListener(View v, int position) {
                //全额付款
                Buttons button = getButtons(orderInfos, position);
                if (orderInfos.get(position).getOrderType().contentEquals("BASIS")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("title", "订单详情");
                    bundle.putString("tips", "这项操作暂时无法在app内完成，你可以：");
                    gotoActivity(ComputerOperationActivity.class, false, bundle);
                    return;
                }
                if (button.getType().contentEquals("ALL_PAY")) {
                    if (userInfo.getType().contentEquals("admin") || userInfo.getType().contentEquals("finance")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("orderId", orderInfos.get(position).getId());
                        gotoActivity(FullPayActivity.class, bundle);
                        orderAction = true;
                    } else {
                        showErrorDialog(R.string.limit_enough_contact, R.string.limit_enough);
                    }
                } else if (button.getType().contentEquals("PAY_FRONT_MONEY")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("orderId",orderInfos.get(position).getId());
                    bundle.putString("payType","_down_pay");
                    gotoActivity(PayActivity.class,bundle);
                } else if (button.getType().contentEquals("PAY_DELIVERY")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("deliveryId",orderInfos.get(position).getDeliverys().get(0).getId());
                    bundle.putString("payType","_goods_pay");
                    gotoActivity(PayActivity.class,bundle);
                } else if (button.getType().contentEquals("CONTRACT")) {
                    if (userInfo.getType().contentEquals("admin") || userInfo.getType().contentEquals("finance")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("orderId", orderInfos.get(position).getId());
                        gotoActivity(SignContractActivity.class, bundle);
                        orderAction = true;
                    } else {
                        showErrorDialog(R.string.limit_enough_contact, R.string.limit_enough);
                    }


                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("title", "订单详情");
                    bundle.putString("tips", "这项操作暂时无法在app内完成，你可以：");
                    gotoActivity(ComputerOperationActivity.class, false, bundle);
                }
            }
        });
    }

    private Buttons getButtons(ArrayList<OrderInfo> orderInfos, int postions) {
        for (Buttons b : orderInfos.get(postions).getButtons()) {
            if (!b.getType().equalsIgnoreCase("CONFIRM_FAST_PAY")) {
                return b;
            }
        }
        return null;
    }

    /**
     * 获取订单列表
     */
    private void getOrderList(final int page) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        params.put("currentPage", "" + page);
        params.put("orderStatus", status);

        VolleyUtil.getInstance().httpPost(this, Urls.GET_ORDER_LIST, params, new HttpListener() {
            @Override
            public void onResponseJson(JsonObject response) {
                super.onResponseJson(response);
                String errorCode = response.get(Constants.RESPONSE_CODE).getAsString();
                VolleyUtil.getInstance().cancelProgressDialog();
                List<OrderInfo> tempList = null;
                if (errorCode.equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {
                    Gson gson = new Gson();
                    if (page == 0) {
                        orderInfos.clear();
                        currentPage = 0;
                    } else {
                        currentPage++;
                    }
                    if (response.get(Constants.RESPONSE_CONTENT).isJsonArray()) {
                        tempList = (List<OrderInfo>) gson.fromJson(response.get(Constants.RESPONSE_CONTENT),
                                new TypeToken<List<OrderInfo>>() {
                                }.getType());
                        orderInfos.addAll(tempList);
                    }
                    mAdapter.setData(orderInfos);
                    if (page == 0) {
                        if (tempList != null) {
                            if (tempList.size() < 10) {
                                if (tempList.size() == 0) {
                                    mListView.setEmptyView(ll_empty_view);
                                }
                                mListView.setPullLoadEnable(false);
                            } else {
                                mListView.setPullLoadEnable(true);
                                mListView.startAutoLoadMore();
                            }
                        }
                    } else {
                        if (tempList != null && tempList.size() < 10)
                            mListView.setPullLoadEnable(false);
                    }
                } else {
                    mListView.setPullLoadEnable(true);
                    ServerErrorCodeDispatch.getInstance().checkErrorCode(OrderListActivity.this, errorCode, response.get(Constants.RESPONSE_DESCRIPTION).getAsString());
                }
                if (page == 0)
                    mListView.stopRefresh();
                else
                    mListView.stopLoadMore();
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                VolleyUtil.getInstance().cancelProgressDialog();
                if (page == 0)
                    mListView.stopRefresh();
                else
                    mListView.stopLoadMore();
            }
        }, false);
    }


    public void showErrorDialog(int resource, int title) {
        ConfirmDialog.createConfirmDialogWithTitle(this, getString(resource), getString(title));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_detail:
                break;
        }
    }
}
