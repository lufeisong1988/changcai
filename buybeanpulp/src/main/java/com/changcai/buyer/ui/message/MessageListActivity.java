package com.changcai.buyer.ui.message;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.common.Urls;
import com.changcai.buyer.http.HttpListener;
import com.changcai.buyer.http.VolleyUtil;
import com.changcai.buyer.ui.base.BaseActivity;
import com.changcai.buyer.ui.base.BaseTouchBackActivity;
import com.changcai.buyer.ui.message.bean.ExtrasInfo;
import com.changcai.buyer.ui.message.bean.MessageInfo;
import com.changcai.buyer.ui.order.DeliveryDetailActivity;
import com.changcai.buyer.ui.order.OrderDetailActivity;
import com.changcai.buyer.ui.order.bean.OrderInfo;
import com.changcai.buyer.util.AndroidUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.ServerErrorCodeDispatch;
import com.changcai.buyer.util.UserDataUtil;
import com.changcai.buyer.view.ConfirmDialog;
import com.changcai.buyer.view.XListView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageListActivity extends BaseTouchBackActivity implements View.OnClickListener {
    private XListView mListView;
    private LinearLayout ll_empty_view;
    private MessageListAdapter mAdapter;
    private ArrayList<MessageInfo> messageInfos = new ArrayList<>();
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        getMsgList(currentPage);
        initTitle();
        initView();
    }

    private void initView() {
        btnBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("消息列表");
        titleView.setBackgroundColor(getResources().getColor(R.color.global_blue));
        tvTitle.setTextColor(getResources().getColor(R.color.white));
        ll_empty_view = (LinearLayout) findViewById(R.id.ll_empty_view);

        mListView = (XListView) findViewById(R.id.mListView);
        mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(true);
        mListView.setHeaderBgNewStyle();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessageInfo messageInfo = (MessageInfo) parent.getAdapter().getItem(position);
                ExtrasInfo extrasInfo =  messageInfo.getExtras();
                if(extrasInfo != null) {
                    TextView tv_title =  (TextView) view.findViewById(R.id.tv_title);
                    if(tv_title != null) {
                        tv_title.setTextColor(getResources().getColor(R.color.global_text_gray));
                    }
                    readMsg(messageInfo.getId());
                    if(!TextUtils.isEmpty(extrasInfo.getArticle())){
                        Bundle b = new Bundle();
                        b.putString("url", extrasInfo.getArticle());
                        b.putString("title", "资讯详情");
                        AndroidUtil.startBrowser(MessageListActivity.this, b, false);
                        return;
                    }

                    if(!TextUtils.isEmpty(extrasInfo.getOrder())){
                        Bundle b = new Bundle();
                        b.putString("orderId", extrasInfo.getOrder());
                        gotoActivity(OrderDetailActivity.class, b);
                        return;
                    }

                    if(!TextUtils.isEmpty(extrasInfo.getDelivery())){
                        Bundle b = new Bundle();
                        b.putString("deliveryId", extrasInfo.getDelivery());
                        gotoActivity(DeliveryDetailActivity.class, b);
                        return;
                    }
                }
            }
        });
        mListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onPullRefresh() {
                currentPage = 0;
                getMsgList(currentPage);
            }

            @Override
            public void onPullLoadMore() {
                getMsgList(currentPage + 1);
            }
        });
        if (mAdapter == null) {
            mAdapter = new MessageListAdapter(this);
            mListView.setAdapter(mAdapter);
        }
        mAdapter.setData(messageInfos);
    }

    /**
     * 获取列表
     */
    private void getMsgList(final int page) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        params.put("currentPage", "" + page);

        VolleyUtil.getInstance().httpPost(this, Urls.GET_MESSAGE_LIST, params, new HttpListener() {
            @Override
            public void onResponseJson(JsonObject response) {
                super.onResponseJson(response);
                String errorCode = response.get(Constants.RESPONSE_CODE).getAsString();
                VolleyUtil.getInstance().cancelProgressDialog();
                List<MessageInfo> tempList = null;
                if (errorCode.equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {
                    Gson gson = new Gson();
                    if (page == 0) {
                        messageInfos.clear();
                        currentPage = 0;
                    } else {
                        currentPage ++;
                    }
                    if (response.get(Constants.RESPONSE_CONTENT).isJsonArray()) {
                        tempList = (List<MessageInfo>) gson.fromJson(response.get(Constants.RESPONSE_CONTENT),
                                new TypeToken<List<MessageInfo>>() {
                                }.getType());
                        messageInfos.addAll(tempList);
                    }
                    mAdapter.setData(messageInfos);
                    if (page == 0) {
                        if (tempList != null) {
                            if (tempList.size() < 10) {
                                if(tempList.isEmpty()) {
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
                    currentPage++;
                } else {
                    mListView.setPullLoadEnable(true);
                    ServerErrorCodeDispatch.getInstance().checkErrorCode(MessageListActivity.this,errorCode, response.get(Constants.RESPONSE_DESCRIPTION).getAsString());
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


    /**
     * 阅读消息
     */
    private void readMsg(String msgId ) {

        if(!UserDataUtil.isLogin())
            return;

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        params.put("msgId", "" + msgId);

        VolleyUtil.getInstance().httpPost(this, Urls.READ_MESSAGE, params, new HttpListener() {
            @Override
            public void onResponseJson(JsonObject response) {
                super.onResponseJson(response);
                String errorCode = response.get(Constants.RESPONSE_CODE).getAsString();
                VolleyUtil.getInstance().cancelProgressDialog();
                List<MessageInfo> tempList = null;
                if (errorCode .equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {
                } else {
                    ServerErrorCodeDispatch.getInstance().checkErrorCode(MessageListActivity.this,errorCode, response.get(Constants.RESPONSE_DESCRIPTION).getAsString());
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
            }
        }, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_detail:
                break;
        }
    }
}
