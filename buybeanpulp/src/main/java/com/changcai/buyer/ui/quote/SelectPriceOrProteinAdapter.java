package com.changcai.buyer.ui.quote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.ui.quote.bean.Condition;
import com.changcai.buyer.ui.quote.bean.ConditionValue;

import java.util.List;

/**
 * 筛选蛋白价格或者报价方式
 * Created by huangjian299 on 16/6/7.
 */
public class SelectPriceOrProteinAdapter extends BaseAdapter {

    private List<ConditionValue> dataList;
    private LayoutInflater inflater = null;
    private int currentPosition;
    private Context context;

    public SelectPriceOrProteinAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setData(List<ConditionValue> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public void setCuttentPosition(int position) {
        currentPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (dataList == null) {
            return 0;
        } else {
            return dataList.size();
        }

    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.price_fragment_select_price_adapter_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.icon);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.llSelectLayout = (LinearLayout) convertView.findViewById(R.id.selectLayout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ConditionValue conditionValue = dataList.get(position);
        if (conditionValue != null) {
            viewHolder.tvName.setText(conditionValue.getName());
        }

        if (currentPosition == position) {
            viewHolder.ivIcon.setVisibility(View.VISIBLE);
            viewHolder.tvName.setTextColor(context.getResources().getColor(R.color.global_blue));
            viewHolder.llSelectLayout .setBackgroundColor(context.getResources().getColor(R.color.item_background_gray));
        } else {
            viewHolder.ivIcon.setVisibility(View.INVISIBLE);
            viewHolder.tvName.setTextColor(context.getResources().getColor(R.color.black));
            viewHolder.llSelectLayout .setBackgroundColor(context.getResources().getColor(R.color.white));
        }

        return convertView;
    }

    private class ViewHolder {
        public ImageView ivIcon;
        public TextView tvName;
        public LinearLayout llSelectLayout;

    }
}
