package com.changcai.buyer.ui.news.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.ui.news.bean.NewsClassify;
import com.changcai.buyer.ui.news.bean.NewsEntity;

import java.util.List;

/**
 * Created by liuxingwei on 2016/11/8.
 */
public class LevelAdapter extends BaseAdapter {

    private Context context;
    private List<NewsClassify> data;

    private SparseBooleanArray sparseBooleanArray;

    public LevelAdapter(Context context, List<NewsClassify> data) {
        this.context = context;
        this.data = data;
        init();
    }

    private void init() {
        sparseBooleanArray = new SparseBooleanArray(data.size());
        for (int i = 0; i < sparseBooleanArray.size(); i++) {
            sparseBooleanArray.put(i, false);
        }
        sparseBooleanArray.put(0,true);
    }

    @Override
    public int getCount() {
        return null == data ? 0 : data.size();
    }

    @Override
    public NewsClassify getItem(int position) {
        return data.get(position);
    }

    public NewsClassify getSelectedItem(){
        return data.get(getSelectedPosition());
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_classify_mainlist, parent, false);
            viewHolder.layout = (LinearLayout) convertView.findViewById(R.id.mainitem_layout);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.mainitem_txt);
            viewHolder.ivMoreIcon = (ImageView) convertView.findViewById(R.id.iv_more);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvContent.setText(data.get(position).getName());
        if (sparseBooleanArray.get(position)){
            viewHolder.tvContent.setTextColor(context.getResources().getColor(R.color.global_blue));
            viewHolder.layout.setBackgroundColor(context.getResources().getColor(R.color.item_background_gray));
        }else {
            viewHolder.tvContent.setTextColor(context.getResources().getColor(R.color.black));
            viewHolder.layout.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        if (position == 0){
            viewHolder.ivMoreIcon.setVisibility(View.GONE);
        }else {
            viewHolder.ivMoreIcon.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    public SparseBooleanArray getSparseBooleanArray() {
        return sparseBooleanArray;
    }

    public int getSelectedPosition(){
        int position=0;
        for (int i=0;i<sparseBooleanArray.size();i++){
            if (sparseBooleanArray.get(i)){
                position = i;
                break;
            }
        }
        return position;
    }


    private class ViewHolder {
        private LinearLayout layout;
        private TextView tvContent;
        private ImageView ivMoreIcon;
    }
}
