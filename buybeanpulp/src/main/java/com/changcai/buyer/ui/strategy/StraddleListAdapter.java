package com.changcai.buyer.ui.strategy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.StraddleModel;
import com.changcai.buyer.listener.CustomListener;
import com.changcai.buyer.view.PromptHeaderView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liuxingwei on 2017/7/28.
 */

public class StraddleListAdapter extends BaseAdapter {


    protected Context mContext;

    private LayoutInflater layoutInflater;
    private CustomListener customListener;
    private List<StraddleModel> straddleModelList;

    public StraddleListAdapter(Context mContext, List<StraddleModel> straddleModelList) {
        this.mContext = mContext;
        this.straddleModelList = straddleModelList;
        layoutInflater = LayoutInflater.from(mContext);
    }

    public StraddleListAdapter(Context mContext) {
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return straddleModelList == null ? 0 : straddleModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return straddleModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        StraddleModel straddleModel = straddleModelList.get(position);
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.straddle_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customListener != null) {
                    customListener.onCustomerListener(v, position);
                }
            }
        });
        viewHolder.contentText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customListener != null) {
                    customListener.onCustomerListener(v, position);
                }
            }
        });
        viewHolder.straddleParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customListener != null) {
                    customListener.onCustomerListener(v, position);
                }
            }
        });
        viewHolder.timeText.setCustomFontText(straddleModel==null?"":straddleModel.getCreateTime());
        viewHolder.contentText.setText(straddleModel==null?"":straddleModel.getTitle());
        return convertView;
    }

    public void setCustomListener(CustomListener customListener) {
        this.customListener = customListener;
    }

    static class ViewHolder {
        @BindView(R.id.timeText)
        PromptHeaderView timeText;
        @BindView(R.id.contentText)
        TextView contentText;
        @BindView(R.id.straddleParent)
        LinearLayout straddleParent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
