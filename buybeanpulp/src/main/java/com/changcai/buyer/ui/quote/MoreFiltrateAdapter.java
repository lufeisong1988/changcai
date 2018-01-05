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
import com.changcai.buyer.listener.CustomParentWithChildListener;
import com.changcai.buyer.view.MyGridView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liuxingwei on 2017/2/13.
 */
public class MoreFiltrateAdapter extends BaseAdapter {


    private CustomParentWithChildListener parentCustomListener;
    private Context context;
    private LayoutInflater inflater = null;

    private List<FiltrateDetailModel> filtrateDetailModels;

    private boolean resetBooleanArray;

    public boolean isResetBooleanArray() {
        return resetBooleanArray;
    }

    public void setResetBooleanArray(boolean resetBooleanArray) {
        this.resetBooleanArray = resetBooleanArray;
    }

    public void setParentCustomListener(CustomParentWithChildListener parentCustomListener) {
        this.parentCustomListener = parentCustomListener;
    }

    public MoreFiltrateAdapter(Context context ) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setFiltrateDetailModels(List<FiltrateDetailModel> filtrateDetailModels) {
        this.filtrateDetailModels = filtrateDetailModels;
    }

    @Override
    public int getCount() {
        return filtrateDetailModels == null ? 0 : filtrateDetailModels.size();
    }


    @Override
    public Object getItem(int position) {
        return filtrateDetailModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getGridType(int position) {
        return filtrateDetailModels.get(position).getFilterType();
    }

    public FiltrateDetailModel getItemModel(int position){
        return filtrateDetailModels.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_view_more_filtrate, null);
            viewHolder = new ViewHolder(convertView, context);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvMoreFiltrateItemName.setText(filtrateDetailModels.get(position).getFilterName());
        viewHolder.gvMoreFiltrateContent.setAdapter(viewHolder.moreFiltrateGridViewAdapter);
        viewHolder.moreFiltrateGridViewAdapter.setFilterList(filtrateDetailModels.get(position).getFilterList(),isResetBooleanArray());
        viewHolder.moreFiltrateGridViewAdapter.notifyDataSetChanged();
        viewHolder.moreFiltrateGridViewAdapter.setCustomListener(new CustomListener() {
            @Override
            public void onCustomerListener(View v, int childPosition) {
                SparseBooleanArray sparseBooleanArray = viewHolder.moreFiltrateGridViewAdapter.getSparseBooleanArray();
                for (int i = 0; i < sparseBooleanArray.size(); i++) {
                    if (i==childPosition){
                        sparseBooleanArray.put(i,true);
                    }else{
                        sparseBooleanArray.put(i,false);
                    }
                }
                viewHolder.moreFiltrateGridViewAdapter.setSparseBooleanArray(sparseBooleanArray);
                viewHolder.moreFiltrateGridViewAdapter.notifyDataSetChanged();
                if (parentCustomListener !=null){
                    parentCustomListener.onCustomerListener(v,position,childPosition);
                }
            }
        });
        return convertView;
    }



    static class ViewHolder {
        @BindView(R.id.tv_more_filtrate_item_name)
        TextView tvMoreFiltrateItemName;
        @BindView(R.id.gv_more_filtrate_content)
        MyGridView gvMoreFiltrateContent;

        MoreFiltrateGridViewAdapter moreFiltrateGridViewAdapter;

        ViewHolder(View view, Context context) {
            ButterKnife.bind(this, view);
            moreFiltrateGridViewAdapter = new MoreFiltrateGridViewAdapter(context);
        }
    }
}
