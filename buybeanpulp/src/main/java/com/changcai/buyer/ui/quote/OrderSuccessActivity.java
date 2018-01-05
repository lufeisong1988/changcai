package com.changcai.buyer.ui.quote;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.PriceInfo;
import com.changcai.buyer.ui.base.BaseActivity;
import com.changcai.buyer.ui.main.MainActivity;
import com.changcai.buyer.ui.order.OrderDetailActivity;
import com.changcai.buyer.ui.order.bean.OrderInfo;
import com.changcai.buyer.util.DateUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.UserDataUtil;
import com.changcai.buyer.view.TimeTextView;

/**
 * 订单成功界面
 */
public class OrderSuccessActivity extends BaseActivity implements View.OnClickListener {

    private TimeTextView tvTime;
    private TextView tv_message;
    private Button btnOrderDetail;
    private Button btnGoBackQuote;
    private String orderId;
    private boolean isFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        initTitle();

        initView();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            long leaveTime = bundle.getLong("time");
            if(leaveTime > 0) {
                long[] timer = DateUtil.diffDate(leaveTime, "yyyy-MM-dd HH:mm:ss");
                tvTime.setTimes(timer);
                tvTime.setCallBack(new TimeTextView.ITimerViewCallBack() {
                    @Override
                    public void onTimerOut() {
//                        showTimeOutView();
                    }
                });
                tvTime.run();
            }
            orderId = bundle.getString("orderId");
        }
    }

    private void initView() {

        btnBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("下单成功");

        tvTime  = (TimeTextView) this.findViewById(R.id.time);
        btnOrderDetail = (Button) this.findViewById(R.id.order_detail);
        btnOrderDetail.setOnClickListener(this);
        btnGoBackQuote = (Button) this.findViewById(R.id.go_back_quote);
        btnGoBackQuote.setOnClickListener(this);

        tv_message = (TextView) this.findViewById(R.id.time);

        if(UserDataUtil.isFactory()) {
            tv_message.setText("请联系财务员在报价有效时间内尽快签署合同并支付保证金。");
        } else {
            tv_message.setText("请在报价有效时间内尽快签署合同并支付保证金。");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.order_detail:
                if(TextUtils.isEmpty(orderId)) {
                    Toast.makeText(this, "暂不支持，接口需要返回订单", Toast.LENGTH_SHORT).show();
                } else {
                    Bundle b = new Bundle();
                    b.putString("orderId", orderId);
                    gotoActivity(OrderDetailActivity.class, b);
                }
                break;
            case R.id.go_back_quote:

                Intent intent  = new Intent(this, MainActivity.class);
                intent.putExtra("switchFragment","1");
                startActivity(intent);
                break;
        }
    }
}
