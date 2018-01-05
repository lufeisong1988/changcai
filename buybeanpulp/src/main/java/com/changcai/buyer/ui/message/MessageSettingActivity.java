package com.changcai.buyer.ui.message;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.changcai.buyer.R;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.common.Urls;
import com.changcai.buyer.http.HttpListener;
import com.changcai.buyer.http.VolleyUtil;
import com.changcai.buyer.ui.base.BaseActivity;
import com.changcai.buyer.ui.message.bean.ExtrasInfo;
import com.changcai.buyer.ui.message.bean.MessageInfo;
import com.changcai.buyer.ui.message.bean.MessageSettingInfo;
import com.changcai.buyer.ui.order.DeliveryDetailActivity;
import com.changcai.buyer.ui.order.OrderDetailActivity;
import com.changcai.buyer.util.AndroidUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.ServerErrorCodeDispatch;
import com.changcai.buyer.view.XListView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageSettingActivity extends BaseActivity implements View.OnClickListener {

    private ToggleButton tg_order_alert;
    private ToggleButton tg_periodicReport;
    private ToggleButton tg_dataAnalysis;
    private ToggleButton tg_changcaiView;

    private XListView mListView;
    private MessageSettingAdapter mAdapter;
    private ArrayList<MessageSettingInfo> messageInfos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_setting);
        initTitle();
        initView();
        getMyExt();
    }

    private void initView() {
        btnBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("提醒设置");

        titleView.setBackgroundColor(getResources().getColor(R.color.global_blue));
        btnBack.setImageResource(R.drawable.icon_back_white);
        tvTitle.setTextColor(getResources().getColor(R.color.white));

        mListView = (XListView) findViewById(R.id.mListView);
        mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(false);
        mListView.setHeaderBgNewStyle();
        if (mAdapter == null) {
            mAdapter = new MessageSettingAdapter(this);
            mListView.setAdapter(mAdapter);
        }
    }

    /**
     * 设置消息
     */
    private void setMyExt() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        params.put("orderAlert", tg_order_alert.isChecked()?"1":"0");
        params.put("periodicReport", tg_periodicReport.isChecked()?"1":"0");
        params.put("changcaiView", tg_changcaiView.isChecked()?"1":"0");
        params.put("dataAnalysis", tg_dataAnalysis.isChecked()?"1":"0");
        VolleyUtil.getInstance().httpPost(this, Urls.SET_MY_EXT, params, new HttpListener() {
            @Override
            public void onResponseJson(JsonObject response) {
                super.onResponseJson(response);
                String errorCode = response.get(Constants.RESPONSE_CODE).getAsString();
                VolleyUtil.getInstance().cancelProgressDialog();
                if (errorCode .equalsIgnoreCase( Constants.REQUEST_SUCCESS_S)) {
                    JsonObject content = response.get(Constants.RESPONSE_CONTENT).getAsJsonObject();
                    tg_order_alert.setChecked("1".equals(content.get("orderAlert").getAsString()));
                    tg_periodicReport.setChecked("1".equals(content.get("periodicReport").getAsString()));
                    tg_changcaiView.setChecked("1".equals(content.get("changcaiView").getAsString()));
                    tg_dataAnalysis.setChecked("1".equals(content.get("dataAnalysis").getAsString()));
                } else {
                    ServerErrorCodeDispatch.getInstance().checkErrorCode(MessageSettingActivity.this,errorCode, response.get(Constants.RESPONSE_DESCRIPTION).getAsString());
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                VolleyUtil.getInstance().cancelProgressDialog();
            }
        }, false);
    }

    /**
     * 获取消息设置
     */
    private void getMyExt() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        VolleyUtil.getInstance().httpPost(this, Urls.GET_MY_EXT, params, new HttpListener() {
            @Override
            public void onResponseJson(JsonObject response) {
                super.onResponseJson(response);
                String errorCode = response.get(Constants.RESPONSE_CODE).getAsString();
                VolleyUtil.getInstance().cancelProgressDialog();
                if (errorCode.equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {
                    Gson gson = new Gson();
                    List<MessageSettingInfo> messageInfos = (List<MessageSettingInfo>) gson.fromJson(response.get(Constants.RESPONSE_CONTENT),
                            new TypeToken<List<MessageSettingInfo>>() {
                            }.getType());
                    mAdapter.setData(messageInfos);
//                    tg_order_alert.setChecked("1".equals(content.get("orderAlert").getAsString()));
//                    tg_periodicReport.setChecked("1".equals(content.get("periodicReport").getAsString()));
//                    tg_changcaiView.setChecked("1".equals(content.get("changcaiView").getAsString()));
//                    tg_dataAnalysis.setChecked("1".equals(content.get("dataAnalysis").getAsString()));
                } else {
                    ServerErrorCodeDispatch.getInstance().checkErrorCode(MessageSettingActivity.this,errorCode, response.get(Constants.RESPONSE_DESCRIPTION).getAsString());
                }
            }

            @Override
            public void onError(String error) {
                super.onError(error);
                VolleyUtil.getInstance().cancelProgressDialog();
            }
        }, true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
}
