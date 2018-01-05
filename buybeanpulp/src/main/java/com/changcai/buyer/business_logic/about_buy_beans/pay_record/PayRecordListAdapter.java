package com.changcai.buyer.business_logic.about_buy_beans.pay_record;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.ui.order.bean.Payment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liuxingwei on 2017/4/6.
 */

class PayRecordListAdapter extends BaseAdapter {

    private Context context;
    private List<Payment> pricingInfoList;

    public PayRecordListAdapter(Context context, List<Payment> pricingInfoList) {
        this.context = context;
        this.pricingInfoList = pricingInfoList;
    }

    @Override
    public int getCount() {
        return pricingInfoList == null ? 0 : pricingInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return pricingInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.list_fragment_pay_item, parent, false);
            viewHolder = new ViewHolder(convertView);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Payment payment = pricingInfoList.get(position);
        viewHolder.tvPayAction.setText(payment.getPayObjectZh());
        viewHolder.tvPayIncomingAndOutgoings.setText(payment.getSymbolZh().concat(payment.getAmountZh()));
        viewHolder.tvPayRecordTime.setText(payment.getCreateTime());

        if (position == pricingInfoList.size()-1){
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, context.getResources().getDimensionPixelOffset(R.dimen.dim1));
            viewHolder.viewDivider.setLayoutParams(layoutParams);
        }else{
            RelativeLayout.LayoutParams layoutParamsMargin = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, context.getResources().getDimensionPixelOffset(R.dimen.dim1));
            layoutParamsMargin.setMargins(20,0,0,0);
            viewHolder.viewDivider.setLayoutParams(layoutParamsMargin);
        }
        return convertView;
    }



    static class ViewHolder {
        @BindView(R.id.tv_pay_record_time)
        TextView tvPayRecordTime;
        @BindView(R.id.tv_pay_action)
        TextView tvPayAction;
        @BindView(R.id.tv_pay_incoming_and_outgoings)
        TextView tvPayIncomingAndOutgoings;
        @BindView(R.id.view_divider)
        View viewDivider;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
