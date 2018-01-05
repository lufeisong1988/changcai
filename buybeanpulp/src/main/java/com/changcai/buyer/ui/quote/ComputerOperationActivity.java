package com.changcai.buyer.ui.quote;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.ui.base.BaseActivity;

/**
 * 电脑操作页面
 */
public class ComputerOperationActivity extends BaseActivity {

    private TextView tv_tips, tv_info;
    private String title;
    private String tips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_computer_operation);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            title = bundle.getString("title");
            tips = bundle.getString("tips");
        }
        initTitle();

        initView();
    }

    private void initView() {
        //this.findViewById(R.id.line).setVisibility(View.GONE);
        btnBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        if(!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        } else {
            tvTitle.setText("报价详情");
        }
        tv_tips = (TextView) findViewById(R.id.tv_tips);
        tv_info = (TextView) findViewById(R.id.tv_info);

        if(!TextUtils.isEmpty(tips)) {
            tv_tips.setText(tips);
            tv_info.setText(R.string.computer_operation_message_1);
        }
    }
}
