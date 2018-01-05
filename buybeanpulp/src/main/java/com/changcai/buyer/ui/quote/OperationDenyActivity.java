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
public class OperationDenyActivity extends BaseActivity {

    private TextView tv_tips;
    private String title;
    private String tips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation_deny);

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
        }
        tv_tips = (TextView) findViewById(R.id.tv_tips);

        if(!TextUtils.isEmpty(tips)) {
            tv_tips.setText(tips);
        }
    }
}
