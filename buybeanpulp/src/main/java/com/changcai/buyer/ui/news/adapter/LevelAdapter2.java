package com.changcai.buyer.ui.news.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.ui.news.bean.NewsClassify;
import com.changcai.buyer.ui.news.bean.SubClassified;

import java.util.List;

/**
 * Created by liuxingwei on 2016/11/8.
 */
public class LevelAdapter2 extends BaseAdapter{

    private Context context;
    private List<SubClassified> data;

    private SparseBooleanArray sparseBooleanArray;

    public LevelAdapter2(Context context, List<SubClassified> data) {
        this.context = context;
        this.data = data;
        init();
    }

    public void setData(List<SubClassified> data){
        this.data = data;
        init();
        notifyDataSetChanged();
    }

    private void init() {
        sparseBooleanArray = new SparseBooleanArray(data==null?0:data.size());
        for (int i = 0; i < sparseBooleanArray.size(); i++) {
            sparseBooleanArray.put(i, false);
        }
    }

    @Override
    public int getCount() {
        return null == data ? 0 : data.size();
    }

    @Override
    public SubClassified getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_classify_morelist, parent, false);
            viewHolder.layout = (LinearLayout) convertView.findViewById(R.id.moreitem_layout);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.moreitem_txt);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.layout.setBackgroundColor(context.getResources().getColor(R.color.item_background_gray));
        viewHolder.tvContent.setText(data.get(position).getName());
        if (sparseBooleanArray.get(position)){
            viewHolder.tvContent.setTextColor(context.getResources().getColor(R.color.global_blue));
        }else {
            viewHolder.tvContent.setTextColor(context.getResources().getColor(R.color.black));
        }
        return convertView;
    }


    public SparseBooleanArray getSparseBooleanArray() {
        return sparseBooleanArray;
    }

    private class ViewHolder {
        private LinearLayout layout;
        private TextView tvContent;
    }
}
