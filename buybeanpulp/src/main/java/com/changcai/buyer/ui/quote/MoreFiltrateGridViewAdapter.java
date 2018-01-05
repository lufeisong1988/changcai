package com.changcai.buyer.ui.quote;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.FiltrateDetailModel;
import com.changcai.buyer.listener.CustomListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liuxingwei on 2017/2/13.
 */
public class MoreFiltrateGridViewAdapter extends BaseAdapter {
    private List<FiltrateDetailModel.FilterListBean> filterList;
    private LayoutInflater inflater;
    private CustomListener customListener;

    private SparseBooleanArray sparseBooleanArray;

    public MoreFiltrateGridViewAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        sparseBooleanArray = new SparseBooleanArray();
    }

    public SparseBooleanArray getSparseBooleanArray() {
        return sparseBooleanArray;
    }

    public void setSparseBooleanArray(SparseBooleanArray sparseBooleanArray) {
        this.sparseBooleanArray = sparseBooleanArray;
    }

    private void initSparseBooleanArray() {
        if (filterList != null) {
            for (int i = 0; i < filterList.size(); i++) {
                if (i == 0) {
                    sparseBooleanArray.put(i, true);
                } else {
                    sparseBooleanArray.put(i, false);
                }
            }
        }
    }


    public void setFilterList(List<FiltrateDetailModel.FilterListBean> filterList,boolean resetSparseBooleanArray) {
        this.filterList = filterList;
        if (sparseBooleanArray.size()==0 || resetSparseBooleanArray) {
            initSparseBooleanArray();
        }
    }

    public void setCustomListener(CustomListener customListener) {
        this.customListener = customListener;
    }

    @Override
    public int getCount() {
        return filterList == null ? 0 : filterList.size();
    }

    @Override
    public Object getItem(int position) {
        return filterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getFiltrateId(int position) {
        return filterList.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.more_filtrate_grid_view_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvMoreFiltrateItemContentName.setText(filterList.get(position).getName());
        viewHolder.tvMoreFiltrateItemContentName.setSelected(sparseBooleanArray.get(position));
        viewHolder.tvMoreFiltrateItemContentName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customListener != null) {
                    customListener.onCustomerListener(v, position);
                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_more_filtrate_item_content_name)
        TextView tvMoreFiltrateItemContentName;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
