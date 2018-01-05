package com.changcai.buyer.ui.quote;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changcai.buyer.R;

/**
 * Created by huangjian299 on 16/6/7.
 */
public class QuotedPriceFragmentHeadView extends LinearLayout implements View.OnClickListener {

    private  Context mContext;

    private ImageView ivDeliveryPlaceArrow;
    private TextView tvDeliveryPlaceText;

    private ImageView ivDeliveryPlaceTime;
    private TextView tvDeliveryPlaceTimeText;

    private ImageView ivProteinPriceArrow;
    private TextView tvProteinPriceText;

    private ImageView ivPricingMethodArrow;
    private TextView tvPricingMethodText;

    public QuotedPriceFragmentHeadView(Context context) {
        super(context);
    }

    public QuotedPriceFragmentHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View parentView = inflater.inflate(R.layout.price_fragment_head_layout, this, true);
        initView();
    }

    private void initView() {
        ivDeliveryPlaceArrow = (ImageView)findViewById(R.id.deliveryPlaceArrow);
        tvDeliveryPlaceText = (TextView) findViewById(R.id.deliveryPlaceText);
        this.findViewById(R.id.deliveryPlace).setOnClickListener(this);

        ivDeliveryPlaceTime = (ImageView)findViewById(R.id.deliveryPlaceArrow);
        tvDeliveryPlaceTimeText = (TextView) findViewById(R.id.deliveryPlaceText);
        findViewById(R.id.deliveryTime).setOnClickListener(this);

        ivProteinPriceArrow = (ImageView)findViewById(R.id.proteinPriceArrow);
        tvProteinPriceText = (TextView) findViewById(R.id.proteinPriceText);
        findViewById(R.id.proteinPrice).setOnClickListener(this);

    }

    public QuotedPriceFragmentHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onClick(View v) {

    }
}
