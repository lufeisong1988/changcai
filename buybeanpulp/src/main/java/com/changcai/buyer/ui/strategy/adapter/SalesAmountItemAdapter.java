package com.changcai.buyer.ui.strategy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.ui.strategy.bean.SalesAmountItemBean;

import java.util.List;

/**
 * Created by lufeisong on 2017/9/3.
 */

public class SalesAmountItemAdapter extends BaseAdapter {
    private List<SalesAmountItemBean.SalesDetailBean> list;
    private Context context;

    public SalesAmountItemAdapter(List<SalesAmountItemBean.SalesDetailBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_salesamount_item, null);
            convertView.setTag(vh);
            vh.tv_rank = (TextView) convertView.findViewById(R.id.tv_salesamount_item_rank);
            vh.tv_name = (TextView) convertView.findViewById(R.id.tv_salesamount_item_name);
            vh.tv_widget = (TextView) convertView.findViewById(R.id.tv_salesamount_item_widget);
            vh.tv_money = (TextView) convertView.findViewById(R.id.tv_salesamount_item_money);
            vh.divider_header = convertView.findViewById(R.id.divider_header);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.divider_header.setVisibility(View.GONE);
        SalesAmountItemBean.SalesDetailBean salesDetailBean = list.get(position);
        vh.tv_rank.setText(String.valueOf(position + 1));
        vh.tv_name.setText(salesDetailBean.getName());
        vh.tv_widget.setText(salesDetailBean.getVolume() + "");
        vh.tv_money.setText(salesDetailBean.getReceivable() + "");

        return convertView;
    }

    class ViewHolder {

        TextView tv_rank;
        TextView tv_name;
        TextView tv_widget;
        TextView tv_money;
        View divider_header;
    }
}
