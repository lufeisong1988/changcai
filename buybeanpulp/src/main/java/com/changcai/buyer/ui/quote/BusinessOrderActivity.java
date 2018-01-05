package com.changcai.buyer.ui.quote;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.ui.base.BaseActivity;
import com.changcai.buyer.ui.order.OrderDetailActivity;
import com.changcai.buyer.util.AppManager;
import com.changcai.buyer.view.CustomFontTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liuxingwei on 2017/2/13.
 */
public class BusinessOrderActivity extends BaseActivity {

    @BindView(R.id.tv_waiting)
    CustomFontTextView tvWaiting;
    @BindView(R.id.tv_check_my_order)
    TextView tvCheckMyOrder;
    @BindView(R.id.ctv_other)
    CustomFontTextView ctvOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_order_activity);
        ButterKnife.bind(this);
        initTitle();
        tvWaiting.setText(getString(R.string.order_success_message_4, getIntent().getExtras().getString("financeContractPhone")));
        btnBack.setImageResource(R.drawable.icon_nav_back);
        btnBack.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.tv_check_my_order, R.id.ctv_other})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_check_my_order:
                gotoActivity(OrderDetailActivity.class,getIntent().getExtras());
                AppManager.getAppManager().finishActivity(BusinessOrderActivity.class, OrderActivity.class, QuoteDetailActivity.class);
                break;
            case R.id.ctv_other:
                AppManager.getAppManager().finishActivity(BusinessOrderActivity.class,OrderActivity.class,QuoteDetailActivity.class);
                break;
        }
    }


}
