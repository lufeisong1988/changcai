package com.changcai.buyer.business_logic.about_buy_beans.point_price;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.ui.order.bean.PricingInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liuxingwei on 2017/4/6.
 */

public class PointPriceListAdapter extends BaseAdapter {

    private Context context;
    private List<PricingInfo> pricingInfoList;

    public PointPriceListAdapter(Context context, List<PricingInfo> pricingInfoList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.list_fragment_price_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PricingInfo pricingInfo = pricingInfoList.get(position);
        viewHolder._tvPointPriceTime.setText(pricingInfo.getCreateTime() == null ? "null" : pricingInfo.getCreateTime());
        viewHolder._tvApplyForCount.setText("/".concat(pricingInfo.getSuccessQuantity()).concat("手"));
        viewHolder._tvDealCount.setText(pricingInfo.getApplyQuantity());
        viewHolder._tvFuturesPriceRmb.setText(pricingInfo.getPrice().concat("元"));

        if (position == 0){
            viewHolder._viewTop.setVisibility(View.VISIBLE);
        }else {
            viewHolder._viewTop.setVisibility(View.GONE);
        }

        return convertView;
    }



    static class ViewHolder {
        @BindView(R.id.tv_point_price_time)
        TextView _tvPointPriceTime;
        @BindView(R.id.tv_futures_price_rmb)
        TextView _tvFuturesPriceRmb;
        @BindView(R.id.tv_deal_count)
        TextView _tvDealCount;
        @BindView(R.id.tv_apply_for_count)
        TextView _tvApplyForCount;
        @BindView(R.id.tv_point_status)
        TextView _tvPointStatus;
        @BindView(R.id.viewLastLine)
        View _viewLastLine;
        @BindView(R.id.viewTop)
        View _viewTop;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
