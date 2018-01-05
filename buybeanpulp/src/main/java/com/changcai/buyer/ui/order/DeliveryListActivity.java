package com.changcai.buyer.ui.order;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.changcai.buyer.R;
import com.changcai.buyer.business_logic.about_buy_beans.full_pay.PayActivity;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.common.Urls;
import com.changcai.buyer.http.HttpListener;
import com.changcai.buyer.http.VolleyUtil;
import com.changcai.buyer.listener.CustomListener;
import com.changcai.buyer.ui.base.BaseActivity;
import com.changcai.buyer.ui.order.bean.DeliveryInfo;
import com.changcai.buyer.ui.quote.ComputerOperationActivity;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.ServerErrorCodeDispatch;
import com.changcai.buyer.view.IndicatorView;
import com.changcai.buyer.view.XListView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class DeliveryListActivity extends BaseActivity{
    private XListView mListView;
    private LinearLayout ll_empty_view;
    private DeliverListAdapter mAdapter;
    private ArrayList<DeliveryInfo> orderInfos = new ArrayList<>();
    private int currentPage = 0;
    private String status = "";
    private IndicatorView indicatorView;
    private boolean isFromOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_list);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            status = bundle.getString("deliveryStatus", "");
            if (bundle.containsKey("delivery")){
                isFromOrder =true;
                orderInfos.addAll((Collection<? extends DeliveryInfo>) bundle.getSerializable("delivery"));
            }
        }

        initTitle();
        initView();
    }


    @Override
    protected void onResume() {
        super.onResume();
        getOrderList(currentPage);
    }

    private void initView() {
        btnBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("我的提货单");
        titleView.setBackgroundColor(getResources().getColor(R.color.white));
        btnBack.setImageResource(R.drawable.icon_nav_back);
        tvTitle.setTextColor(getResources().getColor(R.color.black));
        ll_empty_view = (LinearLayout) findViewById(R.id.ll_empty_view);

        View viewBottomLine = findViewById(R.id.view_bottom_line);
        viewBottomLine.setVisibility(View.GONE);

        indicatorView = (IndicatorView) findViewById(R.id.indicatorView);

        if (isFromOrder){
            indicatorView.setVisibility(View.GONE);
        }

        if (Constants.UNPAY.equalsIgnoreCase(status)) {
            indicatorView.setIndex(1);
        } else if (Constants.SELLER_CONFIRMED.equalsIgnoreCase(status)) {
            indicatorView.setIndex(2);
        }

        indicatorView.setOnIndicatorChangedListener(new IndicatorView.OnIndicatorChangedListener() {
            @Override
            public void onIndicatorChanged(int oldSelectedIndex, int newSelectedIndex) {
                switch (newSelectedIndex) {
                    case 0:
                        status = "";
                        break;
                    case 1:
                        status = Constants.UNPAY;
                        break;
                    case 2:
                        status = Constants.SELLER_CONFIRMED;
                        break;

                    case 3:
                        status = Constants.INVALID;
                        break;
                }

                currentPage = 0;
                getOrderList(currentPage);
            }
        });

        mListView = (XListView) findViewById(R.id.mListView);
        mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(true);
        mListView.setHeaderBgNewStyle();

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
            mAdapter = new DeliverListAdapter(this);
            mListView.setAdapter(mAdapter);
        }
        mAdapter.setData(orderInfos);

        mAdapter.setCustomListener(new CustomListener() {
            @Override
            public void onCustomerListener(View v, int position) {
                if (v.getId() == R.id.ll_item_parent) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("deliveryId", orderInfos.get(position).getId());
                    gotoActivity(DeliveryDetailActivity.class, false, bundle);
                } else if (v.getId() == R.id.tv_status_btn) {

                    if (orderInfos.get(position).getButtons()!=null && orderInfos.get(position).getButtons().size()>0){
                        if (orderInfos.get(position).getButtons().get(0).getType().equalsIgnoreCase("PAY_DELIVERY") && orderInfos.get(position).getDeliveryMode().equalsIgnoreCase("SEND_ORDER")){
                            Bundle bundle = new Bundle();
                            bundle.putString("deliveryId",orderInfos.get(position).getId());
                            bundle.putString("payType","_goods_pay");
                            gotoActivity(PayActivity.class,bundle);
                        }else{
                            Bundle bundle = new Bundle();
                            bundle.putString("title", "订单详情");
                            bundle.putString("tips", "这项操作暂时无法在app内完成，你可以：");
                            gotoActivity(ComputerOperationActivity.class, bundle);
                        }
                    }

                }
            }
        });


    }

    /**
     * 获取提货单列表
     */
    private void getOrderList(final int page) {
        if (isFromOrder)return;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        params.put("currentPage", "" + page);
        params.put("deliveryStatus", status);

        VolleyUtil.getInstance().httpPost(this, Urls.GET_DELIVERY_LIST, params, new HttpListener() {
            @Override
            public void onResponseJson(JsonObject response) {
                super.onResponseJson(response);
                String errorCode = response.get(Constants.RESPONSE_CODE).getAsString();
                VolleyUtil.getInstance().cancelProgressDialog();
                List<DeliveryInfo> tempList = null;
                if (errorCode.equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {
                    Gson gson = new Gson();
                    if (page == 0) {
                        orderInfos.clear();
                        currentPage = 0;
                    } else {
                        currentPage++;
                    }
                    if (response.get(Constants.RESPONSE_CONTENT).isJsonArray()) {
                        tempList = (List<DeliveryInfo>) gson.fromJson(response.get(Constants.RESPONSE_CONTENT),
                                new TypeToken<List<DeliveryInfo>>() {
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
                    ServerErrorCodeDispatch.getInstance().checkErrorCode(DeliveryListActivity.this, errorCode, response.get(Constants.RESPONSE_DESCRIPTION).getAsString());
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

}
