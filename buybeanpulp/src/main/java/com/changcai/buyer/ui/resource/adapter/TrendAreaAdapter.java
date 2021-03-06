package com.changcai.buyer.ui.resource.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.ui.resource.bean.DomainsAndTypesBean;
import com.changcai.buyer.view.CustomFontTextView;

import java.util.List;

/**
 * Created by lufeisong on 2017/8/30.
 */

public class TrendAreaAdapter extends BaseAdapter {
    private Context context;
    private List<DomainsAndTypesBean.DomainsBean> list;
    private int position = 0;
    private TrendAreaAdapter.ViewHolder hold;

    public TrendAreaAdapter(Context context, List<DomainsAndTypesBean.DomainsBean> list) {
        this.context = context;
        this.list = list;
    }

    public TrendAreaAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<DomainsAndTypesBean.DomainsBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public String getSelectItemId() {
        return String.valueOf(list.get(getSelectItem()).getId());
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = View.inflate(context, R.layout.item_classify_mainlist, null);
            hold = new TrendAreaAdapter.ViewHolder(view);
            view.setTag(hold);
        } else {
            hold = (TrendAreaAdapter.ViewHolder) view.getTag();
        }
        DomainsAndTypesBean.DomainsBean conditionValue = list.get(position);
        if (conditionValue != null)
            hold.txt.setText(conditionValue.getName());
        hold.txt.setTextColor(0xFF26272A);
//        hold.txt.setTextSize(TypedValue.COMPLEX_UNIT_PX,26);
        hold.txt.setPadding(40,0,0,0);
        hold.layout.setBackgroundColor(Color.parseColor("#F9FAFB"));
        if (this.position == position) {
            hold.txt.setTextColor(0xFF408AFF);
//            hold.layout.setBackgroundColor(Color.WHITE);
        }
        if (position == this.position) {
            hold.imageViewMore.setVisibility(View.VISIBLE);
            hold.imageViewMore.setImageResource(R.drawable.icon_inline_sel);
        } else {
            hold.imageViewMore.setVisibility(View.GONE);

        }
        return view;
    }

    public void setSelectItem(int position) {
        this.position = position;
        notifyDataSetChanged();
    }

    public int getSelectItem() {
        return position;
    }

    private static class ViewHolder {
        LinearLayout layout;
        TextView txt;
        ImageView imageViewMore;
        LinearLayout parent;

        public ViewHolder(View view) {
            txt = (CustomFontTextView) view.findViewById(R.id.mainitem_txt);
            layout = (LinearLayout) view.findViewById(R.id.mainitem_layout);
            imageViewMore = (ImageView) view.findViewById(R.id.iv_more);
            parent = (LinearLayout) view.findViewById(R.id.ll_city_parent);
        }
    }
}


