package com.changcai.buyer.ui.strategy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.Articles;
import com.changcai.buyer.view.CustomFontTextView;
import com.changcai.buyer.view.PromptHeaderView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liuxingwei on 2017/7/28.
 */

public class PromptGoodsAdapter extends BaseAdapter {

    protected Context mContext;

    private LayoutInflater layoutInflater;

    protected List<Articles> articlesList;

    public PromptGoodsAdapter(Context mContext, List<Articles> articlesList) {
        this.mContext = mContext;
        this.articlesList = articlesList;
        layoutInflater = LayoutInflater.from(this.mContext);
    }

    public PromptGoodsAdapter(Context mContext) {
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(this.mContext);
    }


    @Override
    public int getCount() {
        return articlesList == null ? 0 : articlesList.size();
    }

    @Override
    public Object getItem(int position) {
        return articlesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Articles articles = articlesList.get(position);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.prompt_goods_item, null, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.contentText.setText(articles == null ? "" : articles.getContent());
        viewHolder.titleText.setText(articles==null?"":articles.getTitle());
        viewHolder.timeText.setCustomFontText(articles==null?"":articles.getCreateTime());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.titleText)
        TextView titleText;
        @BindView(R.id.contentText)
        CustomFontTextView contentText;
        @BindView(R.id.timeText)
        PromptHeaderView timeText;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
